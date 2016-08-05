##Apache DBUtils封装使用

	
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

	}
