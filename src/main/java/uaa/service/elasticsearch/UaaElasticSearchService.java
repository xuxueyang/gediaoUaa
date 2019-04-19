package uaa.service.elasticsearch;

import com.netflix.discovery.converters.Auto;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class UaaElasticSearchService {
    @Autowired
    private TransportClient transportClient;

    @GetMapping("/get/book/novel")
    @ResponseBody
    public ResponseEntity get(@RequestParam(name = "id") String id){
        GetResponse getFields = this.transportClient.prepareGet("book", "novel", id).get();
        if(!getFields.isExists()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/get/book/novel")
    @ResponseBody
    public ResponseEntity add(@RequestParam(name = "title") String title,
                              @RequestParam(name = "publish_date")
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date publishDate){
        try {
            XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
                .startObject()
                .field("name_title", title).field("publish_date", publishDate.getTime())
                .endObject();
            IndexResponse indexResponse = this.transportClient.prepareIndex("book", "nove").setSource().get();
            return new ResponseEntity(indexResponse.getId(),HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }
    @DeleteMapping("/delete/book/novel")
    @ResponseBody
    public ResponseEntity delte(@RequestParam(name = "id") String id){
//        this.transportClient.prepareDelete()
        return new ResponseEntity(HttpStatus.OK);
    }
    @PutMapping("/update/book/novel")
    @ResponseBody
    public ResponseEntity update(@RequestParam(name = "id") String id){
//        this.transportClient.prepareDelete()
        UpdateRequest updateRequest = new UpdateRequest("","","");

        try {
            XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
            builder.field("","");
            builder.field("","");
            builder.endObject();
            updateRequest.doc(builder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.transportClient.update(updateRequest).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }
    @PostMapping("query/book/novel")
    @ResponseBody
    public ResponseEntity query(){
        //复合查询
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.must(QueryBuilders.matchQuery("author","a"));
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("word_count").from(1).to(20);

        builder.filter(rangeQueryBuilder);

        SearchRequestBuilder searchRequestBuilder = this.transportClient.prepareSearch("book").setTypes("novel")
            .setQuery(builder).setFrom(0).setSize(10);
        System.out.println(searchRequestBuilder);
        SearchResponse searchResponse = searchRequestBuilder.get();
        List<Map<String,Object>> result = new ArrayList<>();
        for(SearchHit hit:searchResponse.getHits()){
            result.add(hit.getSource());
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
