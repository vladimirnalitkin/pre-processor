package com.van.processor.jdbc.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.van.processor.jdbc.prop.DataBaseProp;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static com.van.processor.common.FileUtils.getFileFromResources;
import static com.van.processor.common.FileUtils.stringFomFile;

public class TestUtils {
	public final Logger log = LoggerFactory.getLogger(getClass());

	private DataBaseProp dataBaseProp;
	private static HikariConfig config = new HikariConfig();
	private static HikariDataSource ds;

	public void init() {
		loadProp();
		dbInit();
	}

	private void loadProp() {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		try {
			dataBaseProp = mapper.readValue(getFileFromResources("application-test.yml"), DataBaseProp.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assert dataBaseProp != null : "Can't read database properties!";
	}

	private void dbInit() {
		config.setDriverClassName(dataBaseProp.getDatasource().getDriverclassname());
		config.setJdbcUrl(dataBaseProp.getDatasource().getUrl());
		config.setUsername(dataBaseProp.getDatasource().getUsername());
		config.setPassword(dataBaseProp.getDatasource().getPassword());
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		ds = new HikariDataSource(config);
	}

	public void reCreateSchema(DataSource dataSource) throws SQLException {
		if ("always".equals(dataBaseProp.getDatasource().getInitializationmode())) {
			try (Connection con = dataSource.getConnection(); Statement stmt = con.createStatement();) {
				dataBaseProp.getDatasource().getInitsql().forEach(file -> {
					try {
						log.debug("Exec : " + file);
						stmt.executeUpdate(stringFomFile(getFileFromResources(file)));
					} catch (SQLException | IOException e) {
						log.error(e.getLocalizedMessage());
					}
				});
			}
		}
	}

	public DataSource getDataSource() {
		return ds;
	}

}
