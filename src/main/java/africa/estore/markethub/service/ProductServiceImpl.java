package africa.estore.markethub.service;

import africa.estore.markethub.dto.request.AddProductRequest;
import africa.estore.markethub.dto.response.ProductResponse;
import africa.estore.markethub.model.Product;
import africa.estore.markethub.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    //TODO: do constructor injection
    @Autowired
    private ProductRepository productRepository;
    @Override
    public ProductResponse addProduct(AddProductRequest addProductRequest) {
        ModelMapper modelMapper = new ModelMapper();
        Product product = modelMapper.map(addProductRequest, Product.class);
        //TODO: save product media files
        product = productRepository.save(product);
        return modelMapper.map(product, ProductResponse.class);
    }
}
