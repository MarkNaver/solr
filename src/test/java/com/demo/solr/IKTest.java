package com.demo.solr;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.FieldAnalysisRequest;
import org.apache.solr.client.solrj.response.FieldAnalysisResponse;
import org.junit.Test;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import java.util.*;
import java.util.Map.Entry;

import org.apache.solr.client.solrj.request.FieldAnalysisRequest;
import org.apache.solr.client.solrj.response.AnalysisResponseBase.AnalysisPhase;
import org.apache.solr.client.solrj.response.AnalysisResponseBase.TokenInfo;
import org.apache.solr.client.solrj.response.FieldAnalysisResponse;

import java.util.List;

/**
 * IKTest
 *
 * @author WuYuxiang
 * @date 2018/8/28
 */

public class IKTest {

    private String url = "http://localhost:8080/solr/product";

    /**
     * IK 分词插件测试
     */
    @Test
    public void IKTest() {
        SolrClient solrClient = new HttpSolrClient.Builder(url)
                .withConnectionTimeout(5000)
                .withSocketTimeout(5000)
                .build();

        String txt = "中国特色社会主义伟大胜利";
        List<String> results = getAnalysis(solrClient, txt);
        for (String word : results) {
            System.out.println(word);
        }
    }

    /**
     * 分词统计，把字符串分词并返回分词列表
     *
     * @param sentence
     * @return
     */
    public static List<String> getAnalysis(SolrClient solrClient, String sentence) {
        FieldAnalysisRequest request = new FieldAnalysisRequest(
                "/analysis/field");
        request.addFieldName("title");// 字段名，随便指定一个支持中文分词的字段
        request.setFieldValue("");// 字段值，可以为空字符串，但是需要显式指定此参数
        request.setQuery(sentence);

        FieldAnalysisResponse response = null;
        try {
            response = request.process(solrClient);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> results = new ArrayList<String>();
        Iterator<AnalysisPhase> it = response.getFieldNameAnalysis("title")
                .getQueryPhases().iterator();
        while (it.hasNext()) {
            AnalysisPhase pharse = (AnalysisPhase) it.next();
            List<TokenInfo> list = pharse.getTokens();
            for (TokenInfo info : list) {
                results.add(info.getText());
            }

        }
        return results;
    }


}
