package com.demo.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * SolrTest
 *
 * @author WuYuxiang
 * @date 2018/8/27
 */
public class SolrTest {

    @Autowired
    SolrClient solrClient;

    private String url = "http://localhost:8080/solr/student";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 日志记录测试
     */
    @Test
    public void test() {
        logger.info("info:{}", "info");
        logger.error("error:{}", "error");
    }

    /**
     * 添加索引
     *
     * @throws IOException
     * @throws SolrServerException
     */
    @Test
    public void insertOrUpdateIndex() throws IOException, SolrServerException {
        // 创建HttpSolrClient
        HttpSolrClient client = new HttpSolrClient.Builder(url)
                .withConnectionTimeout(5000)
                .withSocketTimeout(5000)
                .build();

        // 创建Document对象
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", "6");
        document.addField("studentName", "张三");
        document.addField("studentAge", "88");

        client.add(document);

        client.commit();
        client.close();
    }

    /**
     * 删除索引
     *
     * @throws IOException
     * @throws SolrServerException
     */
    @Test
    public void deleteIndex() throws IOException, SolrServerException {
        HttpSolrClient client = new HttpSolrClient.Builder(url)
                .withConnectionTimeout(5000)
                .withSocketTimeout(5000)
                .build();
        client.deleteById("6");
        // 根据条件删除
        //client.deleteByQuery("id:1111");
        // 全部删除
        //client.deleteByQuery("*:*");
        client.commit();
        client.close();
    }

    /**
     * 简单查询
     *
     * @throws IOException
     * @throws SolrServerException
     */
    @Test
    public void simpleSearch() throws IOException, SolrServerException {
        HttpSolrClient client = new HttpSolrClient.Builder(url)
                .withConnectionTimeout(5000)
                .withSocketTimeout(5000)
                .build();
        // 创建SolrQuery
        SolrQuery query = new SolrQuery();
        // 输入查询条件
        query.setQuery("studentName:测试");
        // 执行查询并返回结果
        QueryResponse response = client.query(query);
        // 获取匹配的所有结果
        SolrDocumentList list = response.getResults();
        // 匹配结果总数
        long count = list.getNumFound();
        System.out.println("总结果数：" + count);

        for (SolrDocument document : list) {
            System.out.println(document.get("id"));
            System.out.println(document.get("studentName"));
            System.out.println(document.get("studentAge"));
            System.out.println("================");
        }

    }

    /**
     * 复杂条件查询
     *
     * @throws IOException
     * @throws SolrServerException
     */
    @Test
    public void complexSearch() throws IOException, SolrServerException {
        HttpSolrClient client = new HttpSolrClient.Builder(url)
                .withConnectionTimeout(5000)
                .withSocketTimeout(5000)
                .build();
        SolrQuery query = new SolrQuery();
        // 输入查询条件
        query.setQuery("studentName:测试");
        // 设置过滤条件
//        query.setFilterQueries("id:[1 TO 4]");
        // 设置排序
//        query.addSort("id", SolrQuery.ORDER.desc);
        // 设置分页信息（使用默认的）
//        query.setStart(1);
//        query.setRows(2);
        // 设置显示的Field的域集合(两种方式二选一)
//        query.setFields(new String[]{"id", "title", "sellPoint", "price", "status" });
//        query.setFields("id,studentName,studentAge");
        // 设置默认域
//        query.set("df", "product_keywords");
        // 设置高亮信息
        query.setHighlight(true);
        query.addHighlightField("studentName");
        query.setHighlightSimplePre("<span color='red'>");
        query.setHighlightSimplePost("</span>");
        // 执行查询并返回结果
        QueryResponse response = client.query(query);
        // 获取匹配的所有结果
        SolrDocumentList list = response.getResults();
        // 匹配结果总数
        long count = list.getNumFound();
        System.out.println("总结果数：" + count);
        // 获取高亮显示信息
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        for (SolrDocument document : list) {
            System.out.println(document.get("id"));
            List<String> list2 = highlighting.get(document.get("id")).get("studentName");
            if (list2 != null)
                System.out.println("高亮显示的数据：" + list2.get(0));
            else {
                System.out.println(document.get("title"));
            }

        }
    }


}
