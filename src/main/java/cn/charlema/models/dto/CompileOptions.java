package cn.charlema.models.dto;

import lombok.Data;

/**
 * 编译代码文件的类
 */
@Data
public class CompileOptions {

    /**
     * 编译文件路径
     */
    public String executeFilePath;

    /**
     * 编译文件的语言
     */
    public String language;

    /**
     * 编译限制时间
     */
    public Long limteCompileTime;


    /**
     *代替文件目录路径
     */
    public String ReplacementPath;

    public CompileOptions(){};

    public CompileOptions(String executeFilePath, String language, Long limteCompileTime) {
        this.executeFilePath = executeFilePath;
        this.language = language;
        this.limteCompileTime = limteCompileTime;
//        ReplacementPath = replacementPath;
    }
}
