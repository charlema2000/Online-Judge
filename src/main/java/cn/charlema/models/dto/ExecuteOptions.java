package cn.charlema.models.dto;

import net.sf.jsqlparser.statement.select.Limit;

/**
 * 执行文件可选操作
 */
public class ExecuteOptions {

    public String language;
    public Long LimitTIme; // 资源限制


//    Language        string
//    LimitTime       int64 // 资源限制
//    MemoryLimit     int64
//    CPUQuota        int64
//    ExcludedPaths   []string // 屏蔽的敏感路径
//    ReplacementPath string   // 取代敏感路径的路径
}
