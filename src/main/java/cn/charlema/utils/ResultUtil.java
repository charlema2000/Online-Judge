package cn.charlema.utils;

import cn.charlema.models.vo.Result;

/**
 * 封装结果工具类
 */
public class ResultUtil {

    /**
     * 请求成功
     * @return
     */
    public static Result success(){
        return new Result(200,"request success");
    }


}
