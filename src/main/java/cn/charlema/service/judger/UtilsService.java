package cn.charlema.service.judger;


import cn.charlema.config.FilePathConfig;
import org.springframework.stereotype.Component;

/**
 * 为其他服务提供的工具服务
 */
@Component
public interface UtilsService {

    /**
     * 为执行文件生成一个临时目录
     * @return
     */
    String GetExecutePath();

}
