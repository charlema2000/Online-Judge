package cn.charlema.models.dto;

/**
 * 编译结果类
 */
public class CompileResult{

    public boolean compile;

    public String ErrorMessage;

    public String CompiledFilePath;


    public CompileResult(boolean compile, String errorMessage) {
        this.compile = compile;
        ErrorMessage = errorMessage;
    }
}