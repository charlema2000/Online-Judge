package cn.charlema.models.pojo;

import java.util.Date;

public class Problem {

    /**
     * 题目id
     */
    private Integer problemID;

    /**
     * 题目名称
     */
    private String problemName;

    /**
     *
     */
    private Integer createUserID;

    private Date createTime;

    private Date updateTime;

    private String languages;

    private String introduction;

    private Integer level;

}
