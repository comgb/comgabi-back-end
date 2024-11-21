package comgb.comgabi.danawa_crawler.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    private Integer price;
    private String manufacturer;
    private String model;
    private String category;
    private String seller;
    @Column(length = 2083)
    private String imageUrl;
    private Integer shoppingCost;
    @Column(columnDefinition = "LONGTEXT")
    private String productDetailsTable;
    @Column(columnDefinition = "LONGTEXT")
    private String productDescription;
    @Column(length = 2083)
    private String url;

    public Product() {

    }

    public Product(Integer price, String manufacturer, String model, String category, String seller,
            String imageUrl, Integer shoppingCost, String productDetailsTable, String productDescription, String url) {
                this.price = price;
                this.manufacturer = manufacturer;
                this.model = model;
                this.category = category;
                this.seller = seller;
                this.imageUrl = imageUrl;
                this.shoppingCost = shoppingCost;
                this.productDetailsTable = productDetailsTable;
                this.productDescription = productDescription;
                this.url = url;
    }

}
