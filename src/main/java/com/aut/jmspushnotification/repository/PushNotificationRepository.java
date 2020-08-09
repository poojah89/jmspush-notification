package com.aut.jmspushnotification.repository;

import java.math.BigDecimal;
import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Repository
public class PushNotificationRepository {
	
	@Autowired
    private JdbcTemplate template;
	
	
	 public void updateStatus(BigDecimal pId, Integer statusId){
	        SimpleJdbcCall call = new SimpleJdbcCall(template)
	                .withCatalogName("pnf_aq_cp").withProcedureName("message_status_update")
	                .declareParameters(
	                        new SqlParameter("p_id", Types.BIGINT),
	                        new SqlParameter("p_status_id", Types.NUMERIC)
	                );

	        MapSqlParameterSource paramMap = new MapSqlParameterSource()
	                .addValue("p_id", pId)
	                .addValue("p_status_id", statusId);

	       call.execute(paramMap);
	    }
	

}
