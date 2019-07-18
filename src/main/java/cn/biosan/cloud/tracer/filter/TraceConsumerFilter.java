package cn.biosan.cloud.tracer.filter;

import cn.biosan.cloud.tracer.DubboClientTracer;
import cn.biosan.cloud.tracer.utils.JacksonUtils;
import cn.biosan.cloud.tracer.utils.MdcUtils;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.alipay.common.tracer.core.context.trace.SofaTraceContext;
import com.alipay.common.tracer.core.generator.TraceIdGenerator;
import com.alipay.common.tracer.core.holder.SofaTraceContextHolder;
import com.alipay.common.tracer.core.span.SofaTracerSpan;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 *
 * @author gaozhenyu@biosan.cn
 * @date 2019/3/28 下午3:32
 */
@Activate(group = Constants.CONSUMER, order = -999)
public class TraceConsumerFilter implements Filter {

    private static final Logger            logger = LoggerFactory.getLogger(TraceConsumerFilter.class);
    private              DubboClientTracer dubboClientTracer;

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

//        /** 构建Tracer **/
//        if (this.dubboClientTracer == null) {
//            this.dubboClientTracer = DubboClientTracer.getInstance();
//        }
//        /** client send **/
//        SofaTracerSpan childSpan = dubboClientTracer.clientSend(invocation.getMethodName());
//        MdcUtils.putSpanLog(childSpan.getSofaTracerSpanContext());
//        /** 获取trace上下文 **/
//        SofaTraceContext sofaTraceContext = SofaTraceContextHolder.getSofaTraceContext();
//
//        /**
//         * 透传方式
//         * 1. openTracing inject/extract
//         * 2. dubbo RpcContext
//         */
        try {
            RpcContext context = RpcContext.getContext();
            HashMap attachmentMap = new HashMap();
            attachmentMap.put("biosan", "gaozhenyu@biosan.cn");
            attachmentMap.put("traceId", StringUtils.isEmpty(MdcUtils.getTraceId())?TraceIdGenerator.generate():MdcUtils.getTraceId());
//            attachmentMap.put("spanContext", JacksonUtils.getObjectMapperInstance().writeValueAsString(childSpan.getSofaTracerSpanContext()));
            context.setAttachments(attachmentMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (invocation instanceof RpcInvocation) {
            ((RpcInvocation) invocation).setInvoker(invoker);
        }

        Result result;
        try {
            result = invoker.invoke(invocation);
        } finally {
//            dubboClientTracer.clientReceive("200");
            logger.info("TraceConsumerFilter after invoke");
        }
        return result;
    }

    //    public void processDubboRequestCarrier(Invocation invocation, SofaTracerSpan currentSpan) {
    //        SofaTracer sofaTracer = dubboClientTracer.getSofaTracer();
    //        sofaTracer.inject(currentSpan.getSofaTracerSpanContext(),
    //                ExtendFormat.Builtin.B3_TEXT_MAP, new HttpClientRequestCarrier(invocation));
    //    }
}
