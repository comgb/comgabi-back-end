package comgb.comgabi.newsCrawler.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import comgb.comgabi.newsCrawler.model.News;

public interface NewsRepository extends JpaRepository<News, Integer> {
}
