package cn.charlema.service.debug;

import cn.charlema.models.dto.CompileOptions;
import cn.charlema.models.dto.CompileResult;
import cn.charlema.models.dto.StartDebugOptions;
import cn.charlema.models.dto.DebugResult;
import org.springframework.stereotype.Component;

/**
 * 用于提供代码编译过程的服务
 */
@Component
public interface DebugHandler {

    /**
     *
     * @param compileFiles 需要编译的文件
     * @param OutputFilePath    编译后输出的文件路径
     * @param compileOptions    编译文件的相关信息
     * @return
     */
    public CompileResult Compile(String compileFiles, String OutputFilePath, CompileOptions compileOptions);


    public DebugResult Start(String execFile,StartDebugOptions options) throws Exception;
}
