package africa.estore.markethub.service;

import africa.estore.markethub.dto.request.AddProductRequest;
import africa.estore.markethub.dto.request.UpdateProductRequest;
import africa.estore.markethub.dto.response.ProductResponse;

public interface ProductService {
    ProductResponse addProduct(AddProductRequest addProductRequest);
    ProductResponse findProductById(String id);
    ProductResponse updateProduct(String id, UpdateProductRequest updateProductRequest);
    void deleteProduct(String id);
}
