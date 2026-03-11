package sparrow.integration;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

public class AppTest extends RestDocsTest {
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
