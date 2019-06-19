package top.kylewang.monitor.influxdb;


import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author KyleWang
 * @version 1.0
 * @date 2019年06月18日
 */
@Configuration
public class InfluxDbConfig {

    @Value("${influxDB.serverAddr}")
    private String serverAddr;
    @Value("${influxDB.username}")
    private String username;
    @Value("${influxDB.password}")
    private String password;
    @Value("${influxDB.dataBase}")
    private String dataBase;
    @Value("${influxDB.retentionPolicyName}")
    private String retentionPolicyName;
    @Value("${influxDB.retentionPolicy}")
    private String retentionPolicy;

    @Bean
    public InfluxDB influxDB() {
        // 连接influxDB
        InfluxDB influxDB = InfluxDBFactory.connect(serverAddr, username, password);
        // 创建db
        influxDB.query(new Query("CREATE DATABASE " + dataBase));
        influxDB.setDatabase(dataBase);
        // 创建retentionPolicy
        influxDB.query(new Query("CREATE RETENTION POLICY \"" + retentionPolicyName + "\" ON \"" + dataBase
                + "\" DURATION " + retentionPolicy + " REPLICATION 1 DEFAULT"));
        influxDB.setRetentionPolicy(retentionPolicyName);
        return influxDB;
    }
}
