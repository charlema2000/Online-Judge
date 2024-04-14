package cn.charlema.utils;

import java.util.UUID;

/**
 * 用作项目随机数生成工具类
 */
public class RandomUtil {

    /**
     * 生成一个唯一的uuid
     * @return
     */
    public static UUID GetUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    /**
     *
     */
}
