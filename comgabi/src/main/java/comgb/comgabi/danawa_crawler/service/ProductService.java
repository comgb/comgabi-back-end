package comgb.comgabi.danawa_crawler.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import comgb.comgabi.danawa_crawler.ProductURLCrawler;
import comgb.comgabi.danawa_crawler.SubcutaneousObject;
import comgb.comgabi.danawa_crawler.model.Product;
import comgb.comgabi.danawa_crawler.repository.ProductRepository;
import lombok.Data;

@Data
@Service
public class ProductService {

    @Autowired
    ProductURLCrawler productURLCrawler;

    @Autowired
    SubcutaneousObject subcutaneousObject;

    @Autowired
    ProductRepository productRepository;

    public void saveProductData(String query, int index) {
        List<Product> products = this.productCombined(query, index);
        for(Product product : products) {
            productRepository.save(product);
        }
    }

    private List<Product> productCombined(String query, int index) {
        List<Product> productList = new ArrayList<>();

        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String url = "https://search.danawa.com/dsearch.php?k1=" + encodedQuery;
    
        // fluidProductInformationCrawler로 크롤링된 정보를 가져오기
        List<List<String>> fluidList = subcutaneousObject.fluidProductInformationCrawler(url);

        // 각각의 리스트를 분리
        List<String> priceList = fluidList.get(0);          // 가격
        List<String> sellerList = fluidList.get(1);         // 판매자
        List<String> urlList = fluidList.get(2);            // URL
        List<String> shoppingCostList = fluidList.get(3);   // 배송비
    
        // fixedProductInformation으로 크롤링된 정보를 가져오기
        List<String> fixedInfoList = subcutaneousObject.fixedProductInformation(url);
        String manufacturer = fixedInfoList.get(0);        // 제조사
        String category = fixedInfoList.get(1);            // 카테고리
        String model = fixedInfoList.get(2);               // 모델명
        String productDescription = fixedInfoList.get(3);  // 제품 설명
        String productDetailsTable = fixedInfoList.get(4); // 제품 상세 정보
        String imageUrl = fixedInfoList.get(5);            // 이미지 URL
    
        // 크롤링된 정보를 사용하여 Product 객체 생성
        for (int i = 0; i < priceList.size(); i++) {
            // 각 Product 객체에 대한 정보 설정
            Product product = new Product();
            product.setPrice(Integer.parseInt(priceList.get(i))); // 가격
            product.setManufacturer(manufacturer);               // 제조사
            product.setModel(model);                             // 모델
            product.setCategory(category);                       // 카테고리
            product.setSeller(sellerList.get(i));                // 판매자
            product.setImageUrl(imageUrl);                       // 이미지 URL
            product.setShoppingCost(Integer.parseInt(shoppingCostList.get(i))); // 배송비
            product.setProductDetailsTable(productDetailsTable); // 제품 상세 테이블
            product.setProductDescription(productDescription);   // 제품 설명
            product.setUrl(urlList.get(i));                      // 제품 URL
    
            // 생성된 Product 객체를 리스트에 추가
            productList.add(product);
        }
    
        return productList;
    }
    
}
