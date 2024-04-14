package cn.charlema.service.judger.impl;

import cn.charlema.config.FilePathConfig;
import cn.charlema.service.judger.UtilsService;
import cn.charlema.utils.RandomUtil;

import java.nio.file.Paths;

public class UtilsServiceImpl implements UtilsService {

    FilePathConfig filePathConfig = new FilePathConfig();
    @Override
    public String GetExecutePath() {
        String uuid = RandomUtil.GetUUID().toString();
        String executePath = Paths.get(filePathConfig.TempDir, uuid).toString();
        return executePath;
    }
}
