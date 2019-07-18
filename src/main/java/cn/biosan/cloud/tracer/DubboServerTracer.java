package cn.biosan.cloud.tracer;

import com.alipay.common.tracer.core.appender.encoder.SpanEncoder;
import com.alipay.common.tracer.core.configuration.SofaTracerConfiguration;
import com.alipay.common.tracer.core.reporter.stat.AbstractSofaTracerStatisticReporter;
import com.alipay.common.tracer.core.span.SofaTracerSpan;
import com.alipay.common.tracer.core.tracer.AbstractServerTracer;
import com.alipay.sofa.tracer.plugins.springmvc.*;

/**
 *
 * @author gaozhenyu@biosan.cn
 * @date 2019/4/4 下午4:55
 */
public class DubboServerTracer extends AbstractServerTracer {

    private static volatile DubboServerTracer dubboServerTracer = null;

    public static DubboServerTracer getInstance() {
        if (dubboServerTracer == null) {
            synchronized (DubboServerTracer.class) {
                if (dubboServerTracer == null) {
                    dubboServerTracer = new DubboServerTracer();
                }
            }
        }
        return dubboServerTracer;
    }

    public DubboServerTracer() {
        super("dubboServer");
    }

    @Override
    protected String getServerDigestReporterLogName() {
        return DubboLogEnum.DUBBO_DIGEST.getDefaultLogName();
    }

    @Override
    protected String getServerDigestReporterRollingKey() {
        return DubboLogEnum.DUBBO_DIGEST.getRollingKey();
    }

    @Override
    protected String getServerDigestReporterLogNameKey() {
        return DubboLogEnum.DUBBO_DIGEST.getLogNameKey();
    }

    @Override
    protected SpanEncoder<SofaTracerSpan> getServerDigestEncoder() {
        return (SpanEncoder)(Boolean.TRUE.toString().equalsIgnoreCase(SofaTracerConfiguration.getProperty("spring_mvc_json_format_output")) ? new SpringMvcDigestJsonEncoder() : new SpringMvcDigestEncoder());
    }

    @Override
    protected AbstractSofaTracerStatisticReporter generateServerStatReporter() {
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
