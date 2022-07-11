package org.trab.test.dbreststorage.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class JMinorVersionDTOMapper  implements RowMapper<MinorVersionJDTO> {

	@Override
	public MinorVersionJDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		MinorVersionJDTO ret=new  MinorVersionJDTO();
		ret.name=rs.getString("NAME");
		return ret;
	}

}
