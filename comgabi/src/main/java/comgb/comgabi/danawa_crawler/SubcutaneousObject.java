package comgb.comgabi.danawa_crawler;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

@Component
public class SubcutaneousObject {

    public List<List<String>> fluidProductInformationCrawler(String url) {
            try {
                List<List<String>> fluidProductInformations = new ArrayList<>();
                List<String> priceList = new ArrayList<>();
                List<String> sellerList = new ArrayList<>();
                List<String> urlList = new ArrayList<>();
                List<String> shoppingCostList = new ArrayList<>();
    
                // Selenium WebDriver 설정
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless"); // UI 없이 실행 (헤드리스 모드)
                WebDriver driver = new ChromeDriver(options);
    
                // JavaScript로 동적 로딩된 페이지에 접근
                driver.get(url);
    
                // 페이지가 완전히 로드될 때까지 대기 (동적 콘텐츠가 로딩될 시간 필요)
                try {
                    Thread.sleep(3000); // 3초 대기
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
    
                // Selenium을 통해 동적으로 로드된 HTML 소스 가져오기
                String pageSource = driver.getPageSource();
    
                Document doc = Jsoup.parse(pageSource);
    
                Elements prices = doc.select(".diff_box > .d_dsc > .prc_line .price em.prc_c");
                for (Element price : prices) {
                    priceList.add(price.text().replace(",", ""));
                }
    
                // "diff_box" 클래스의 하위 태그 중 "d_mall" 클래스 찾기
                Elements seller = doc.select(".diff_box .d_mall");
    
                for (Element mall : seller) {
                    // a 태그 찾기
                    Elements aTags = mall.select("a");
    
                    for (Element aTag : aTags) {
                        // img 태그 포함 여부 확인
                        if (aTag.select("img").size() > 0) {
                            // a 태그의 alt 속성 가져오기
                            String altText = aTag.select("img").attr("alt");
                            sellerList.add(altText);
                        }
    
                        // span 태그 포함 여부 확인
                        Elements spanTags = aTag.select("span.txt_logo");
                        if (!spanTags.isEmpty()) {
                            // span 태그 내용 가져오기
                            for (Element span : spanTags) {
                                String spanText = span.text();
                                sellerList.add(spanText);
                            }
                        }
                    }
                }
    
                Elements urls = doc.select(".diff_box > .d_buy > a.priceCompareBuyLink");
                for (Element urlContent : urls) {
                    String href = urlContent.attr("href");
                    urlList.add(href);
                }
    
                Elements shoppingCosts = doc.select(".diff_box > .d_dsc > .prc_line > span.ship");
                for (Element shoppingCost : shoppingCosts) {
                    shoppingCostList.add(shoppingCost.text().replace("(", "").replace(")", "").replace("배송비", "")
                            .replace(",", "").replace("원", ""));
                }
    
                fluidProductInformations.add(priceList);
                fluidProductInformations.add(sellerList);
                fluidProductInformations.add(urlList);
                fluidProductInformations.add(shoppingCostList);
    
                return fluidProductInformations;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    
        public List<String> fixedProductInformation(String url) {
    
            List<String> fluidProductInformations = new ArrayList<>();
    
            // Selenium WebDriver 설정
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless"); // UI 없이 실행 (헤드리스 모드)
            WebDriver driver = new ChromeDriver(options);
    
            // JavaScript로 동적 로딩된 페이지에 접근
            driver.get(url);
    
            // 페이지가 완전히 로드될 때까지 대기 (동적 콘텐츠가 로딩될 시간 필요)
            try {
                Thread.sleep(3000); // 3초 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    
            // Selenium을 통해 동적으로 로드된 HTML 소스 가져오기
            String pageSource = driver.getPageSource();
    
            Document doc = Jsoup.parse(pageSource);
    
            Element manufacturer = doc.selectFirst("table.spec_tbl > tbody > :nth-child(1) > :nth-child(2)");
    
            if (manufacturer != null) {
                // 자식 태그가 a 태그인지 확인
                Element aTag = manufacturer.selectFirst("a");
                if (aTag != null) {
                    // a 태그가 있을 경우 :nth-child(1)을 가져옴
                    Element firstChild = manufacturer.selectFirst(":nth-child(1)");
                    if (firstChild != null) {
                        fluidProductInformations.add(firstChild.text());
                    }
                } else {
                    // a 태그가 없을 경우 자신의 내용 가져오기
                    fluidProductInformations.add(manufacturer.text());
                }
            }
    
            Element category = doc.selectFirst("table.spec_tbl > tbody > :nth-child(2) > :nth-child(2) > a");
            fluidProductInformations.add(category.text());
    
            Element model = doc.selectFirst("div.top_summary > h3.prod_tit > span.title");
            fluidProductInformations.add(model.text());
    
            Element productDescription = doc.selectFirst("div#partContents_16_0");
            if(productDescription != null) {
                fluidProductInformations.add(productDescription.html());
            } else {
                fluidProductInformations.add("");
            }
    
            Elements productDetailsTable = doc.select("table.spec_tbl > tbody > :nth-child(n+4)");
            fluidProductInformations.add(productDetailsTable.html());
            
            String productImage = doc.select("img#baseImage").attr("src");
            fluidProductInformations.add(productImage);
            
    
    
            return fluidProductInformations;
        }   
}
