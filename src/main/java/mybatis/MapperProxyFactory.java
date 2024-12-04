package mybatis;

import mybatis.annotation.*;
import mybatis.typeHandler.DoubleTypeHandler;
import mybatis.typeHandler.IntegerTypeHandler;
import mybatis.typeHandler.StringTypeHandler;
import mybatis.typeHandler.TypeHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapperProxyFactory {
    private static Map<Class, TypeHandler> typeHandlerMap=new HashMap<>();
    static {
        typeHandlerMap.put(String.class,new StringTypeHandler());  //定义类型处理器
        typeHandlerMap.put(Integer.class,new IntegerTypeHandler());
        typeHandlerMap.put(Double.class,new DoubleTypeHandler());
    }
    //封装 查询 修改删除操作是否正确执行
    public static <T extends Annotation> Integer executeOkOrNot(Method method, Object[] args, Connection connection, Class<T> anno){
        try {
            final T annotation = method.getAnnotation(anno);
            Method valueMethod = anno.getMethod("value" );
            String value= (String) valueMethod.invoke(annotation);
            getResultSet(method, args, connection, value);
            return 1;
        }catch (Exception e){
            return 0;
        }
    }
    public static <T,S> T getMapper(Class<T> mapper,Class<S> domain,Connection connection)
    {
        Object instance = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{mapper},
                (Object proxy, Method method, Object[] args)-> {
          //解析sql---执行sql----结果封装
            boolean hasSelect = method.isAnnotationPresent(Select.class);
            boolean hasUpdate = method.isAnnotationPresent(Update.class);
            boolean hasDelete = method.isAnnotationPresent(Delete.class);
            boolean hasInsert = method.isAnnotationPresent(Insert.class);
            if(hasSelect)
            {
                List<S> list=new ArrayList<>();
                Select annotation = method.getAnnotation(Select.class);
                String value = annotation.value();
                ResultSet resultSet = getResultSet(method, args, connection, value);
                while (resultSet.next())
                {
                    S s = domain.newInstance();
                    Field[] fields = domain.getDeclaredFields();
                    for (Field field : fields) {
                        // 获取字段名称
                        String fieldName = field.getName();
                        Class<?> fieldType = field.getType();
                        //判断是否有DataName注解
                        // 否则根据字段名称获取对应的set方法
                        //字段名称首字母大写
                        Method setMethod = domain.getMethod("set" + capitalizeFirstLetter(fieldName), fieldType);
                        String re="";
                        if(field.isAnnotationPresent(DataName.class)){
                            re = resultSet.getString(field.getAnnotation(DataName.class).value());
                        }else{
                            re = resultSet.getString(fieldName);
                        }
                        try{
                            Method valueOfMethod = fieldType.getMethod("valueOf", String.class);
                            setMethod.invoke(s,valueOfMethod.invoke(null, re) );
                        }catch (Exception e){
                            setMethod.invoke(s,re );
                        }
                            //final Object reValue = Class.forName(fieldType.getName()).cast(re);
                            // 通过反射机制调用set方法，将resultSet对应字段的值赋值给User对象的对应字段


                    }

                    list.add(s);
                }
                if(list.size()>1){
                    return list;
                }else if(list.size()==1){
                    return list.get(0);
                }

            }else if(hasUpdate){
                return executeOkOrNot(method, args, connection, Update.class);
            }else if(hasDelete){
                return executeOkOrNot(method, args, connection, Delete.class);
            }else if(hasInsert){
                return executeOkOrNot(method, args, connection, Insert.class);
            }

            connection.close();

            return null;


        });
        return (T)instance;


    }

    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;  // 如果字符串为空或null，直接返回原字符串
        }

        // 获取第一个字符并转换为大写
        char firstChar = Character.toUpperCase(str.charAt(0));

        // 如果字符串只有一个字符，直接返回大写后的字符
        if (str.length() == 1) {
            return String.valueOf(firstChar);
        }

        // 将大写后的第一个字符与剩余部分连接起来
        return firstChar + str.substring(1);
    }
    private static ResultSet getResultSet(Method method, Object[] args, Connection connection, String value) throws SQLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Parameter[] parameterTypes = method.getParameters();
        Map<String,Object> paramMap=new HashMap<>();
        //paramMap 参数值 "name":"wu"  "id":"123"
       // Integer index=0;
        for (int i = 0; i <parameterTypes.length ; i++) {
            Parameter parameterType = parameterTypes[i];
            String value1 = parameterType.getAnnotation(Param.class).value();
            //parameterType.getName() 方法拿到的是arg0 arg1这种形式
            // 采用Param注解  采用反射得到name id 的形式
            Class<?> entityClass = args[i].getClass();
            //判断是否为基础类型 或者String
            if(!entityClass.isPrimitive()&&!entityClass.equals(String.class)){
                Field[] fields = entityClass.getDeclaredFields();
                for (int j = 0; j < fields.length; j++) {
                    if (!fields[j].getName().equalsIgnoreCase("serialVersionUID")) {
                        fields[j].setAccessible(true); // 设置可访问性，以便访问私有属性
                        Object o=fields[j].get(args[i]);
                        //判断是否有注解DataName 如果有的话 通过注解映射对应Map
                        if(fields[j].isAnnotationPresent(DataName.class)){
                            String value2 = fields[j].getAnnotation(DataName.class).value();
                            paramMap.put(value1+"."+value2,o);
                        }else{
                            paramMap.put(value1+"."+fields[j].getName(),o);
                        }
                        //paramMap.put("args"+(j+index), o);

                    }
                }
               // index+=fields.length;
            }else{
               // paramMap.put("args"+(i+index), args[i]);
                paramMap.put(value1, args[i]);
            }


        }
        ParameterMappingTokenHandler tokenHandler=new ParameterMappingTokenHandler();
        GenericTokenParser parser=new GenericTokenParser("#{","}",tokenHandler);
        String parse = parser.parse(value);
        List<ParameterMapping> parameterMappings = tokenHandler.getParameterMappings();
        PreparedStatement statement= connection.prepareStatement(parse);
        for (int i = 0; i < parameterMappings.size(); i++) {
            String parameterMapping = parameterMappings.get(i).getContent();
            Object v1=paramMap.get(parameterMapping);
            Class<?> aClass = v1.getClass();
            typeHandlerMap.get(aClass).setParameter(statement,i+1,v1);
        }

        statement.execute();
        return statement.getResultSet();
    }

    public static Connection getConnection(String ip,String datasource, String user, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url="jdbc:mysql://"+ip+":3306/"+datasource+"?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
            Connection connection= DriverManager.getConnection(url,user,password);
            return  connection;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
