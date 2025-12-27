package comgb.comgabi.newsCrawler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import comgb.comgabi.newsCrawler.model.News;
import comgb.comgabi.newsCrawler.repository.NewsRepository;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CrawlerService {

    @Autowired
    private NewsRepository newsRepository;

    public void fetchAndSaveNewsData(String url) {
        try {
            URL feedUrl = new URL(url);
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(feedUrl));

            System.out.println("entries size = " + feed.getEntries().size());
            System.out.println(feed.getFeedType());
            System.out.println(feed.getTitle());

            URL urlObj = new URL(url);
            URLConnection conn = urlObj.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            for (SyndEntry entry : feed.getEntries()) {
                String title = entry.getTitle();
                if (title != null && title.contains(" -")) {
                    title = title.substring(0, title.indexOf(" -"));
                }

                Date publishedDate = entry.getPublishedDate();
                String publisher = entry.getSource() != null ? entry.getSource().getTitle() : null;
                String link = entry.getLink();

                News news = new News(title, publishedDate, publisher, link);
                newsRepository.save(news);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch RSS", e);
        }
    }

    @Transactional
    public List<News> getAllEntities(int numberOfData) {
        List<News> newsList = newsRepository.findAll();
        Random random = new Random();

        // 현재 시간에서 3일 전을 계산
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);

        // newsList에서 createdDate가 3일 전 이후인 데이터만 필터링
        newsList = newsList.stream()
                .filter(news -> {
                    if (news.getCreatedDate() != null) {
                        LocalDateTime createdDateTime = news.getCreatedDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime();
                        return createdDateTime.isAfter(threeDaysAgo);
                    }
                    return false; // createdDate가 null이면 제외
                })
                .collect(Collectors.toList());
        if (newsList.size() < numberOfData) {
            numberOfData = newsList.size();
        }
        else if (newsList.size() == 0) {
            return newsList;
        }
        List<News> newNewsList = new ArrayList<>();
        ArrayList<Integer> excludingList = new ArrayList<>();
        for (int i = 0; i < numberOfData; i++) {
            News news = newsList.get(random.nextInt(newsList.size() + 1));
            if (excludingList.contains(news.getNewsId())) {
                i -= 1;
                continue;
            } else {
                newNewsList.add(news);
                excludingList.add(news.getNewsId());
            }
        }
        return newNewsList;
    }
}