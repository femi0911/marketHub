package africa.estore.markethub.service;

import africa.estore.markethub.dto.request.AddProductRequest;
import africa.estore.markethub.dto.request.UpdateProductRequest;
import africa.estore.markethub.dto.response.ProductResponse;
import africa.estore.markethub.exception.ProductNotFoundException;
import africa.estore.markethub.integration.cloud.CloudService;
import africa.estore.markethub.model.Product;
import africa.estore.markethub.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private  final ModelMapper modelMapper;
    private final CloudService cloudService;


    @Override
    public ProductResponse addProduct(AddProductRequest addProductRequest) {
        Product product = modelMapper.map(addProductRequest, Product.class);
        if (addProductRequest.getProductMediaFiles() != null &&
                !addProductRequest.getProductMediaFiles().isEmpty()) {
            List<String> mediaUrls = cloudService.upload(addProductRequest.getProductMediaFiles());
            product.setProductMediaFiles(mediaUrls);
        }
        product = productRepository.save(product);
        return modelMapper.map(product, ProductResponse.class);
    }

    @Override
    public ProductResponse findProductById(String id) {
        Product product = getProductById(id);
        return modelMapper.map(product, ProductResponse.class);
    }

    @Override
    public ProductResponse updateProduct(String id, UpdateProductRequest updateProductRequest) {
        Product product = getProductById(id);
        modelMapper.map(updateProductRequest, product);
        //TODO: update product media files
        product = productRepository.save(product);
        return modelMapper.map(product, ProductResponse.class);
    }

    @Override
    public void deleteProduct(String id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    private Product getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
