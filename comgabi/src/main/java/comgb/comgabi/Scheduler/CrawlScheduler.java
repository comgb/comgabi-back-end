package comgb.comgabi.Scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import comgb.comgabi.newsCrawler.service.CrawlerService;

@Component
public class CrawlScheduler {

    private final CrawlerService crawlerService;

    public CrawlScheduler(CrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @Scheduled(cron = "0 0 12 * * *")
    public void crawlDailyAtNoon() {
        crawlerService.fetchAndSaveNewsData("https://news.google.com/rss/topics/CAAqKAgKIiJDQkFTRXdvSkwyMHZNR1ptZHpWbUVnSnJieG9DUzFJb0FBUAE?hl=ko%26gl=KR%26ceid=KR:ko");
    }
}

