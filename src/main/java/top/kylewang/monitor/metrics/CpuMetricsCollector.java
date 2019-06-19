package top.kylewang.monitor.metrics;

import com.sun.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.kylewang.monitor.influxdb.InfluxDbSender;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * @author KyleWang
 * @version 1.0
 * @date 2019年06月18日
 */
@Component
public class CpuMetricsCollector implements MetricsCollector {

	private static final Logger logger = LoggerFactory.getLogger(CpuMetricsCollector.class);

	@Autowired
	private InfluxDbSender influxDbSender;

	@Scheduled(fixedDelay = 5 * 1000)
	@Override
	public void collectMetrics() {
		Map<String, Long> cpuMetrics = new HashMap<>();
		OperatingSystemMXBean bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		// 获取系统cpu占用及进程cpu占用，分数转百分比
		long systemCpuRatio = Math.round(bean.getSystemCpuLoad() * 100);
		long processCpuRatio = Math.round(bean.getProcessCpuLoad() * 100);
		cpuMetrics.put("systemCpuRatio", systemCpuRatio);
		cpuMetrics.put("processCpuRatio", processCpuRatio);
		logger.info("systemCpuRatio={},processCpuRatio{}", systemCpuRatio, processCpuRatio);
		influxDbSender.send("cpuMetrics", cpuMetrics);
	}
}
