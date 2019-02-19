package fr.travel.demo;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


@SpringBootApplication
public class DemoApplication {

	
	
	public static void main(String[] args) {
		
		
		  System.exit(SpringApplication.exit(SpringApplication.run(DemoApplication.class, args)));
	}
	@Resource
	Environment env;

	@Bean("dataSourceSqlServer")
	public DataSource dataSource( @Value("${req}") String req) throws SQLException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("datasource.sqlserver.driver-class-name"));
		dataSource.setUrl(env.getProperty("datasource.sqlserver.url"));
		dataSource.setUsername(env.getProperty("datasource.sqlserver.username"));
		dataSource.setPassword(env.getProperty("datasource.sqlserver.password"));
		
		Statement stmt = dataSource.getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery(req);
		
		// collect column names
		List<String> columnNames = new ArrayList<>();
		ResultSetMetaData rsmd = rs.getMetaData();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
		    columnNames.add(rsmd.getColumnLabel(i));
		}

		int rowIndex = 0;
		while (rs.next()) {
		    rowIndex++;
		    // collect row data as objects in a List
		    List<Object> rowData = new ArrayList<>();
		    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
		        rowData.add(rs.getObject(i));
		    }
		    // for test purposes, dump contents to check our results
		    // (the real code would pass the "rowData" List to some other routine)
		    System.out.printf("Row %d%n", rowIndex);
		    for (int colIndex = 0; colIndex < rsmd.getColumnCount(); colIndex++) {
		        String objType = "null";
		        String objString = "";
		        Object columnObject = rowData.get(colIndex);
		        if (columnObject != null) {
		            objString = columnObject.toString() + " ";
		            objType = columnObject.getClass().getName();
		        }
		        System.out.printf("  %s: %s(%s)%n",
		                columnNames.get(colIndex), objString, objType);
		    }
		}
		return dataSource;
	}
	

	
}
