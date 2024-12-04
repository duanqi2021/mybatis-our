package my;

import my.Mapper.ProductMapper;
import my.domain.Product;
import mybatis.MapperProxyFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;



public class MyApplication {
    public static void main(String[] args) throws SQLException {
        Connection connection=MapperProxyFactory.getConnection("127.0.0.1","shop","root","duanqi1998");
        ProductMapper productMapper=MapperProxyFactory.getMapper(ProductMapper.class, Product.class,connection);
    //   Product product=new Product();
      //  product.setId(23);
//        product.setName("测试1");
//        product.setPrice(7.1);
//
//        productMapper.insertProduct(product);

        final List<Product> productList = productMapper.getProductList("4");
        System.out.println(productList);
//        System.out.println(test);
 //       System.out.println(productMapper.deleteProductById("1"));
//        Product product=new Product();
//        product.setPRODUCT_Id(1);
//        product.setPRODUCT_Name("ces123");
//        product.setPRODUCT_Price(1.34);
//        System.out.println(productMapper.insertProduct(product));
//        System.out.println(productMapper.updateProductById(product,"测试一下"));

    }
}
