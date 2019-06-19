package top.kylewang.monitor.influxdb;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.kylewang.monitor.util.ServerUtil;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author KyleWang
 * @version 1.0
 * @date 2019年06月18日
 */
@Component
public class InfluxDbSender {

	private static final Logger logger = LoggerFactory.getLogger(InfluxDbSender.class);

	/** 本机ip */
	private String host;
	@Value("${server.port}")
	private int port;
	@Autowired
	private InfluxDB influxDB;

	@PostConstruct
	public void init() {
		try {
			// 获取本机ip
			host = ServerUtil.getIp();
		} catch (Exception e) {
			logger.error("get host&port error", e);
		}
	}

	public void send(String measurement, Map<String, Long> metrics) {
		// 打上ip+端口的tag ，用于分类查询
		Point.Builder builder = Point.measurement(measurement).time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
				.tag("host", host).tag("port", port + "").tag("addr", host + ":" + port);
		for (Map.Entry<String, Long> entry : metrics.entrySet()) {
			builder.addField(entry.getKey(), entry.getValue());
		}
		influxDB.write(builder.build());
	}
}
