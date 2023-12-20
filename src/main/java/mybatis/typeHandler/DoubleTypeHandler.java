package mybatis.typeHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DoubleTypeHandler implements TypeHandler<Double> {

    @Override
    public void setParameter(PreparedStatement statement, int i, Double value) throws SQLException {
        if(value!=null){
            statement.setDouble(i, value);
        }
        else{
            statement.setDouble(i, 0);
        }
    }
}
