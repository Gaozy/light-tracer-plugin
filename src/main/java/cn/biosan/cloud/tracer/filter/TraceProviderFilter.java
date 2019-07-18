package cn.biosan.cloud.tracer.filter;

import cn.biosan.cloud.tracer.DubboServerTracer;
import cn.biosan.cloud.tracer.utils.JacksonUtils;
import cn.biosan.cloud.tracer.utils.MdcUtils;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.alipay.common.tracer.core.context.span.SofaTracerSpanContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 *
 * @author gaozhenyu@biosan.cn
 * @date 2019/3/28 下午3:32
 */
@Activate(group = Constants.PROVIDER,order = -999)
public class TraceProviderFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(TraceProviderFilter.class);

    private DubboServerTracer dubboServerTracer;

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
//            if(null == dubboServerTracer){
//                dubboServerTracer = DubboServerTracer.getInstance();
//            }
//            RpcContext context = RpcContext.getContext();
//            String spanContextAttachment = context.getAttachment("spanContext");
//            if(!StringUtils.isEmpty(spanContextAttachment)){
//                SofaTracerSpanContext spanContext = JacksonUtils.getObjectMapperInstance().readValue(spanContextAttachment, SofaTracerSpanContext.class);
//                /** sr **/
//                dubboServerTracer.serverReceive(spanContext);
//                MdcUtils.putSpanLog(spanContext);
//            }

            RpcContext context = RpcContext.getContext();
            MdcUtils.putTraceId(context.getAttachment("traceId"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(invocation instanceof RpcInvocation){
            ((RpcInvocation) invocation).setInvoker(invoker);
        }
        Result result;
        try{
            result = invoker.invoke(invocation);
        } finally {
//            // 消费方未引入时，不会进行sr，ss会直接handle return
//            dubboServerTracer.serverSend("200");
            logger.info("TraceProviderFilter after invoke");
        }
        return result;
    }
}
