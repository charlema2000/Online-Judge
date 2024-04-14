package cn.charlema.models.dto;


import lombok.Data;

/**
 *执行代码请求所设置类
 */
@Data
public class ExecuteRequestDto {

    private Integer problemID;

    private String core;

    private String language;

    private String input;

}
