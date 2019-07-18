package cn.biosan.cloud.tracer.utils;

import com.alipay.common.tracer.core.context.span.SofaTracerSpanContext;
import org.slf4j.MDC;

/**
 *
 * @author gaozhenyu@biosan.cn
 * @date 2019/4/8 下午6:06
 */
public class MdcUtils {

    public static void putSpanLog(SofaTracerSpanContext spanContext){
        MDC.put("traceId",spanContext.getTraceId());
        MDC.put("spanId",spanContext.getSpanId());
        MDC.put("parentId",spanContext.getParentId());
    }

    public static void clearSpanLog(){
        MDC.clear();
    }

    public static void putTraceId(String traceId){
        MDC.put("traceId",traceId);
    }

    public static String getTraceId(){
        return MDC.get("traceId");
    }
}
