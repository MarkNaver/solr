package com.demo.solr.repository;

import com.demo.solr.entity.demo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TestRepository
 *
 * @author WuYuxiang
 * @date 2018/8/28
 */
@Repository
public class TestRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate jdbc;


    /**
     * 查询天猫所有商品信息
     * @return
     */
    public List<ProductVO> findAllProductVO() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT TM.ID, TM.TITLE, TM.TYPE, TM.PRICE, TM.AREA, TM.PRODUCTID FROM TMALL TM");
        SqlParameterSource paramSource = new MapSqlParameterSource();
        List<ProductVO> list = jdbc.query(sql.toString(), paramSource, new BeanPropertyRowMapper<>(ProductVO.class));
        return list;
    }
}
