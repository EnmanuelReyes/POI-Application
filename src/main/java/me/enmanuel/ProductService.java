package me.enmanuel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Enmanuel
 * Date: 27/07/2016
 * Time: 05:39 PM
 */
@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<Product> getProducts() {

        return productRepository.getAll();
    }

    public Product save(Product product) {
        if (product.getId() != null) {
            productRepository.update(product);
        } else {
            productRepository.save(product);
        }
        return product;
    }

    public Product getProduct(Integer productId) {
        return productRepository.get(productId);
    }

    public void delete(Product product) {
        productRepository.delete(product);
    }
}
