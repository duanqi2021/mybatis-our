package mybatis.typeHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface TypeHandler<T> {
    void setParameter(PreparedStatement statement,int i,T value) throws SQLException;
}
