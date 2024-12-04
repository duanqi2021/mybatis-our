package my.domain;

import lombok.Data;
import mybatis.annotation.DataName;

@Data
public class Product {

    @DataName("PRODUCT_Id")
    private Integer id;

    @DataName("PRODUCT_Name")
    private String name;


    @DataName("PRODUCT_Price")
    private Double price;
    @Override
    public String toString() {
        return "Product{" +
                "Id='" + id + '\'' +
                ", Name='" + name+ '\'' +",Price='" + price + '\'' +
                '}';
    }
}
