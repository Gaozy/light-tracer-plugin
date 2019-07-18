package cn.biosan.cloud.tracer.utils;

import java.util.UUID;

/**
 *
 * @author gaozhenyu@biosan.cn
 * @date 2019/7/18 下午2:43
 */
public class TraceIdUtils {

    public static String generate(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
