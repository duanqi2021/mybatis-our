package mybatis;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParameterMapping {
    // 保存#{}中对于的字段名称
    private String content;
}
