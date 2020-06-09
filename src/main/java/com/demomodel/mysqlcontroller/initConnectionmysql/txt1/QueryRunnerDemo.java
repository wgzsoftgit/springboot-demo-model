package com.demomodel.mysqlcontroller.initConnectionmysql.txt1;
/*
* 测试写好的工具类,
* 提供的是一个DataSource接口的数据源
* QueryRunner类构造方法,接收DataSource接口的实现类
* 后面,调用方法update,query,无需传递他们Connection连接对象
*/

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class QueryRunnerDemo {
	public static void main(String[] args) {
// insert();
// select();
		update(35);
	}

//定义2个方法,实现数据表的添加,数据表查询
//QueryRunner类对象,写在类成员位置
	private static QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());

//数据表查询
	public static void select() {
		String sql = "SELECT * FROM sort";
		List<Object[]> list = qr.query(sql, new ArrayList());
		for (Object[] objs : list) {
			for (Object obj : objs) {
				System.out.print(obj + "\t");
			}
			System.out.println();
		}
	}

//数据表添加数据
	public static void insert() {
		String sql = "INSERT INTO sort (sname,sprice,sdesc)VALUES(?,?,?)";
		Object[] params = { "水果", 100.12, "刚刚上市的核桃" };
		int row = qr.update(sql, params);
		System.out.println(row);
	}

// 数据表修改
	public static void update(int num) {
		String sql = "UPDATE sort SET sname = 'IT行业' WHERE sid = ?";
		int row;
		row = qr.update(sql, num);
		System.out.println(row);

	}

}
//https://blog.csdn.net/Turbo_Come/java/article/details/85332243