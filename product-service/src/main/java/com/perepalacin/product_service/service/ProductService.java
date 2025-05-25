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
            String keyword, // For searching by name/description
            Pageable pageable) { // For pagination and sorting

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

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product with id " + id + " not found"));
    }

    public Product getProductByPublicUrl(String publicUrl) {
        return productRepository.findByPublicUrl(publicUrl).orElseThrow(() -> new NotFoundException("Product with public url: " + publicUrl + ", not found"));
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}
