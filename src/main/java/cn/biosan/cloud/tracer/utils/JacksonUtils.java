package cn.biosan.cloud.tracer.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author gaozhenyu@biosan.cn
 * @date 2019/4/3 下午4:20
 */
public class JacksonUtils {

    public static volatile ObjectMapper objectMapper = null;

    public static ObjectMapper getObjectMapperInstance(){

        if(null == objectMapper){
            synchronized (ObjectMapper.class){
                if(null == objectMapper){
                    objectMapper = new ObjectMapper();
                }
            }
        }
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        return objectMapper;
    }
}
