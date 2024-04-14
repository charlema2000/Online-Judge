package cn.charlema.models.pojo;

import java.util.Date;

public class Submission {

    private Integer userID;

    private Integer problemID;

    private String language;

    private String core;

    /**
     * 异常日志输出
     */
    private String errorMessage;

    /**
     * 用户代码结果输出
     */
    private String userOutput;


    /**
     * 提交记录时间
     */
    private Date submissTime;

}
