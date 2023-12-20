package my.domain;

import lombok.Data;

@Data
public class Product {
    private Integer PRODUCT_Id;
    private String PRODUCT_Name;

    private Double PRODUCT_Price;
    @Override
    public String toString() {
        return "Product{" +
                "Id='" + PRODUCT_Id + '\'' +
                ", Name='" + PRODUCT_Name + '\'' +",Price='" + PRODUCT_Price + '\'' +
                '}';
    }
}
