package cn.biosan.cloud.tracer;

import io.opentracing.propagation.TextMap;

import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author gaozhenyu@biosan.cn
 * @date 2019/4/2 下午5:11
 */
public class DubboRequestCarrier implements TextMap {

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        // no operation
        throw new UnsupportedOperationException();
    }

    @Override
    public void put(String key, String value) {

    }
}
