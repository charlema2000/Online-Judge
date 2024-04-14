package cn.charlema.service.judger;

import cn.charlema.models.pojo.Submission;

import java.util.List;

public interface SubmissionService {

    //获取已经提交的答题记录
    public List<Submission> GetUserSubmissionList(Integer userID);
}
