package top.kylewang.monitor.metrics;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.kylewang.monitor.influxdb.InfluxDbSender;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.HashMap;
import java.util.Map;

/**
 * @author KyleWang
 * @version 1.0
 * @date 2019年06月18日
 */
@Component
public class MemoryMetricsCollector implements MetricsCollector {

	private static final Logger logger = LoggerFactory.getLogger(MemoryMetricsCollector.class);

	@Autowired
	private InfluxDbSender influxDbSender;

	@Scheduled(fixedDelay = 5 * 1000)
	@Override
	public void collectMetrics() {
		Map<String, Long> memoryPoolMetrics = new HashMap<>();
		for (MemoryPoolMXBean mpBean : ManagementFactory.getMemoryPoolMXBeans()) {
			if (mpBean.isValid()) {
				// 内存分区名
				String name = mpBean.getName();
				// 内存占用(单位kb转为m)
				long used = mpBean.getUsage().getUsed() / 1024 / 1024;
				memoryPoolMetrics.put(name, used);
			}
		}
		logger.info(JSONObject.toJSONString(memoryPoolMetrics));
		influxDbSender.send("memoryPoolMetrics", memoryPoolMetrics);
	}
}
