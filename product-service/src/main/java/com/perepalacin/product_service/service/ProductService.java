package com.perepalacin.product_service.service;

import com.perepalacin.product_service.client.RestockServiceClient;
import com.perepalacin.product_service.entity.Product;
import com.perepalacin.product_service.entity.RestockEvent;
import com.perepalacin.product_service.exceptions.NotFoundException;
import com.perepalacin.product_service.helpers.ProductSpecification;
import com.perepalacin.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final KafkaTemplate<String, RestockEvent> kafkaTemplate;
    private final RestockServiceClient restockServiceClient;

    public Page<Product> getFilteredProducts(
            List<String> brands,
            List<String> mechanisms,
            Integer minDiameter,
            Integer maxDiameter,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String keyword,
            Pageable pageable) {

        Specification<Product> spec = Specification.where(null);

        if (brands != null && !brands.isEmpty()) {
            spec = spec.and(ProductSpecification.hasAnyBrand(brands));
        }
        if (mechanisms != null && !mechanisms.isEmpty()) {
            spec = spec.and(ProductSpecification.hasAnyMechanism(mechanisms));
        }
        if (minDiameter != null || maxDiameter != null) {
            spec = spec.and(ProductSpecification.diameterBetween(minDiameter, maxDiameter));
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

    public Product createProduct(final Product product) {
        return productRepository.save(product);
    }

    public Product editProduct(Product product, Long productId) {
        Product alreadyExistingProduct = productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException("Product with id: " + productId + ", couldn't be found")
        );
        if (alreadyExistingProduct.getStock() == 0 && product.getStock() != 0) {
            try {
                ResponseEntity<List<String>> response = restockServiceClient.getListOfUsersSubscribedToProductRestock(productId);
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    RestockEvent restockEvent = RestockEvent.builder()
                            .emailList(response.getBody())
                            .product(product)
                            .build();
                    kafkaTemplate.send("inventory-events", restockEvent);
                    log.info("Restock event sent to kafka: {}", restockEvent);
                } else {
                    throw new NotFoundException("We couldn't find the product with id: " + product + " please try again later.");
                }
            } catch (HttpClientErrorException.NotFound e) {
                throw new NotFoundException("We couldn't find the product with id: " + product + " please try again later." + e.getMessage());
            } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
                throw new RuntimeException("Error fetching users subscribed to this product. Server response was: " + e.getMessage(), e);
            }
        }
        product.setId(productId);
        return productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
        productRepository.delete(Product.builder().id(productId).build());
    }
}
