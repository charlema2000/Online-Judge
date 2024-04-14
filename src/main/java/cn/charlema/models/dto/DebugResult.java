package cn.charlema.models.dto;


/**
 * 代码调试后输出的结果类
 */
public class DebugResult {
    /**
     * IsEnd        bool      // 调试是否结束
     * 	File         string    // 运行到哪个文件
     * 	Function     string    // 运行到那个函数
     * 	Line         int       // 行号
     * 	UserOutput   string    // 用户输出
     * 	ErrorMessage string    // 异常信息，比如运行超时
     * 	BackTrace    BackTrace // 调用栈信息
     * 	NeedInput    int       // 是否需要输入
     */
    public Boolean IsEnd;

    public String File;

    public String Function;

    public int Line;
    public String UserOutput;

    public String ErrorMessage;

    public StackFrame[] BackTrace; // 调用栈信息

    public int NeedInput;


}
