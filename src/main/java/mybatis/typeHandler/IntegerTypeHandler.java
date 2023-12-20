package mybatis.typeHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IntegerTypeHandler implements TypeHandler<Integer> {

    @Override
    public void setParameter(PreparedStatement statement, int i, Integer value) throws SQLException {
        if(value!=null){
            statement.setInt(i, value);
        }
        else{
            statement.setInt(i, 0);
        }
    }
}
