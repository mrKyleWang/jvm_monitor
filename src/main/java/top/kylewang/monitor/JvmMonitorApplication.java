package top.kylewang.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author KyleWang
 * @version 1.0
 * @date 2019年06月18日
 */
@SpringBootApplication
@EnableScheduling
public class JvmMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(JvmMonitorApplication.class, args);
	}
}
