package comgb.comgabi.danawa_crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class ProductURLCrawler {
    public List<String> productURLCrawler(String query) {
        try {
            List<String> urlList = new ArrayList<>();
            int pageNum = 1;
            Document doc = Jsoup
                    .connect("https://search.danawa.com/dsearch.php?query=" + query + "&page=" + pageNum + "&limit=40")
                    .timeout(5000)
                    .get();

            Elements contents = doc
                    .select(".prod_main_info > .thumb_image > :first-child");
            

            for (Element content : contents) {
                String href = content.attr("href");
                urlList.add(href);
            }
            return urlList;
        } catch (IOException e) {
            throw new RuntimeException("Failed to crawling", e);
        }
    }
}
