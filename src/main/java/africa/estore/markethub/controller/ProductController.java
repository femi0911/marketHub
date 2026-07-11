package africa.estore.markethub.controller;

import africa.estore.markethub.dto.request.AddProductRequest;
import africa.estore.markethub.dto.response.ProductResponse;
import africa.estore.markethub.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse addProduct(@ModelAttribute AddProductRequest addProductRequest) {
        return productService.addProduct(addProductRequest);
    }
}
