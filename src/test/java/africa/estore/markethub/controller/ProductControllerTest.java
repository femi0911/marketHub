package africa.estore.markethub.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static africa.estore.markethub.util.TestUtils.getTestMediaFilesForUpload;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    @DisplayName(
            """
            Given:
            I have the details of a new product,
            including its media files.
            When:
            I send a multipart request to the
            add product endpoint.
            Then:
            The product is saved, and a response
            containing the product details and the
            urls of its uploaded media files, is
            returned with a 201 status.
            """
    )
    public void testCanAddProduct() throws Exception {
        MockMultipartHttpServletRequestBuilder request = multipart("/api/v1/products");
        getTestMediaFilesForUpload().forEach((file)->request.file(file));
        mockMvc.perform(request
                        .param("name", "mobile phone")
                        .param("description", "an iphone 18 pro max")
                        .param("category", "Gadget")
                        .param("price", "100000")
                        .param("quantity", "10")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("mobile phone"))
                .andExpect(jsonPath("$.description").value("an iphone 18 pro max"))
                .andExpect(jsonPath("$.category").value("Gadget"))
                .andExpect(jsonPath("$.price").value(100000))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.productMediaFiles", hasSize(2)))
                .andExpect(jsonPath("$.productMediaFiles[0]", containsString("cloudinary")))
                .andExpect(jsonPath("$.productMediaFiles[1]", containsString("cloudinary")));
    }
}
