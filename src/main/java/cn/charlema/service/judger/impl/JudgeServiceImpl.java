package cn.charlema.service.judger.impl;

import cn.charlema.models.dto.CompileOptions;
import cn.charlema.models.dto.CompileResult;
import cn.charlema.models.dto.ExecuteRequestDto;
import cn.charlema.models.dto.ExecuteResultDto;
import cn.charlema.service.debug.DebugHandler;
import cn.charlema.service.judger.JudgeService;
import cn.charlema.service.judger.UtilsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class JudgeServiceImpl implements JudgeService {
    
    private static final Long LimitCompileTime  = (long)10*1000;

    private static final Long LimitExecuteTime = (long)15*1000;
    
    @Autowired
    UtilsService utilsService;

    @Autowired
    DebugHandler debugHandler;


    /**
     *执行用户代码
     * @param executeRequestDto 客户端发送的代码请求体
     * @return
     */
    public ExecuteResultDto Execute(ExecuteRequestDto executeRequestDto) {
        ExecuteResultDto executeResult = new ExecuteResultDto();

        //生成临时存放文件的目录
        String executePath = utilsService.GetExecutePath();
        File file = new File(executePath);
        if (!file.exists()){
            boolean dr = file.mkdirs();
        }

        // 保存用户代码到用户的执行路径，并获取编译文件列表
        String compileFiles = saveUserCode(executeRequestDto.getLanguage(),executeRequestDto.getCore(),executePath);

        // 输出的执行文件路劲
        String executeFilePath = Paths.get(executePath, "main").toString();

        CompileOptions compileOptions = new CompileOptions(executeFilePath,executeRequestDto.getLanguage(),LimitCompileTime);

        CompileResult compileResult = null;
        try{
            compileResult = debugHandler.Compile(compileFiles,executeFilePath,compileOptions);
        }catch (Exception e){
            return null;
        }


        return null;
    }

    /**
     *保存用户代码到用户的executePath，并返回需要编译的文件列表
     * @param language 代码的语言
     * @param code 代码内容
     * @param executePath 存放编译文件的目录
     * @return
     */
    public String saveUserCode(String language,String code,String executePath) {
        String fileName = getMainFileNameByLanguage(language);
        String compilerFile = Paths.get(executePath,fileName).toString();
        FileWriter writer = null;
        try{
            writer = new FileWriter(compilerFile);
            writer.write("");//清空原文件内容
            writer.write(compilerFile);
            writer.flush();
            writer.close();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        return compilerFile;
    }

    /**
     * 根据语言生成main文件的名字
     * @param language
     * @return
     */
    public String getMainFileNameByLanguage(String language){
        switch (language){
            case "java": return "main.java";
            case  "c"  : return "main.c";
            default: return null;
        }
    }
}
