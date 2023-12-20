package my.Mapper;

import my.domain.Product;
import mybatis.annotation.*;

import java.util.List;

public interface ProductMapper {
    @Select("select * from product where PRODUCT_Id>=#{id} ")
    public List<Product> getProductList(@Param("id") String id);


    @Update("update product set PRODUCT_Name =#{name} where PRODUCT_Id=#{z.PRODUCT_Id} ")
    public Integer updateProductById(@Param("z") Product product,@Param("name") String name);

    @Delete("delete from product where PRODUCT_Id=#{id} ")
    public Integer deleteProductById(@Param("id") String id);


    @Insert("insert into  product (PRODUCT_Id,PRODUCT_Name,PRODUCT_Price) VALUES (#{p.PRODUCT_Id},#{p.PRODUCT_Name},#{p.PRODUCT_Price} )")
    public Integer insertProduct(@Param("p") Product product);
}
