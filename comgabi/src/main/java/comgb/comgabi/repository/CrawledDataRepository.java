package comgb.comgabi.repository;

import comgb.comgabi.model.CrawledData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawledDataRepository extends JpaRepository<CrawledData, Long> {
}
