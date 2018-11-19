package com.demo.solr.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.FieldAnalysisRequest;
import org.apache.solr.client.solrj.response.AnalysisResponseBase;
import org.apache.solr.client.solrj.response.FieldAnalysisResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.StringUtils;

/**
 * SolrUtil
 *
 * @author WuYuxiang
 * @date 2018/8/28
 */
public class SolrUtil {

    public static SolrClient client;
    private static String url;

    static {
        url = "http://localhost:8080/solr/product";
        client = new HttpSolrClient.Builder(url)
                .withConnectionTimeout(5000)
                .withSocketTimeout(5000)
                .build();
    }

    /**
     * 添加或者更新索引
     * @param entities
     * @param <T>
     * @return
     * @throws SolrServerException
     * @throws IOException
     */
    public static <T> boolean batchSaveOrUpdate(List<T> entities) throws SolrServerException, IOException {

        DocumentObjectBinder binder = new DocumentObjectBinder();
        int total = entities.size();
        int count = 0;
        for (T t : entities) {
            SolrInputDocument doc = binder.toSolrInputDocument(t);
            client.add(doc);
            System.out.printf("添加数据到索引中，总共要添加 %d 条记录，当前添加第%d条 %n", total, ++count);
        }
        client.commit();
        return true;
    }

    /**
     * 删除索引
     * @param id
     * @return
     */
    public static boolean deleteById(String id) {
        try {
            client.deleteById(id);
            client.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 查询索引
     * @param keywords
     * @param keyvalue
     * @param sortproperty
     * @param startOfPage
     * @param numberOfPage
     * @return
     * @throws SolrServerException
     * @throws IOException
     */
    public static QueryResponse query(String keywords,String keyvalue,String sortproperty, int startOfPage, int numberOfPage) throws SolrServerException, IOException {
        SolrQuery query = new SolrQuery();
        query.setQuery(keywords+":"+keyvalue);
        // 设置过滤条件
//        query.setFilterQueries("id:[1 TO 4]");
        // 设置排序
        if(org.apache.commons.lang.StringUtils.isNotBlank(sortproperty)){
            query.addSort(sortproperty, SolrQuery.ORDER.desc);
        }

        // 设置分页信息（使用默认的）
        query.setStart(startOfPage);
        query.setRows(numberOfPage);
        // 设置显示的Field的域集合(两种方式二选一)
        // query.setFields(new String[]{"id", "title", "sellPoint", "price", "status" });
//        query.setFields("id,studentName,studentAge");
        // 设置默认域
        // query.set("df", "product_keywords");
        // 设置高亮信息
        query.setHighlight(true);
        query.addHighlightField(keywords);
        query.setHighlightSimplePre("<span style='color:red '>");
        query.setHighlightSimplePost("</span>");

        QueryResponse rsp = client.query(query);
        return rsp;
    }

    /**
     * 分词统计，把字符串分词并返回分词列表
     *
     * @param sentence
     * @return
     */
    public static List<String> getAnalysis(String sentence) {
        FieldAnalysisRequest request = new FieldAnalysisRequest(
                "/analysis/field");
        request.addFieldName("title");// 字段名，随便指定一个支持中文分词的字段
        request.setFieldValue("");// 字段值，可以为空字符串，但是需要显式指定此参数
        request.setQuery(sentence);

        FieldAnalysisResponse response = null;
        try {
            response = request.process(client);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> results = new ArrayList<String>();
        Iterator<AnalysisResponseBase.AnalysisPhase> it = response.getFieldNameAnalysis("title")
                .getQueryPhases().iterator();
        while (it.hasNext()) {
            AnalysisResponseBase.AnalysisPhase pharse = (AnalysisResponseBase.AnalysisPhase) it.next();
            List<AnalysisResponseBase.TokenInfo> list = pharse.getTokens();
            for (AnalysisResponseBase.TokenInfo info : list) {
                results.add(info.getText());
            }

        }
        return results;
    }
}
