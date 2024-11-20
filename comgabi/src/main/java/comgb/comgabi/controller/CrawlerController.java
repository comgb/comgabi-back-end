package comgb.comgabi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import comgb.comgabi.newsCrawler.model.News;
import comgb.comgabi.service.CrawlerService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class CrawlerController {

    @Autowired
    private CrawlerService crawlerService;

    @GetMapping("/api/crawl-news")
    public String crawlNews(@RequestParam("url") String url) {
        try {
            crawlerService.fetchAndSaveNewsData(url);
            return "News data has been crawled and saved successfully.";
        } catch (Exception e) {
            return "Error while crawling news data: " + e.getMessage();
        }
    }

    // 저장된 뉴스 데이터 가져오기 API
    @GetMapping("/api/news")
    public List<News> getAllNews(@RequestParam("numberOfData") int numberOfData) {
        return crawlerService.getAllEntities(numberOfData);
    }
}
