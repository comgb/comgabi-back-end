package comgb.comgabi.controller;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import comgb.comgabi.danawa_crawler.service.ProductService;
import comgb.comgabi.newsCrawler.model.News;
import comgb.comgabi.newsCrawler.service.CrawlerService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class CrawlerController {

    @Autowired
    private CrawlerService crawlerService;

    @Autowired
    private ProductService productService;

    @GetMapping("/api/crawl-news")
    public String crawlNews(@RequestParam("url") String url) {
        try {
            crawlerService.fetchAndSaveNewsData(url);
            return "News data has been crawled and saved successfully.";
        } catch (Exception e) {
            return "Error while crawling news data: " + e.getMessage();
        }
    }

    @GetMapping("/api/news")
    public List<News> getAllNews(@RequestParam("numberOfData") int numberOfData) {
        return crawlerService.getAllEntities(numberOfData);
    }

    @GetMapping("/api/crawl-product")
    public String crawlProduct(@RequestParam("query") String query) {
        Random random = new Random();
        int index = random.nextInt(41);
        try {
            productService.saveProductData(query, index);
            return "Product data has been crawled and saved successfully.";
        } catch (Exception e) {
            return "Error while crawling product data: " + e.getMessage();
        }
    }
}
