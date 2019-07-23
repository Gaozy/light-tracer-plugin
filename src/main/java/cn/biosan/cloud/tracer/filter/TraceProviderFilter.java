package cn.biosan.cloud.tracer.filter;

import cn.biosan.cloud.tracer.utils.MdcUtils;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gaozhenyu@biosan.cn
 * @date 2019/3/28 下午3:32
 */
@Activate(group = Constants.PROVIDER,order = -999)
public class TraceProviderFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(TraceProviderFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext context = RpcContext.getContext();
        MdcUtils.putTraceId(context.getAttachment("traceId"));
        if(invocation instanceof RpcInvocation){
            ((RpcInvocation) invocation).setInvoker(invoker);
        }
        Result result;
        try{
            result = invoker.invoke(invocation);
        } finally {
//            logger.info("TraceProviderFilter after invoke");
        }
        return result;
    }
}
