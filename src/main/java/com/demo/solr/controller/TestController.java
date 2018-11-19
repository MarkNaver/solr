package com.demo.solr.controller;

import com.demo.solr.entity.demo.ProductVO;
import com.demo.solr.entity.json.JSONResponse;
import com.demo.solr.service.TestService;
import com.demo.solr.util.SolrUtil;
import net.sf.json.JSONArray;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TestController
 *
 * @author WuYuxiang
 * @date 2018/8/27
 */
@Controller
@RequestMapping("/")
public class TestController {

    @Autowired
    TestService service;

    JSONResponse jsonResponse;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 测试首页
     *
     * @param model
     * @return
     */
    @RequestMapping("index")
    public String index(Model model) {
        model.addAttribute("msg", "This is a sample page!");
        return "demo/index";
    }

    /**
     * 测试插入索引
     *
     * @param model
     * @return
     * @throws IOException
     * @throws SolrServerException
     */
    @RequestMapping("testImport")
    public String testImport(Model model) throws IOException, SolrServerException {
        List<ProductVO> list = service.findAllProductVO();
        boolean flag = SolrUtil.batchSaveOrUpdate(list);
        model.addAttribute("msg", flag);
        return "demo/index";
    }

    /**
     * 分词测试
     *
     * @param sentence
     * @return
     * @throws IOException
     * @throws SolrServerException
     */
    @RequestMapping("testSeparateWord")
    @ResponseBody
    public JSONResponse testSeparateWord(String sentence) throws IOException, SolrServerException {
        jsonResponse = new JSONResponse();
        List<String> results = SolrUtil.getAnalysis(sentence);
        jsonResponse.setJsonArray(JSONArray.fromObject(results));
        jsonResponse.setResult(JSONResponse.Result.SUCCESS);
        return jsonResponse;
    }

    /**
     * 测试查询
     *
     * @param keyValue
     * @return
     * @throws IOException
     * @throws SolrServerException
     */
    @RequestMapping("testQuery")
    @ResponseBody
    public JSONResponse testQuery(String keyValue, String sortProperty) throws IOException, SolrServerException {
        jsonResponse = new JSONResponse();
        QueryResponse queryResponse = SolrUtil.query("title", keyValue, sortProperty, 0, 10);
        // 获取高亮显示信息
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        List<ProductVO> productVOList = new ArrayList<>();
        for (SolrDocument document : queryResponse.getResults()) {
            ProductVO productVO = new ProductVO();
            productVO.setId(Integer.parseInt(document.get("id").toString()));
            productVO.setTitle(highlighting.get(document.get("id")).get("title").get(0));
            productVO.setType(document.get("type").toString());
            productVO.setPrice(Float.parseFloat(document.get("price").toString()));
            productVO.setArea(document.get("area").toString());
            productVO.setProductId(document.get("productId").toString());
            productVOList.add(productVO);
        }
        jsonResponse.setTotal((int) queryResponse.getResults().getNumFound());
        jsonResponse.setRows(productVOList);
        jsonResponse.setResult(JSONResponse.Result.SUCCESS);
        return jsonResponse;
    }



}
