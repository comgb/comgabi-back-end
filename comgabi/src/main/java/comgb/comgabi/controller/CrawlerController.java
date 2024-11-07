package comgb.comgabi.controller;

import comgb.comgabi.model.CrawledData;
import comgb.comgabi.repository.CrawledDataRepository;
import comgb.comgabi.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CrawlerController {

    @Autowired
    private CrawlerService crawlerService;

    @Autowired
    private CrawledDataRepository crawledDataRepository;

    // URL에서 데이터를 크롤링하고 저장하는 엔드포인트
    @GetMapping("/crawl")
    public List<String> crawl(@RequestParam String url) {
        try {
            // 크롤링된 제목을 리스트로 반환
            return crawlerService.fetchHeadlines(url);
        } catch (IOException e) {
            e.printStackTrace();
            return List.of("Error fetching data");
        }
    }

    // 저장된 모든 크롤링 데이터를 가져오는 엔드포인트
    @GetMapping("/data")
    public List<CrawledData> getAllCrawledData() {
        return crawledDataRepository.findAll();  // DB에 저장된 모든 크롤링 데이터 반환
    }
}
