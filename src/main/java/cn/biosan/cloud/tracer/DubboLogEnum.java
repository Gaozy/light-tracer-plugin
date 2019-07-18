package cn.biosan.cloud.tracer;

/**
 *
 * @author gaozhenyu@biosan.cn
 * @date 2019/4/2 下午2:29
 */
public enum  DubboLogEnum {

    // biosan dubbo log
    DUBBO_DIGEST("gaozy-dubbo_digest_log_name", "gaozy-dubbo-digest.log", "gaozy-dubbo-digest_rolling"),
    DUBBO_STAT("dubbo_stat_log_name", "dubbo-stat.log", "dubbo_stat_rolling");

    private String logNameKey;
    private String defaultLogName;
    private String rollingKey;

    DubboLogEnum(String logNameKey, String defaultLogName, String rollingKey) {
        this.logNameKey = logNameKey;
        this.defaultLogName = defaultLogName;
        this.rollingKey = rollingKey;
    }

    public String getLogNameKey() {
        return logNameKey;
    }

    public String getDefaultLogName() {
        return defaultLogName;
    }

    public String getRollingKey() {
        return rollingKey;
    }
}
