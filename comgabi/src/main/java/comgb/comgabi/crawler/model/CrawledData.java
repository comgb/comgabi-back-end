package comgb.comgabi.crawler.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CrawledData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String url;
    private LocalDateTime crawledAt;

    // 기본 생성자, Getter/Setter 추가
    public CrawledData() {}

    public CrawledData(String title, String url, LocalDateTime crawledAt) {
        this.title = title;
        this.url = url;
        this.crawledAt = crawledAt;
    }

    // Getters and Setters
}
