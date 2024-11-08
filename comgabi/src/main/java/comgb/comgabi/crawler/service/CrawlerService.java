package comgb.comgabi.crawler.service;

import comgb.comgabi.crawler.model.CrawledData;
import comgb.comgabi.crawler.repository.CrawledDataRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CrawlerService {

    @Autowired
    private CrawledDataRepository crawledDataRepository;

    // 크롤링 데이터를 가져오는 메서드
    public List<String> fetchHeadlines(String url) throws IOException {
        List<String> headlines = new ArrayList<>();
        
        // Jsoup을 사용하여 HTML 페이지 파싱
        Document document = Jsoup.connect(url).get();
        
        // 페이지 내 h2 태그를 찾음 (예: 뉴스 제목 등)
        Elements elements = document.select("h2");

        for (Element element : elements) {
            String title = element.text();  // 각 h2 태그에서 텍스트 추출
            headlines.add(title);

            // 크롤링된 데이터를 데이터베이스에 저장
            CrawledData data = new CrawledData(title, url, LocalDateTime.now());
            crawledDataRepository.save(data);  // 데이터 저장
        }
        
        return headlines;  // 추출된 제목 리스트 반환
    }
}
