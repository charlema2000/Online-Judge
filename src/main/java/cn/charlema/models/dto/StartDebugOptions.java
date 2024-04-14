package cn.charlema.models.dto;

import com.sun.jdi.event.BreakpointEvent;

/**
 * 初次调试代码可选参数
 */
public class StartDebugOptions {

    /**
     * '工作路径 workPath工作目录，调试过程中产生的文件可能会保存在该目录内
     */
    public String workPath;

    /**
     * 存储多个断点事件
     */
    public BreakpointEvent[] breakpointEvents;

    /**
     * 超时时间限制
     */
    public Long LimitTime;

    /**
     * 如果存在调试文件则直接在文件内部执行
     */
    public String DebugFile;


}
