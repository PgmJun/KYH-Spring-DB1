package hello.jdbc.connection;

import static hello.jdbc.connection.ConnectionConst.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConnectionUtil {
	public static Connection getConnection() {
		try {

			DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
			Connection connection = dataSource.getConnection();

			log.info("get connection={}, class", connection, connection.getClass());
			return connection;
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}
}
