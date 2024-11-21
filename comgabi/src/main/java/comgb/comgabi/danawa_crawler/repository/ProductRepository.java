package comgb.comgabi.danawa_crawler.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import comgb.comgabi.danawa_crawler.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
