package com.perepalacin.product_service.controller;

import com.perepalacin.product_service.entity.Product;
import com.perepalacin.product_service.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String mechanism,
            @RequestParam(required = false) Integer diameter,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort // e.g., "name,asc", "price,desc"
    ) {
        Sort sorting = Sort.by(sort[0].split(",")[0]);
        if (sort.length > 1 && sort[1].equalsIgnoreCase("desc")) {
            sorting = sorting.descending();
        } else {
            sorting = sorting.ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sorting);

        Page<Product> products = productService.getFilteredProducts(
                brand, mechanism, diameter, minPrice, maxPrice, keyword, pageable
        );
        return ResponseEntity.ok(products);
    }

    @GetMapping("/url/{publicUrl}")
    public ResponseEntity<Product> getProductByPublicUrl(@PathVariable(name="publicUrl") String publicUrl) {
        Product product = productService.getProductByPublicUrl(publicUrl);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable(name="id") Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Product>> getProductsBatch(@RequestBody List<Long> productIds) {
        List<Product> products = productService.findProductsByIds(productIds);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/admin")
    public ResponseEntity<Product> createNewProduct(@RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product));
    }

    @PatchMapping("/admin/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.editProduct(product, productId));
    }

    @DeleteMapping("/admin/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}

