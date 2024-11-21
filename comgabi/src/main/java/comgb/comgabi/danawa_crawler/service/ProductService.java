package comgb.comgabi.danawa_crawler.service;

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

        // 제품 URL을 가져오는 메서드 호출
        List<String> urls = productURLCrawler.productURLCrawler(query);
        
        // fluidProductInfo와 fixedProductInfo를 가져오는 메서드 호출
        List<List<String>> fluidProductsInfo = subcutaneousObject.fluidProductInformationCrawler(urls.get(index));
        List<String> fixedProductsInfo = subcutaneousObject.fixedProductInformation(urls.get(index));
    
        // 제품을 저장할 리스트
        List<Product> products = new ArrayList<>();
    
        // fixedProductsInfo의 길이를 체크하여 부족한 데이터는 null이나 기본값을 사용
        if (fixedProductsInfo.size() < 6) {
            // 필수 데이터가 부족하면 빈 리스트를 반환
            return products;
        }
    
        // fixedProductsInfo에서 데이터를 가져옴
        String manufacturer = fixedProductsInfo.get(0);
        String category = fixedProductsInfo.get(1);
        String model = fixedProductsInfo.get(2);
        String productDescription = fixedProductsInfo.get(3) != null ? fixedProductsInfo.get(3) : "";
        String productDetailsTable = fixedProductsInfo.get(4) != null ? fixedProductsInfo.get(4) : "";
        String imageUrl = fixedProductsInfo.get(5) != null ? fixedProductsInfo.get(5) : "";
    
        // fluidProductsInfo에서 각 제품 정보를 처리
        for (List<String> productInfo : fluidProductsInfo) {
            try {
                // 가격과 배송비 파싱 시 예외 처리
                Integer price = parsePrice(productInfo.get(0));  // 가격 파싱
                String seller = productInfo.get(1);  // 판매자 정보
                String url = productInfo.get(2);  // URL
                Integer shoppingCost = parsePrice(productInfo.get(3));  // 배송비 파싱
    
                // Product 객체 생성 후 리스트에 추가
                products.add(new Product(price, manufacturer, model, category, seller, imageUrl, shoppingCost, productDetailsTable, productDescription, url));
            } catch (NumberFormatException e) {
                // 가격이나 배송비 파싱 실패 시 해당 제품은 건너뛰기
                continue;
            }
        }
    
        // 생성된 제품 리스트 반환
        return products;
    }
    
    // 가격을 파싱하는 메서드 (쉼표 제거 및 예외 처리)
    private Integer parsePrice(String priceStr) {
        if (priceStr == null || priceStr.isEmpty()) {
            return 0;  // 빈 값이나 null일 경우 0 반환
        }
        try {
            return Integer.parseInt(priceStr.replace(",", ""));  // 쉼표를 제거하고 정수로 변환
        } catch (NumberFormatException e) {
            return 0;  // 변환 실패 시 0 반환
        }
    }
    
}
