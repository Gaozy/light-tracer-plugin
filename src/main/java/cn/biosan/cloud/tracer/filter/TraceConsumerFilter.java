package cn.biosan.cloud.tracer.filter;

import cn.biosan.cloud.tracer.utils.MdcUtils;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.alipay.common.tracer.core.generator.TraceIdGenerator;
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

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext context = RpcContext.getContext();
        HashMap attachmentMap = new HashMap();
        attachmentMap.put("biosan", "gaozhenyu@biosan.cn");
        attachmentMap.put("traceId", StringUtils.isEmpty(MdcUtils.getTraceId())?TraceIdGenerator.generate():MdcUtils.getTraceId());
        context.setAttachments(attachmentMap);

        if (invocation instanceof RpcInvocation) {
            ((RpcInvocation) invocation).setInvoker(invoker);
        }

        Result result;
        try {
            result = invoker.invoke(invocation);
        } finally {
            logger.info("TraceConsumerFilter after invoke");
        }
        return result;
    }
}
