package com.van.testService.config;

import com.van.processor.jdbc.JdbcOperations;
import com.van.processor.jdbc.JdbcOperationsService;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static com.van.processor.common.FileUtils.getFileFromResources;
import static com.van.processor.common.FileUtils.stringFomFile;

@Configuration
public class DatabaseConfig {

	@Bean(name="customDataSource")
	@ConfigurationProperties("spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public JdbcOperations getJdbcOperationsService() {
		return new JdbcOperationsService(dataSource());
	}

	@Bean
	public CommandLineRunner demo() {
		return (args) -> {
			System.out.println("reCreateSchema  ----- start ");
			reCreateSchema();
			System.out.println("reCreateSchema  ----- end");
		};
	}

	public void reCreateSchema() throws SQLException, IOException {
		try (Connection con = dataSource().getConnection(); Statement stmt = con.createStatement();) {
			stmt.executeUpdate(stringFomFile(getFileFromResources("schema.sql")));
			stmt.executeUpdate(stringFomFile(getFileFromResources("data.sql")));
		}
	}
}
