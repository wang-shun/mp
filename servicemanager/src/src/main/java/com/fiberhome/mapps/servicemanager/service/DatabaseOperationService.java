package com.fiberhome.mapps.servicemanager.service;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fiberhome.mapps.servicemanager.exception.DBTestFailException;
import com.fiberhome.mapps.servicemanager.exception.DBUserExistException;
import com.mchange.v2.c3p0.ComboPooledDataSource;

@Component

public class DatabaseOperationService {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

	public DataSource createDataSource(String driver, String url, String userName, String password) {
		try {
			// 创建ComboPooledDataSource
			ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
			// 设置相应参数
			comboPooledDataSource.setDriverClass(driver);
			comboPooledDataSource.setJdbcUrl(url);
			comboPooledDataSource.setUser(userName);
			comboPooledDataSource.setPassword(password);
			// 设置最小连接个数
			comboPooledDataSource.setMinPoolSize(5);
			// 设置最大连接个数
			comboPooledDataSource.setMaxPoolSize(50);
			// 设置最大空闲时间
			comboPooledDataSource.setMaxIdleTime(5000);
			// 返回数据源对象
			return comboPooledDataSource;
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void createOracle(String driver, String conUrl, String adminUser, String adminPass, String userName,
			String password) throws Exception {
		Connection con = null;
		try {
			Class.forName(driver).newInstance(); // 加载数据库驱动
			con = DriverManager.getConnection(conUrl, adminUser.toUpperCase(), adminPass);
			con.setAutoCommit(false);
			Statement stmt = con.createStatement();
			String sql = "select USER_ID from dba_users where username = '" + userName.toUpperCase() + "'";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				throw new DBUserExistException();
			}
			stmt.execute("CREATE USER " + userName.toUpperCase() + " IDENTIFIED BY \"" + password
					+ "\" DEFAULT TABLESPACE USERS TEMPORARY TABLESPACE TEMP QUOTA UNLIMITED ON USERS");
			stmt.execute("GRANT \"CONNECT\" TO " + userName.toUpperCase());
			stmt.execute("GRANT \"RESOURCE\" TO " + userName.toUpperCase());
			stmt.execute("ALTER USER " + userName.toUpperCase() + " DEFAULT ROLE \"CONNECT\",\"RESOURCE\"");
			stmt.execute("GRANT CREATE  TABLE              TO " + userName.toUpperCase());
			stmt.execute("GRANT CREATE  VIEW               TO " + userName.toUpperCase());
			stmt.execute("GRANT CREATE  MATERIALIZED VIEW  TO " + userName.toUpperCase());
			stmt.execute("GRANT DEBUG CONNECT SESSION      TO " + userName.toUpperCase());
			stmt.execute("GRANT CREATE SYNONYM             TO " + userName.toUpperCase());
			stmt.execute("GRANT CREATE JOB                 TO " + userName.toUpperCase());
			stmt.execute("GRANT ALTER SESSION              TO " + userName.toUpperCase());
			stmt.execute("GRANT FORCE TRANSACTION          TO " + userName.toUpperCase());
			stmt.execute("GRANT ON COMMIT REFRESH          TO " + userName.toUpperCase());
			stmt.execute("GRANT QUERY REWRITE              TO " + userName.toUpperCase());
			stmt.execute("GRANT ANALYZE ANY                TO " + userName.toUpperCase());
			stmt.execute("GRANT SELECT ANY DICTIONARY      TO " + userName.toUpperCase());
			con.commit();
		} catch (Exception ex) {
			LOGGER.error("===建库失败===", ex);
			try {
				con.rollback();
			} catch (Exception e) {
				e.printStackTrace();
			}
			throw ex;
		} finally {
			try {
				if(con != null){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}
	}
	
	public void testPostgresql(String driver, String conUrl, String userName,String password) throws DBTestFailException{
		Connection con = null;
		try {
			Class.forName(driver).newInstance(); // 加载数据库驱动
			con = DriverManager.getConnection(conUrl, userName, password);
			}
		catch(SQLException sqle){
			throw new DBTestFailException();
		}catch(Exception ex){
			LOGGER.error("===测试失败===", ex);
			throw new DBTestFailException();
		}finally {
			try {
				if(con != null){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DBTestFailException();
			}
		}
	}

	public void testOracle(String driver, String conUrl,  String userName,String password) throws DBTestFailException {
			Connection con = null;
			try {
				Class.forName(driver).newInstance(); // 加载数据库驱动
				con = DriverManager.getConnection(conUrl, userName.toUpperCase(), password);
			}catch(Exception ex){
				LOGGER.error("===测试失败===", ex);
				throw new DBTestFailException();
			}finally {
				try {
					if(con != null){
						con.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
					throw new DBTestFailException();
				}
			}
		}
	
	public void createPostgresql(String driver, String conUrl, String adminUser, String adminPass, String userName,
			String password,String dbName) throws Exception {
		Connection con = null;
		int iscreatedb = 0;
		try {
			Class.forName(driver).newInstance(); // 加载数据库驱动
			con = DriverManager.getConnection(conUrl, adminUser, adminPass);
			//con.setAutoCommit(false);
			Statement stmt = con.createStatement();
			String sql = "SELECT u.datname  FROM pg_catalog.pg_database u where u.datname='" + userName
					+ "'";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				throw new DBUserExistException();
			}
			stmt.execute("CREATE user " + userName + " with PASSWORD '" + password
					+ "'  SUPERUSER CREATEDB CREATEROLE   VALID UNTIL 'infinity'");
			stmt.execute("CREATE DATABASE " + dbName + "  WITH OWNER = " + userName
					+ "       ENCODING = 'UTF8'       TEMPLATE=TEMPLATE0");
			stmt.execute("GRANT CONNECT, TEMPORARY ON DATABASE " + dbName + " TO public");
			stmt.execute("GRANT ALL ON DATABASE " + dbName + " TO postgres");
			stmt.execute("GRANT ALL ON DATABASE " + dbName + " TO " + userName + " WITH GRANT OPTION");
			iscreatedb = 1;
			//con.commit();
		} catch (Exception ex) {
			LOGGER.error("===建库失败===", ex);
			try {
				//con.rollback();
				if(iscreatedb == 1){
					Statement stmt = con.createStatement();
					stmt.execute("drop database " + dbName);
					stmt.execute("drop user " + userName);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			throw ex;
		} finally {
			try {
				if(con != null){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

	public void testMysql(String driver, String conUrl,  String userName,String password) throws DBTestFailException {
		Connection con = null;
		try {
			Class.forName(driver).newInstance(); // 加载数据库驱动
			con = DriverManager.getConnection(conUrl, userName, password);
		}catch(Exception ex){
			LOGGER.error("===测试失败===", ex);
			throw new DBTestFailException();
		}finally {
			try {
				if(con != null){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DBTestFailException();
			}
		}
	}
	
	public void createMysql(String driver, String conUrl, String adminUser, String adminPass, String userName,
			String password,String dbName) throws Exception {
		Connection con = null;
		int iscreatedb = 0;
		try {
			Class.forName(driver).newInstance(); // 加载数据库驱动
			con = DriverManager.getConnection(conUrl, adminUser, adminPass);
			//con.setAutoCommit(false);
			Statement stmt = con.createStatement();
			stmt.execute("use mysql;");
			String sql = "select host,user,password from user where user = '" + userName
					+ "';";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				throw new DBUserExistException();
			}
			stmt.execute("CREATE USER '"+userName+"'@'%' IDENTIFIED BY '"+password+"';");
			stmt.execute("CREATE DATABASE "+dbName+";");
			iscreatedb = 1;
			stmt.execute("GRANT ALL ON "+dbName+".* TO '"+userName+"';");
			stmt.execute("flush privileges;");
			//con.commit();
		} catch (Exception ex) {
			LOGGER.error("===建库失败===", ex);
			try {
				//con.rollback();
				if(iscreatedb == 1){
					Statement stmt = con.createStatement();
					stmt.execute("drop database " + dbName);
					stmt.execute("drop user " + userName);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			throw ex;
		} finally {
			try {
				if(con != null){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

}
