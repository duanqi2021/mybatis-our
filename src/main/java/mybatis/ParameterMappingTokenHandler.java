package mybatis;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class ParameterMappingTokenHandler implements TokenHandler {
	private List<ParameterMapping> parameterMappings = new ArrayList<>();

	// context是参数名称 #{id} #{username}
	@Override
	public String handleToken(String content) {
		parameterMappings.add(buildParameterMapping(content));
		return "?";
	}

	// 创建ParameterMapping对象根据字段名称
	private ParameterMapping buildParameterMapping(String content) {
		return new ParameterMapping(content);
	}
}
