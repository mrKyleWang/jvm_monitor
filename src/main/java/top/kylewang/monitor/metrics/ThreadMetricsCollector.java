package top.kylewang.monitor.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.kylewang.monitor.influxdb.InfluxDbSender;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

/**
 * @author KyleWang
 * @version 1.0
 * @date 2019年06月18日
 */
@Component
public class ThreadMetricsCollector implements MetricsCollector {

	private static final Logger logger = LoggerFactory.getLogger(MemoryMetricsCollector.class);

	@Autowired
	private InfluxDbSender influxDbSender;

	@Scheduled(fixedDelay = 5 * 1000)
	@Override
	public void collectMetrics() {
		Map<String, Long> threadMetrics = new HashMap<>();
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		ThreadInfo[] threadInfos = bean.dumpAllThreads(false, false);
		long threadCount = bean.getThreadCount();
		long runnableThreadCount = 0;
		long waitingThreadCount = 0;
		long timedWaitingThreadCount = 0;
		for (ThreadInfo threadInfo : threadInfos) {
			Thread.State state = threadInfo.getThreadState();
			if (state.equals(Thread.State.RUNNABLE)) {
				runnableThreadCount++;
			}
			if (state.equals(Thread.State.WAITING)) {
				waitingThreadCount++;
			}
			if (state.equals(Thread.State.TIMED_WAITING)) {
				timedWaitingThreadCount++;
			}
		}
		threadMetrics.put("threadCount", threadCount);
		threadMetrics.put("runnableThreadCount", runnableThreadCount);
		threadMetrics.put("waitingThreadCount", waitingThreadCount);
		threadMetrics.put("timedWaitingThreadCount", timedWaitingThreadCount);
		logger.info("threadCount={},runnableThreadCount={},waitingThreadCount={},timedWaitingThreadCount={}",
				threadCount, runnableThreadCount, waitingThreadCount, timedWaitingThreadCount);
		influxDbSender.send("threadMetrics", threadMetrics);
	}
}
