package africa.estore.markethub.service;

import africa.estore.markethub.dto.request.AddProductRequest;
import africa.estore.markethub.dto.response.ProductResponse;
import africa.estore.markethub.model.Product;
import africa.estore.markethub.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    public void testCanCreateProductSuccessfully1() {
        AddProductRequest addProductRequest = buildTestProductRequest();
        Product product = buildMockTestProduct();
        Mockito.when(modelMapper.map(addProductRequest, Product.class)).thenReturn(product);
        Mockito.when(productRepository.save(product)).thenReturn(product);
        Mockito.when(modelMapper.map(product, ProductResponse.class)).thenReturn(buildTestProductResponse());
        ProductResponse response = productService.addProduct(addProductRequest);
        assertThat(response).isNotNull();
        assertThat(response.getCategory()).isEqualTo("Gadget");
        assertThat(response.getDescription()).isEqualTo("an iphone 18 pro max");
        assertThat(response.getPrice()).isEqualTo(new BigDecimal("100000"));
    }

    private static ProductResponse buildTestProductResponse() {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setCategory("Gadget");
        productResponse.setDescription("an iphone 18 pro max");
        productResponse.setPrice(new BigDecimal("100000"));
        return productResponse;
    }

    private static Product buildMockTestProduct() {
        Product product = new Product();
        product.setCategory("Gadget");
        product.setDescription("an iphone 18 pro max");
        product.setPrice(new BigDecimal("100000"));
        return product;
    }

    private static AddProductRequest buildTestProductRequest() {
        AddProductRequest addProductRequest = new AddProductRequest();
        addProductRequest.setName("mobile phone");
        addProductRequest.setDescription("an iphone 18 pro max");
        addProductRequest.setCategory("Gadget");
        addProductRequest.setPrice(new BigDecimal("100000"));
        return addProductRequest;
    }
}
