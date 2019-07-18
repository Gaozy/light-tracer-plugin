package cn.biosan.cloud.tracer.filter;

import cn.biosan.cloud.tracer.utils.MdcUtils;
import com.alipay.common.tracer.core.SofaTracer;
import com.alipay.common.tracer.core.configuration.SofaTracerConfiguration;
import com.alipay.common.tracer.core.context.span.SofaTracerSpanContext;
import com.alipay.common.tracer.core.generator.TraceIdGenerator;
import com.alipay.common.tracer.core.registry.ExtendFormat;
import com.alipay.common.tracer.core.span.SofaTracerSpan;
import com.alipay.common.tracer.core.utils.StringUtils;
import com.alipay.sofa.tracer.plugins.springmvc.SpringMvcHeadersCarrier;
import com.alipay.sofa.tracer.plugins.springmvc.SpringMvcTracer;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

/**
 *
 * @author gaozhenyu@biosan.cn
 * @date 2019/3/28 下午3:32
 */
@Component
@WebFilter(urlPatterns = "/*")
public class TraceWebFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        String traceId = TraceIdGenerator.generate();
        MdcUtils.putTraceId(traceId);

        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            // 将Http请求入口作为rootSpan,而非不通过extract方式恢复spanContext
            ResponseWrapper responseWrapper = new ResponseWrapper(response);

            filterChain.doFilter(servletRequest, responseWrapper);

        } catch (Throwable var15) {
            throw new RuntimeException(var15);
        } finally {
            MdcUtils.clearSpanLog();
        }

    }

    @Override
    public void destroy() {
    }


    private boolean isContainSofaTracerMark(HashMap<String, String> headers) {
        return (headers.containsKey("X-B3-TraceId".toLowerCase()) || headers.containsKey("X-B3-TraceId")) && (headers.containsKey("X-B3-SpanId".toLowerCase()) || headers.containsKey("X-B3-SpanId"));
    }

    class ResponseWrapper extends HttpServletResponseWrapper {
        int contentLength = 0;

        public ResponseWrapper(HttpServletResponse httpServletResponse) throws IOException {
            super(httpServletResponse);
        }

        @Override
        public void setContentLength(int len) {
            this.contentLength = len;
            super.setContentLength(len);
        }

        public int getContentLength() {
            return this.contentLength;
        }
    }
}
