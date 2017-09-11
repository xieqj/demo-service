package com.sinotn.spring.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class JdbcDaoImpl extends JdbcDaoSupport implements JdbcDao {

	@Override
	public void runTest() {
		JdbcTemplate jdbcTemplate=this.getJdbcTemplate();
		this.testNumber(jdbcTemplate);
	}

	public void testNumber(JdbcTemplate jdbcTemplate){
		String sql="INSERT INTO numb(field1,field2,field3,field4,field5) VALUES (?,?,?,?,?)";
		ParameterSetter ps=new ParameterSetter(5);
		ps.set((Byte)null);
		ps.set((Short)null);
		ps.set((Integer)null);
		ps.set((Float)null);
		ps.set((Double)null);
		jdbcTemplate.update(sql, ps);
		ps=new ParameterSetter(5);
		ps.set(Byte.MAX_VALUE);
		ps.set(Short.MAX_VALUE);
		ps.set(Integer.MAX_VALUE);
		ps.set(1.2);
		ps.set(Double.MAX_VALUE);
		jdbcTemplate.update(sql, ps);

		ps=new ParameterSetter(5);
		Short s=Short.MAX_VALUE;
		ps.set(s.byteValue());
		ps.set(Short.MAX_VALUE);
		ps.set(Integer.MAX_VALUE);
		ps.set(1.2);
		ps.set(Double.MAX_VALUE);
		jdbcTemplate.update(sql, ps);
	}
}
