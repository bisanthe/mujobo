package com.mujobo.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mujobo.solr.model.JobDoc;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.codehaus.groovy.util.StringUtil;

public class Solr {
    private static final SolrServer jobServer = new HttpSolrServer("http://localhost:8983/solr/job");
    ;

    private Solr() {
    }

    public static void saveJob(JobDoc jobDoc) {
        try {
            jobServer.addBean(jobDoc);
            jobServer.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public static List<JobDoc> defaultSearch(String keyword, String location) {
        SolrQuery solrQuery = new SolrQuery();
        if (StringUtils.isNotBlank(keyword) && StringUtils.isBlank(location)) {
            solrQuery.setQuery("text:" + keyword);
            solrQuery.setParam("hl.q","text:"+keyword);
        } else if (StringUtils.isBlank(keyword) && StringUtils.isNotBlank(location)) {
            solrQuery.setQuery("location:" + location);
            solrQuery.setParam("hl.q","text:"+keyword);
        } else if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(location)) {
            solrQuery.setQuery("text:" + keyword + " AND location:" + location);
            solrQuery.setParam("hl.q","text:"+keyword);
        }
        else{
            solrQuery.setQuery("text:* AND location:*");
        }

        solrQuery.setRows(25).
                setFacet(true).
                setFacetMinCount(1).
                setFacetLimit(8).
                addFacetField("companyName").
                addFacetField("title");
        solrQuery.setParam("hl",true);
        //
        solrQuery.setParam("hl.fl","title description companyName");
        //solrQuery.setParam("hl.requireFieldMatch",true);
        //solrQuery.setParam("hl.highlightMultiTerm",true);


        solrQuery.setParam("f.description.hl.fragsize","160");
        solrQuery.setParam("f.description.hl.snippets","1");
        solrQuery.setParam("f.description.hl.alternateField","description");
        solrQuery.setParam("hl.maxAlternateFieldLength","160");
        return search(solrQuery);
    }

    public static List<JobDoc> mapSearch(String keyword, String location) {
        SolrQuery solrQuery = new SolrQuery();
        if (StringUtils.isNotBlank(keyword) && StringUtils.isBlank(location)) {
            solrQuery.setQuery("text:" + keyword + " AND hasLocation:T");
            solrQuery.setParam("hl.q","text:"+keyword);
        } else if (StringUtils.isBlank(keyword) && StringUtils.isNotBlank(location)) {
            solrQuery.setQuery("location:" + location + " AND hasLocation:T");
            solrQuery.setParam("hl.q","text:"+keyword);
        } else if (StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(location)) {
            solrQuery.setQuery("text:" + keyword + " AND location:" + location + " AND hasLocation:T");
            solrQuery.setParam("hl.q","text:"+keyword);
        }
        else{
            solrQuery.setQuery("text:* AND location:*");
        }

        solrQuery.setRows(400).
                setFacet(true).
                setFacetMinCount(1).
                setFacetLimit(8).
                addFacetField("companyName").
                addFacetField("title");
        solrQuery.setParam("hl",true);
        //
        solrQuery.setParam("hl.fl","title description companyName");
        //solrQuery.setParam("hl.requireFieldMatch",true);
        //solrQuery.setParam("hl.highlightMultiTerm",true);


        solrQuery.setParam("f.description.hl.fragsize","160");
        solrQuery.setParam("f.description.hl.snippets","1");
        solrQuery.setParam("f.description.hl.alternateField","description");
        solrQuery.setParam("hl.maxAlternateFieldLength","160");
        return search(solrQuery);
    }

    public static List<JobDoc> getAll() {
        SolrQuery solrQuery = new SolrQuery("*:*").setRows(Integer.MAX_VALUE);
        return search(solrQuery);
    }

    public static JobDoc byId(long id){
        SolrQuery solrQuery = new SolrQuery("id:"+id).setRows(1);
        return com.mujobo.util.ListUtil.first(search(solrQuery));

    }
    public static void deleteAll(){
        try {
            jobServer.deleteByQuery("*:*");
            jobServer.commit();
            jobServer.optimize();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List<JobDoc> search(SolrQuery query) {
        List<JobDoc> resultList = new ArrayList<JobDoc>();
        try {
            QueryResponse rsp = jobServer.query(query);
            SolrDocumentList docs = rsp.getResults();
            resultList = new ArrayList<JobDoc>(docs.size());
            for (SolrDocument doc : docs) {
                JobDoc jobDoc = new JobDoc(doc);
                if (rsp.getHighlighting() != null && rsp.getHighlighting().get(jobDoc.getId()) != null) {
                    List<String> highlightSnippetsTitle = rsp.getHighlighting().get(jobDoc.getId()).get("title");
                    List<String> highlightSnippetsDescription = rsp.getHighlighting().get(jobDoc.getId()).get("description");

                    if (highlightSnippetsTitle != null) {
                        jobDoc.applyHighlightedTitle(highlightSnippetsTitle);
                    }
                    if (highlightSnippetsDescription != null) {
                        jobDoc.applyHighlightedDescription(highlightSnippetsDescription);
                    }
                }
                resultList.add(jobDoc);
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public static void main(String[] args) {
        try {
            jobServer.addBean(new Object());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SolrServerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
