package africa.estore.markethub.service;

import africa.estore.markethub.dto.request.AddProductRequest;
import africa.estore.markethub.dto.request.UpdateProductRequest;
import africa.estore.markethub.dto.response.ProductResponse;
import africa.estore.markethub.exception.ProductNotFoundException;
import africa.estore.markethub.model.Product;
import africa.estore.markethub.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static africa.estore.markethub.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    public void testCanCreateProductSuccessfully() {
        AddProductRequest addProductRequest = buildTestProductRequest();
        Product product = buildMockTestProduct();
        Mockito.when(modelMapper.map(addProductRequest, Product.class)).thenReturn(product);
        Mockito.when(productRepository.save(product)).thenReturn(product);
        Mockito.when(modelMapper.map(product, ProductResponse.class)).thenReturn(buildTestProductResponse());
        ProductResponse response = productService.addProduct(addProductRequest);
        assertThat(response).isNotNull();
        checkProductAdded(response);
    }

    @Test
    public void testCanFindProductById() {
        String id = UUID.randomUUID().toString();
        Product product = buildMockTestProduct();
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));
        Mockito.when(modelMapper.map(product, ProductResponse.class)).thenReturn(buildTestProductResponse());
        ProductResponse response = productService.findProductById(id);
        assertThat(response).isNotNull();
        checkProductAdded(response);
    }

    @Test
    public void testFindProductByIdThrowsWhenProductDoesNotExist() {
        String id = UUID.randomUUID().toString();
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> productService.findProductById(id))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(id);
    }

    @Test
    public void testCanUpdateProduct() {
        String id = UUID.randomUUID().toString();
        Product product = buildMockTestProduct();
        UpdateProductRequest updateProductRequest = new UpdateProductRequest();
        updateProductRequest.setPrice(new BigDecimal("100000"));
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));
        Mockito.doNothing().when(modelMapper).map(updateProductRequest, product);
        Mockito.when(productRepository.save(product)).thenReturn(product);
        Mockito.when(modelMapper.map(product, ProductResponse.class)).thenReturn(buildTestProductResponse());
        ProductResponse response = productService.updateProduct(id, updateProductRequest);
        assertThat(response).isNotNull();
        Mockito.verify(modelMapper).map(updateProductRequest, product);
        Mockito.verify(productRepository).save(product);
        assertThat(response.getPrice()).isEqualTo(new BigDecimal("100000"));
    }

    @Test
    public void testUpdateProductThrowsWhenProductDoesNotExist() {
        String id = UUID.randomUUID().toString();
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> productService.updateProduct(id, new UpdateProductRequest()))
                .isInstanceOf(ProductNotFoundException.class);
        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void testCanDeleteProduct() {
        String id = UUID.randomUUID().toString();
        Product product = buildMockTestProduct();
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));
        productService.deleteProduct(id);
        Mockito.verify(productRepository).delete(product);
    }

    @Test
    public void testDeleteProductThrowsWhenProductDoesNotExist() {
        String id = UUID.randomUUID().toString();
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> productService.deleteProduct(id))
                .isInstanceOf(ProductNotFoundException.class);
        Mockito.verify(productRepository, Mockito.never()).delete(Mockito.any());
    }

    private static void checkProductAdded(ProductResponse response) {
        assertThat(response.getCategory()).isEqualTo("Gadget");
        assertThat(response.getDescription()).isEqualTo("an iphone 18 pro max");
        assertThat(response.getPrice()).isEqualTo(new BigDecimal("100000"));
    }


}
