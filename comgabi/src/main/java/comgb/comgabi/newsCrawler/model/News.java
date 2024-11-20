package comgb.comgabi.newsCrawler.model;

import java.util.Date;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer newsId;

    private String title;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private String publisher;

    @Column(length = 2083) // URL 최대 길이 설정
    private String url;

    public News(String title, Date createdDate, String publisher, String url) {
        this.title = title;
        this.createdDate = createdDate;
        this.publisher = publisher;
        this.url = url;
    }

    // 기본 생성자 필요
    public News() {
    }
}
