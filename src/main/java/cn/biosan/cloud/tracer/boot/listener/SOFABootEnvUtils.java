package cn.biosan.cloud.tracer.boot.listener;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;

/**
 *
 * @author gaozhenyu@biosan.cn
 * @date 2019/7/10 下午4:03
 */
public class SOFABootEnvUtils {
    private static final String SPRING_CLOUD_MARK_NAME = "org.springframework.cloud.bootstrap.BootstrapConfiguration";

    public SOFABootEnvUtils() {
    }

    public static boolean isSpringCloudBootstrapEnvironment(Environment environment) {
        if (!(environment instanceof ConfigurableEnvironment)) {
            return false;
        } else {
            return !((ConfigurableEnvironment)environment).getPropertySources().contains("sofaBootstrap") && isSpringCloud();
        }
    }

    public static boolean isSpringCloud() {
        return ClassUtils.isPresent("org.springframework.cloud.bootstrap.BootstrapConfiguration", (ClassLoader)null);
    }
}
