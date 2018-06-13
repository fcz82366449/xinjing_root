package cn.easy.base.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Calendar;

@Configuration
public class DataSourceConfig {

	@Value("${spring.datasource.driverClassName}")
	private String driverClassName;

	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;

	@Value("${spring.datasource.hikari.maximum-pool-size:100}")
	private int maximumPoolSize;

	@Value("${spring.datasource.hikari.minimum-idle:10}")
	private int minimumIdle;

	@Autowired
	private Environment env;

	@Bean
	public DataSource configureDataSource() {
		HikariConfig config = new HikariConfig();
		config.setIdleTimeout(600000);
		config.setConnectionTimeout(30000);
		config.setValidationTimeout(3000);
		config.setMaxLifetime(1800000);

		config.setPoolName("AARSHikaripool-" + Calendar.getInstance().getTimeInMillis());
		config.setMaximumPoolSize(maximumPoolSize);
		config.setMinimumIdle(minimumIdle);
		config.setDriverClassName(driverClassName);
		config.setJdbcUrl(url);
		config.setUsername(username);
		config.setPassword(password);
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.addDataSourceProperty("useServerPrepStmts", "true");

		String connectionTestQuery = "select 1";
		String dbType = env.getProperty("spring.jpa.database");
		if("oracle".equalsIgnoreCase(dbType)) {
			connectionTestQuery = "select 1 from dual";
		}
		else if("hsqldb".equalsIgnoreCase(dbType)) {
			connectionTestQuery = "select 1 from INFORMATION_SCHEMA.SYSTEM_USERS";
		}
		else if("db2".equalsIgnoreCase(dbType)) {
			connectionTestQuery = "select 1 from sysibm.sysdummy1";
		}
		else if("firebird".equalsIgnoreCase(dbType)) {
			connectionTestQuery = "select 1 from rdb$database";
		}
		config.setConnectionTestQuery(connectionTestQuery);

		return new HikariDataSource(config);
	}

}
