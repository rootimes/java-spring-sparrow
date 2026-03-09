package sparrow;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import sparrow.controller.ApiController;

public class AppTest extends RestDocsTest {
    @Configuration
    @EnableWebMvc
    @ComponentScan(basePackageClasses = ApiController.class)
    static class TestConfig {
    }

    @Test
    public void documentGetStatusApi() throws Exception {
        this.mockMvc.perform(get("/api/status"))
                .andExpect(status().isOk())
                .andDo(document("status-get",
                        responseFields(
                                fieldWithPath("status").description("API 處理狀態"),
                                fieldWithPath("message").description("詳細回應訊息"))));
    }
}
