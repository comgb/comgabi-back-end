package comgb.comgabi.newsCrawler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import comgb.comgabi.newsCrawler.service.CrawlerService;

@RestController
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
}
