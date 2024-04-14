package cn.charlema.service.debug.impl;

import cn.charlema.models.dto.CompileOptions;
import cn.charlema.models.dto.CompileResult;
import cn.charlema.models.dto.StartDebugOptions;
import cn.charlema.models.dto.DebugResult;
import cn.charlema.service.debug.DebugHandler;
import cn.charlema.service.judger.JudgeService;
import com.sun.jdi.event.BreakpointEvent;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.*;

public class DebugHandlerImpl implements DebugHandler {

    private static final String GdbInitFileName = "init.gdb";


    public ProcessBuilder process;

    public BreakpointEvent breakpointEvent;
    /**
    * '工作路径 workPath工作目录，调试过程中产生的文件可能会保存在该目录内
    */
    public String workPath;

    /**
     * 存储多个断点事件
     */
    public BreakpointEvent[] breakpointEvents;

    /**
     * 如果存在调试文件则直接在文件内部执行
     */
    public String DebugFile;

    public JudgeService judgeService;

    /**
     * 输入测试用例的流
     */
    public OutputStream stdin;

    /**
     * 接收调试结果的流
     */public InputStream stdout;

    /**
     * 接受调试错误信息的流
     */
    public InputStream stderr;



    @Override
    public CompileResult Compile(String compileFiles, String outputFilePath, CompileOptions compileOptions) {
        if(compileOptions==null){
            compileOptions = new CompileOptions();
        }

        CompileOptions options = new  CompileOptions(compileOptions.executeFilePath,"c",compileOptions.limteCompileTime,compileOptions.ReplacementPath);
        CompileResult result = this.Compile(compileFiles,outputFilePath,options);

        return result;
    }

    @Override
    public DebugResult Start(String execFile, StartDebugOptions options) throws Exception{

        if(options!=null&&options.workPath!=""){
            //设置工作目录最后的一个字符是“/"
            if(options.workPath.substring(options.workPath.length()-1)!="/"){
                options.workPath = options.workPath+"/";
            }
        }
        if (options!=null&&options.DebugFile!=""){
            this.DebugFile=options.DebugFile;
        }
        if (options!=null&&(options.breakpointEvents).length>=0){
            this.breakpointEvents=options.breakpointEvents;
        }

        Long limitTime = 0l;
        if (options != null && options.LimitTime > 0){
            limitTime = options.LimitTime;
        }


       try{
           configGDB(workPath);
       }catch (Exception e){
           return null;
       }

       String[] command = new String[]{"gdb","-x", Paths.get(this.workPath, "init.gdb").toString()};

        this.process = new ProcessBuilder("gdb", "-x", Paths.get(this.workPath, "init.gdb").toString(), execFile);


        Process pro = this.process.start();

        this.stdin = pro.getOutputStream();
        this.stdout = pro.getInputStream();
        this.stderr = pro.getErrorStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(stdin));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
        BufferedReader errReader = new BufferedReader(new InputStreamReader(stderr));

        DebugResult debugResult = new DebugResult();

        boolean timeOut = this.isTimeout(command,limitTime);

        //超时则杀掉进程
        if(timeOut){
            try{
                process.directory();
            }catch (Exception e){}
        }

        // 取消gdb缓存
        writer.println("call setbuf(stdout, NULL)\n");
        readStdout1(limitTime,reader);
        // 设置一些断点
        writer.println("b scanf\n");
        readStdout1(limitTime,reader);
        writer.println("b getchar\n");
        readStdout1(limitTime,reader);

        // 开始运行
        writer.println("r\n");

        String gdbOutput = null;
        Boolean isExcutorTimeout = flushAndReadStdout1(gdbOutput, debugResult.UserOutput,limitTime,reader,writer);
        if (isExcutorTimeout){
            pro.destroy();
            debugResult.IsEnd = true;
        }



        return null;
    }


    /**
     * 输出文件GDB格式，以便查看调试信息
     * 接受文件的异常
     * @param workPath
     * @return Exception
     */
     public void configGDB(String workPath ) throws Exception{
         String fileStr = "set print elements 0\n" +
                 "set print null-stop on\n" +
                 "set print repeats 0\n" +
                 "set print union on\n" +
                 "set width 0\n";
         try{
             Path path= Files.write(Paths.get(workPath,GdbInitFileName),fileStr.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
         }catch (Exception e){
         }
     }



    // readStdout1
    // 读取read中的输出，返回输出内容和是否超时
    // 1. gdb输出， 2. 是否超时
    // 读取标准输出流中的内容，直到遇到 "(gdb)" 结束符或超时
    public String readStdout1( long timeoutMillis,BufferedReader reader) throws TimeoutException, IOException {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        StringBuilder answer = new StringBuilder();

        Future<String> future = executor.submit(() -> {
            String line;
            while ((line = reader.readLine()) != null) {
                answer.append(line);
                if (line.endsWith("(gdb)")) {
                    break;
                }
            }
            return answer.toString();
        });

        try {
            return future.get(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "";
        } finally {
            future.cancel(true);
        }
    }

    public String readStdout2( long timeoutMillis,BufferedReader reader){
        String result = null;
         try {
             result = readStdout1(Long.MAX_VALUE,reader);
         }catch (Exception e){}

         return result;
    }


    /**
     *
     * @param gdbOutput
     * @param userOutput
     * @param timeoutMillis
     * @param reader
     * @param writer
     * @return
     */
    public boolean flushAndReadStdout1(String gdbOutput, String userOutput,long timeoutMillis,BufferedReader reader,PrintWriter writer){

        Boolean timeout1=false;
        Boolean timeout2=false;
        try{
             gdbOutput = readStdout1(timeoutMillis,reader);
        }catch (Exception e){
            timeout1=true;};

       writer.println("call (void)fflush(0)\n");

        try{
            userOutput = readStdout1(timeoutMillis,reader);
        }catch (Exception e){
            timeout2=true;};
        return timeout1|timeout2;
    }



    public DebugResult flushAndReadStdout2( String gdbOutput, String userOutput,long timeoutMillis,BufferedReader reader,PrintWriter writer){
        String gdbout =null;
        String useroutput =null;
        Boolean timeOut = flushAndReadStdout1(gdbout, useroutput,timeoutMillis,reader,writer);
    }

    /**
     * // getBackTrace 读取函数调用栈信息
     * func (d *debugHandlerC) getBackTrace() BackTrace {
     *
     * 	d.stdin.Write([]byte("bt\n"))
     * 	// 读取调用栈信息
     * 	staceData := d.readStdout2()
     * 	stace := strings.Split(staceData, "#")
     * 	stace = stace[1:]
     * 	re := regexp.MustCompile(`\d+\s+(0x[0-9a-f]+)?\s?(\w+)?\s?\((.*?)\) at (\S+):(\d+)`)
     *
     * 	frameList := make([]StackFrame, len(stace))
     * 	// 解析调用栈信息
     * 	for i, frameStr := range stace {
     * 		match := re.FindStringSubmatch(frameStr)
     * 		if match != nil {
     * 			function := match[2]
     * 			args := match[3]
     * 			file := match[4]
     * 			line := match[5]
     *
     * 			// 屏蔽路径
     * 			file = strings.Replace(function, d.workPath, "/", 1)
     *
     * 			stackFrame := StackFrame{
     * 				Function: function,
     * 				Args:     args,
     * 				File:     file,
     * 				Line:     parseInt(line),
     *                        }
     *
     * 			frameList[i] = stackFrame* 		}
     *    }
     * 	return frameList
     * }
     */

    public void getBackTrace(BufferedReader reader,PrintWriter writer){
        writer.println("bt\n");
    }

    /**
     * // parseGdbOutput 解析gdb输出，返回用户程序是否结束
     * func (d *debugHandlerC) parseGdbOutput(gdbOutput string) bool {
     * 	re := regexp.MustCompile(`(.*)(\[Inferior \d+ \(process \d+\) exited normally\])`)
     * 	matches := re.FindStringSubmatch(gdbOutput)
     *
     * 	return len(matches) != 0
     * }
     */
    public Boolean parseGdbOutput(String gdbOutput){

    }



    /**
     *
     * @param command
     * @param LimitTime
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean isTimeout(String[] command,  Long LimitTime) throws IOException,InterruptedException {
        //合并异常项
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        boolean waitFor = process.waitFor(LimitTime, TimeUnit.SECONDS);

        //如果超时进程未退出则设定为超时
        if(!waitFor){
            process.destroyForcibly();
            process.waitFor();
            return true;
        }

        return false;

    }



}
