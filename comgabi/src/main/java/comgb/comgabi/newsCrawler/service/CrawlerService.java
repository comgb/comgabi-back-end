package comgb.comgabi.newsCrawler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import comgb.comgabi.newsCrawler.model.News;
import comgb.comgabi.newsCrawler.repository.NewsRepository;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CrawlerService {

    @Autowired
    private NewsRepository newsRepository;

    public void fetchAndSaveNewsData(String url) throws IOException {
        // 배열 데이터 가져오기
        String[] titleArray;
        Date[] dateArray;
        String[] publisherArray;
        String[] urlArray;

        try {
            URL feedUrl = new URL(url);
            XmlReader reader = new XmlReader(feedUrl);
            SyndFeed feed = new SyndFeedInput().build(reader);
            List<SyndEntry> entries = feed.getEntries();

            // 배열 초기화
            titleArray = new String[entries.size()];
            dateArray = new Date[entries.size()];
            publisherArray = new String[entries.size()];
            urlArray = new String[entries.size()];

            // 데이터 채우기
            int index = 0;
            for (SyndEntry entry : entries) {
                titleArray[index] = entry.getTitle();
                if (titleArray[index].contains(" -")) {
                    titleArray[index] = titleArray[index].substring(0, titleArray[index].indexOf(" -"));
                }
                dateArray[index] = entry.getPublishedDate();
                publisherArray[index] = entry.getSource() != null ? entry.getSource().getTitle() : "Unknown";
                urlArray[index] = entry.getLink();
                index++;
            }

            // 배열 데이터를 반복문을 통해 개별적으로 DB에 저장
            for (int i = 0; i < titleArray.length; i++) {
                News news = new News(titleArray[i], dateArray[i], publisherArray[i], urlArray[i]);
                newsRepository.save(news);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<News> fetchNewsData(String url) {
        try {
            URL feedUrl = new URL(url);
            XmlReader reader = new XmlReader(feedUrl);
            SyndFeed feed = new SyndFeedInput().build(reader);
            List<SyndEntry> entries = feed.getEntries();

            List<News> newsList = new ArrayList<>();

            for (SyndEntry entry : entries) {
                String title = entry.getTitle();
                if (title.contains(" -")) {
                    title = title.substring(0, title.indexOf(" -"));
                }
                Date publishedDate = entry.getPublishedDate();
                String publisher = entry.getSource() != null ? entry.getSource().getTitle() : "Unknown";
                String link = entry.getLink();

                // News 객체 생성 후 리스트에 추가
                News news = new News(title, publishedDate, publisher, link);
                newsList.add(news);
            }

            return newsList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}