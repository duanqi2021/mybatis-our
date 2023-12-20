package mybatis.typeHandler;

import mybatis.typeHandler.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StringTypeHandler implements TypeHandler<String> {

    @Override
    public void setParameter(PreparedStatement statement, int i, String value) throws SQLException {
        if(value!=null){
            statement.setString(i, value);
        }
        else{
            statement.setString(i, null);
        }
    }
}
