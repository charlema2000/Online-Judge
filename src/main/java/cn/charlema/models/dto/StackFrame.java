package cn.charlema.models.dto;

/**
 *StackFrame 表示一个栈帧，包含当前栈帧的函数名称，执行的位置等信息
 */
public class StackFrame {

    public String Function; // 调用函数名称
    public String Args; //参数
    public String File; // 函数所在文件
    public int Line; // 方法返回地址
}
