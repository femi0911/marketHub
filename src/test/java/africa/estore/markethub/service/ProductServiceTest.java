package africa.estore.markethub.service;

import africa.estore.markethub.dto.request.AddProductRequest;
import africa.estore.markethub.dto.response.ProductResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


public class ProductServiceTest {


    private ProductService productService;

    @Test
    public void testCanCreateProductSuccessfully() {
        productService = Mockito.mock(ProductServiceImpl.class);
        //given
        AddProductRequest addProductRequest = new AddProductRequest();
        addProductRequest.setName("mobile phone");
        addProductRequest.setDescription("an iphone 18 pro max");
        addProductRequest.setCategory("Gadget");
        addProductRequest.setPrice(new BigDecimal("100000"));
        //when
        Mockito.when()
        ProductResponse response = productService.addProduct(addProductRequest);
        //check
        assertThat(response).isNotNull();
        assertThat(response.getCategory()).isEqualTo("Gadget");
        assertThat(response.getDescription()).isEqualTo("an iphone 18 pro max");
        assertThat(response.getPrice()).isEqualTo(new BigDecimal("100000"));
    }
}
