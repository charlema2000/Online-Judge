package cn.charlema.service.judger;

import cn.charlema.models.dto.ExecuteRequestDto;
import cn.charlema.models.dto.ExecuteResultDto;
import org.springframework.stereotype.Component;

/**
 * 判题模块的服务
 * 编译、运行、保存代码
 */
@Component
public interface JudgeService {

    ExecuteResultDto Execute(ExecuteRequestDto executeRequestDto);

    String saveUserCode(String language,String code,String executePath);

}
