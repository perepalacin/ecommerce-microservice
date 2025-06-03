package com.perepalacin.product_service.service;

import com.perepalacin.product_service.entity.Product;
import com.perepalacin.product_service.exceptions.NotFoundException;
import com.perepalacin.product_service.helpers.ProductSpecification;
import com.perepalacin.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<Product> getFilteredProducts(
            String brand,
            String mechanism,
            Integer diameter,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String keyword,
            Pageable pageable) {

        Specification<Product> spec = Specification.where(null);

        if (brand != null && !brand.isEmpty()) {
            spec = spec.and(ProductSpecification.hasBrand(brand));
        }
        if (mechanism != null && !mechanism.isEmpty()) {
            spec = spec.and(ProductSpecification.hasMechanism(mechanism));
        }
        if (diameter != null) {
            spec = spec.and(ProductSpecification.hasDiameter(diameter));
        }
        if (minPrice != null || maxPrice != null) {
            spec = spec.and(ProductSpecification.priceBetween(minPrice, maxPrice));
        }
        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and(ProductSpecification.containsText(keyword));
        }

        return productRepository.findAll(spec, pageable);
    }

    public Product getProductById(final Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product with id " + id + " not found"));
    }

    public Product getProductByPublicUrl(final String publicUrl) {
        return productRepository.findByPublicUrl(publicUrl).orElseThrow(() -> new NotFoundException("Product with public url: " + publicUrl + ", not found"));
    }

    public List<Product> findProductsByIds (final List<Long> productIds) {
        return productRepository.findByIdIn(productIds);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

}
