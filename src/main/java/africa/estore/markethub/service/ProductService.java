package africa.estore.markethub.service;

import africa.estore.markethub.dto.request.AddProductRequest;
import africa.estore.markethub.dto.response.ProductResponse;

public interface ProductService {
    ProductResponse addProduct(AddProductRequest addProductRequest);
}
