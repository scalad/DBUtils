##Apache DBUtils封装使用
Apache DBUtils是个小巧的JDBC轻量级封装的工具包，其最核心的特性是结果集的封装，可以直接将查询出来的结果集封装成JavaBean，这就为我们做了最枯燥乏味、最容易出错的一大部分工作。<br><br>
大部分开发人员在生成环境中更多的是依靠Hibernate、Ibatis、Spring JDBC、JPA等大厂提供的持久层技术解决方案，或者是企业内部自己研发的持久层技术。但无论如何，使用这些
技术的初衷和本质都是为了能够减少企业开发成本，提高生产效率，降低耦合。<br><br>
放眼企业级项目，Hibernate等ORM产品是首选，而互联网领域，大部分开发人员往往并不会在生产环境中上这些ORM技术，原因很简单，要的就是效率，其次都不重要。对于刚接触SQL和JDBC的开发人员，最引以为傲的就是希望能够在日后编写复杂的SQL语句，以及会使用诸如Hibernate、Ibatis等第三方持久层技术，并且极力的撇清与传统JDBC技术的关系，但笔者不得不认为,这是一种普遍业界存在的“病态”！<br><br>
如果是企业级的项目，尤其是跟金融相关的业务，SQL语句或许会非常复杂，并且关联着事物。但互联网项目却并非如此，在互联网项目中，看你牛不牛逼并不是取决于你能否写出一条复杂的SQL语句，而是看你能否将原本一条复杂的SQL语句拆散成单条SQL，一句一句的执行；并且脱离Hibernate等ORM产品后，能否使用传统的JDBC技术完成一条简单的CRUD操作，这才是牛逼！是的，你没有听错，互联网确确实实就是这么玩，还原最本质的东西，才是追求性能的不二选择<br><br>
####在使用DBUtils之前首先需要获取Connection或者DataBase连接
	package com.silence.utils;

	import java.beans.PropertyVetoException;
	import java.io.IOException;
	import java.io.InputStream;
	import java.sql.Connection;
	import java.sql.SQLException;
	import java.util.Properties;
	
	import com.mchange.v2.c3p0.ComboPooledDataSource;
	
	public class ConnectionFactory {

	private static String driver;
	private static String dburl;
	private static String user;
	private static String password;
	public static ComboPooledDataSource dataSource;

	private static final ConnectionFactory factory = new ConnectionFactory();

	private Connection conn = null;

	// 初始化数据库连接池
	static {
		Properties prop = new Properties();
		try {
			InputStream inputStream = ConnectionFactory.class.
			getClassLoader().getResourceAsStream("db.properties");
			prop.load(inputStream);
			driver = prop.getProperty("driver");
			dburl = prop.getProperty("dburl");
			user = prop.getProperty("user");
			password = prop.getProperty("password");
			dataSource = new ComboPooledDataSource();
			dataSource.setUser(user);
			dataSource.setPassword(password);
			dataSource.setJdbcUrl(dburl);
			dataSource.setDriverClass(driver);
			dataSource.setInitialPoolSize(10);
			dataSource.setMinPoolSize(5);
			dataSource.setMaxPoolSize(50);
			dataSource.setMaxStatements(100);
			dataSource.setMaxIdleTime(60);
		} catch (IOException e) {
			System.out.println("the file of datasouce configuration is not exist!");
			e.printStackTrace();
		} catch (PropertyVetoException e) {
			System.out.println("datasouce config error!");
			e.printStackTrace();
		}
		System.out.println("datasouce init finish........!");
	}

	private ConnectionFactory() {
	}

	public static ConnectionFactory getInstance() {
		return factory;
	}

	public static ComboPooledDataSource getDataSouce(){
		return dataSource;
	}
	//获取连接
	public Connection getConnection() {
		if (null != dataSource) {
			try {
				conn = dataSource.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
	}

该文件从db.properties中读取数据库的配置文件并使用C3P0创建数据库连接池，该配置文件如下：
	
	driver=com.mysql.jdbc.Driver
	dburl=jdbc:mysql://localhost:3306/jdbc
	user=root
	password=root
	
然后我们就可以使用DBUtils来操作数据库了，下面是使用DBUtils再次封装的工具代码，使用该代码可以使你更快的操纵数据库。

	import java.sql.Connection;
	import java.util.List;
	import java.util.Map;
	
	import org.apache.commons.dbutils.QueryRunner;
	import org.apache.commons.dbutils.handlers.ArrayHandler;
	import org.apache.commons.dbutils.handlers.ArrayListHandler;
	import org.apache.commons.dbutils.handlers.BeanHandler;
	import org.apache.commons.dbutils.handlers.BeanListHandler;
	import org.apache.commons.dbutils.handlers.MapHandler;
	import org.apache.commons.dbutils.handlers.MapListHandler;
	import org.apache.commons.dbutils.handlers.ScalarHandler;
	
	public class DataBaseUtils {
	
	public static QueryRunner getQueryRunner() {
		return new QueryRunner(ConnectionFactory.getDataSouce());
	}

	/**
	 * 批量操作，包括批量保存、修改、删除
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int[] batch(Connection connection, String sql, Object[][] params) {
		try {
			return new QueryRunner().batch(connection, sql, params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 批量操作，包括批量保存、修改、删除
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int[] batch(String sql, Object[][] params) {
		try {
			return getQueryRunner().batch(sql, params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 删除操作
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int delete(String sql, Object... params) {
		try {
			return getQueryRunner().update(sql, params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	/**
	 * 删除操作
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int delete(Connection connection, String sql, Object... params) {
		try {
			return new QueryRunner().update(connection, sql, params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	/**
	 * 更新操作
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int update(Connection connection, String sql, Object... params) {
		try {
			new QueryRunner().update(connection, sql, params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	/**
	 * 更新操作
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int update(String sql, Object... params) {
		try {
			return getQueryRunner().update(sql, params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	/**
	 * 保存操作
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int save(Connection connection, String sql, Object... params) {
		try {
			return new QueryRunner().update(connection, sql, params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	/**
	 * 保存操作
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int save(String sql, Object... params) {
		try {
			return getQueryRunner().update(sql, params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	/**
	 * 根据sql查询list对象
	 * 
	 * @param <T>
	 * 
	 * @param sql
	 * @param type
	 * @param params
	 * @return
	 */
	public static <T> List<T> getListBean(Connection connection, String sql, Class<T> type, Object... params) {
		try {
			return new QueryRunner().query(connection, sql, new BeanListHandler<T>(type), params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据sql查询list对象
	 * 
	 * @param <T>
	 * 
	 * @param sql
	 * @param type
	 * @param params
	 * @return
	 */
	public static <T> List<T> getListBean(String sql, Class<T> type, Object... params) {
		try {
			return getQueryRunner().query(sql, new BeanListHandler<T>(type), params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据sql查询list对象
	 * 
	 * @param <T>
	 * 
	 * @param sql
	 * @param type
	 * @return
	 */
	public static <T> List<T> getListBean(Connection connection, String sql, Class<T> type) {
		try {
			return new QueryRunner().query(connection, sql, new BeanListHandler<T>(type));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据sql查询list对象
	 * 
	 * @param <T>
	 * 
	 * @param sql
	 * @param type
	 * @return
	 */
	public static <T> List<T> getListBean(String sql, Class<T> type) {
		try {
			// BeanListHandler 将ResultSet转换为List<JavaBean>的ResultSetHandler实现类
			return getQueryRunner().query(sql, new BeanListHandler<T>(type));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据sql和对象，查询结果并以对象形式返回
	 * 
	 * @param <T>
	 * 
	 * @param sql
	 * @param type
	 * @param params
	 * @return
	 */
	public static <T> T getBean(Connection connection, String sql, Class<T> type, Object... params) {
		try {
			return new QueryRunner().query(connection, sql, new BeanHandler<T>(type), params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据sql和对象，查询结果并以对象形式返回
	 * 
	 * @param <T>
	 * 
	 * @param sql
	 * @param type
	 * @param params
	 * @return
	 */
	public static <T> T getBean(String sql, Class<T> type, Object... params) {
		try {
			return getQueryRunner().query(sql, new BeanHandler<T>(type), params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据sql和对象，查询结果并以对象形式返回
	 * 
	 * @param <T>
	 * 
	 * @param sql
	 * @param type
	 * @return
	 */
	public static <T> T getBean(Connection connection, String sql, Class<T> type) {
		try {
			return new QueryRunner().query(connection, sql, new BeanHandler<T>(type));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据sql和对象，查询结果并以对象形式返回
	 * 
	 * @param <T>
	 * 
	 * @param sql
	 * @param type
	 * @return
	 */
	public static <T> T getBean(String sql, Class<T> type) {
		try {
			// BeanHandler 将ResultSet行转换为一个JavaBean的ResultSetHandler实现类
			return getQueryRunner().query(sql, new BeanHandler<T>(type));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据传入的sql查询所有记录，以List Map形式返回
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static List<Map<String, Object>> getListMap(Connection connection, String sql, Object... params) {
		try {
			return new QueryRunner().query(connection, sql, new MapListHandler(), params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据传入的sql查询所有记录，以List Map形式返回
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static List<Map<String, Object>> getListMap(String sql, Object... params) {
		try {
			return getQueryRunner().query(sql, new MapListHandler(), params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据传入的sql查询所有记录，以List Map形式返回
	 * 
	 * @param sql
	 * @return
	 */
	public static List<Map<String, Object>> getListMap(Connection connection, String sql) {
		try {
			return new QueryRunner().query(connection, sql, new MapListHandler());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据传入的sql查询所有记录，以List Map形式返回
	 * 
	 * @param sql
	 * @return
	 */
	public static List<Map<String, Object>> getListMap(String sql) {
		try {
			// MapListHandler 将ResultSet转换为List<Map>的ResultSetHandler实现类
			return getQueryRunner().query(sql, new MapListHandler());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据传入的sql，查询记录，以Map形式返回第一行记录。 注意：如果有多行记录，只会返回第一行，所以适用场景需要注意，可以使用根据主键来查询的场景
	 * 
	 * @param <T>
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static Map<String, Object> getFirstRowMap(Connection connection, String sql, Object... params) {
		try {
			return new QueryRunner().query(connection, sql, new MapHandler(), params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据传入的sql，查询记录，以Map形式返回第一行记录。 注意：如果有多行记录，只会返回第一行，所以适用场景需要注意，可以使用根据主键来查询的场景
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static Map<String, Object> getFirstRowMap(String sql, Object... params) {
		try {
			return getQueryRunner().query(sql, new MapHandler(), params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据传入的sql，查询记录，以Map形式返回第一行记录。 注意：如果有多行记录，只会返回第一行，所以适用场景需要注意，可以使用根据主键来查询的场景
	 * 
	 * @param sql
	 * @return
	 */
	public static Map<String, Object> getFirstRowMap(Connection connection, String sql) {
		try {
			return new QueryRunner().query(connection, sql, new MapHandler());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据传入的sql，查询记录，以Map形式返回第一行记录。 注意：如果有多行记录，只会返回第一行，所以适用场景需要注意，可以使用根据主键来查询的场景
	 * 
	 * @param sql
	 * @return
	 */
	public static Map<String, Object> getFirstRowMap(String sql) {
		try {
			// MapHandler 将ResultSet的首行转换为一个Map的ResultSetHandler实现类
			return getQueryRunner().query(sql, new MapHandler());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据sql查询返回所有记录，以List数组形式返回
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static List<Object[]> getListArray(Connection connection, String sql, Object... params) {
		try {
			return new QueryRunner().query(connection, sql, new ArrayListHandler(), params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据sql查询返回所有记录，以List数组形式返回
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static List<Object[]> getListArray(String sql, Object... params) {
		try {
			return getQueryRunner().query(sql, new ArrayListHandler(), params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据sql查询返回所有记录，以List数组形式返回
	 * 
	 * @param sql
	 * @return
	 */
	public static List<Object[]> getListArray(Connection connection, String sql) {
		try {
			new QueryRunner().query(connection, sql, new ArrayListHandler());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据sql查询返回所有记录，以List数组形式返回
	 * 
	 * @param sql
	 * @return
	 */
	public static List<Object[]> getListArray(String sql) {
		try {
			// ArrayListHandler 将ResultSet转换为List<Object[]>的ResultSetHandler实现类
			return getQueryRunner().query(sql, new ArrayListHandler());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据传入的sql，查询记录，以数组形式返回第一行记录。 注意：如果有多行记录，只会返回第一行，所以适用场景需要注意，可以使用根据主键来查询的场景
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static Object[] getFirstRowArray(Connection connection, String sql, Object... params) {
		try {
			return new QueryRunner().query(connection, sql, new ArrayHandler(), params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据传入的sql，查询记录，以数组形式返回第一行记录。 注意：如果有多行记录，只会返回第一行，所以适用场景需要注意，可以使用根据主键来查询的场景
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static Object[] getFirstRowArray(String sql, Object... params) {
		try {
			return getQueryRunner().query(sql, new ArrayHandler(), params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据传入的sql，查询记录，以数组形式返回第一行记录。 注意：如果有多行记录，只会返回第一行，所以适用场景需要注意，可以使用根据主键来查询的场景
	 * 
	 * @param sql
	 * @return
	 */
	public static Object[] getFirstRowArray(Connection connection, String sql) {
		try {
			return new QueryRunner().query(connection, sql, new ArrayHandler());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据传入的sql，查询记录，以数组形式返回第一行记录。 注意：如果有多行记录，只会返回第一行， 所以适用场景需要注意，可以使用根据主键来查询的场景
	 * 
	 * @param sql
	 * @return
	 */
	public static Object[] getFirstRowArray(String sql) {
		try {
			return getQueryRunner().query(sql, new ArrayHandler());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 得到查询记录的条数
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int getCount(String sql, Object... params) {
		try {
			Object value = getQueryRunner().query(sql, new ScalarHandler<>(), params);
			return CommonUtil.objectToInteger(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	/**
	 * 得到查询记录的条数
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int getCount(Connection conn, String sql, Object... params) {
		try {
			// ScalarHandler 将ResultSet的一个列到一个对象
			Object value = new QueryRunner().query(conn, sql, new ScalarHandler<>(), params);
			return CommonUtil.objectToInteger(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	/**
	 * 得到查询记录的条数
	 * 
	 * @param sql
	 * @return
	 */
	public static int getCount(String sql) {
		try {
			Object value = getQueryRunner().query(sql, new ScalarHandler<>());
			return CommonUtil.objectToInteger(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	/*
	 * 得到查询记录的条数
	 * 
	 * @param sql
	 * 
	 * @return 查询记录条数
	 */
	public static int getCount(Connection connection, String sql) {
		try {
			// ScalarHandler 将ResultSet的一个列到一个对象
			Object value = new QueryRunner().query(connection, sql, new ScalarHandler<>());
			return CommonUtil.objectToInteger(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}
		/**
	 * 开启事务
	 */
	public static void beginTransaction(Connection conn) {
		try {
			// 开启事务
			conn.setAutoCommit(false);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 回滚事务
	 */
	public static void rollback(Connection conn) {
		try {
			conn.rollback();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 提交事务
	 */
	public static void commit(Connection conn) {
		try {
			conn.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	}
