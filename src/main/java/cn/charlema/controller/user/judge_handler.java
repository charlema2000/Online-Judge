package cn.charlema.controller.user;

import cn.charlema.models.dto.ExecuteRequestDto;
import cn.charlema.models.vo.Result;
import cn.charlema.utils.ResultUtil;
import org.springframework.web.bind.annotation.RequestBody;

public class judge_handler {


    public Result Execute(@RequestBody ExecuteRequestDto executeRequestDto){
        return ResultUtil.success();
    }
}
