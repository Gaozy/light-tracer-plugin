package cn.biosan.cloud.tracer;

import com.alipay.common.tracer.core.appender.encoder.SpanEncoder;
import com.alipay.common.tracer.core.configuration.SofaTracerConfiguration;
import com.alipay.common.tracer.core.reporter.stat.AbstractSofaTracerStatisticReporter;
import com.alipay.common.tracer.core.span.SofaTracerSpan;
import com.alipay.common.tracer.core.tracer.AbstractClientTracer;
import com.alipay.sofa.tracer.plugins.springmvc.*;

/**
 *
 * @author gaozhenyu@biosan.cn
 * @date 2019/4/2 上午11:11
 */
public class DubboClientTracer extends AbstractClientTracer {

    private static volatile DubboClientTracer dubboClientTracer = null;

    public static DubboClientTracer getInstance() {
        if (dubboClientTracer == null) {
            synchronized (DubboClientTracer.class) {
                if (dubboClientTracer == null) {
                    dubboClientTracer = new DubboClientTracer();
                }
            }
        }
        return dubboClientTracer;
    }

    public DubboClientTracer() {
        super("dubboClient");
    }

    @Override
    protected String getClientDigestReporterLogName() {
        return DubboLogEnum.DUBBO_DIGEST.getDefaultLogName();
    }

    @Override
    protected String getClientDigestReporterRollingKey() {
        return DubboLogEnum.DUBBO_DIGEST.getRollingKey();
    }

    @Override
    protected String getClientDigestReporterLogNameKey() {
        return DubboLogEnum.DUBBO_DIGEST.getLogNameKey();
    }

    @Override
    protected SpanEncoder<SofaTracerSpan> getClientDigestEncoder() {
        return (SpanEncoder)(Boolean.TRUE.toString().equalsIgnoreCase(SofaTracerConfiguration.getProperty("spring_mvc_json_format_output")) ? new SpringMvcDigestJsonEncoder() : new SpringMvcDigestEncoder());
    }

    @Override
    protected AbstractSofaTracerStatisticReporter generateClientStatReporter() {
        return generateSofaMvcStatReporter();
    }

    private SpringMvcStatReporter generateSofaMvcStatReporter() {
        SpringMvcLogEnum springMvcLogEnum = SpringMvcLogEnum.SPRING_MVC_STAT;
        String statLog = springMvcLogEnum.getDefaultLogName();
        String statRollingPolicy = SofaTracerConfiguration.getRollingPolicy(springMvcLogEnum.getRollingKey());
        String statLogReserveConfig = SofaTracerConfiguration.getLogReserveConfig(springMvcLogEnum.getLogNameKey());
        return (SpringMvcStatReporter)(Boolean.TRUE.toString().equalsIgnoreCase(SofaTracerConfiguration.getProperty("spring_mvc_json_format_output")) ? new SpringMvcJsonStatReporter(statLog, statRollingPolicy, statLogReserveConfig) : new SpringMvcStatReporter(statLog, statRollingPolicy, statLogReserveConfig));
    }
}
