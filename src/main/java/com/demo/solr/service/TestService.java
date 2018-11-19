package com.demo.solr.service;

import com.demo.solr.entity.demo.ProductVO;
import com.demo.solr.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TestService
 *
 * @author WuYuxiang
 * @date 2018/8/28
 */
@Service
public class TestService {

    @Autowired
    TestRepository repository;

    public List<ProductVO> findAllProductVO(){
        return repository.findAllProductVO();
    }


}
