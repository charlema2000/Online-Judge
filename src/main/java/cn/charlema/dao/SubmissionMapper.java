package cn.charlema.dao;

import cn.charlema.models.pojo.Submission;

public interface SubmissionMapper {
    /**
     * 往数据库写入提交记录
     */
    int insert(Submission submission);

    /**
     * 获取该用户的所有提交记录
     */
    Submission[] getSubmissonById(Integer UserID);

}
