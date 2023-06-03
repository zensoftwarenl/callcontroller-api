/*
 * MIT License
 *
 * Copyright (c) 2023. ZEN Software B.V.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package zen.cybercloud.callcontroller.tests;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OpenApiGeneratorTests {

    @Autowired
    private MockMvc mockMvc;

//    @Test
//    @Disabled
//    // this converter caused the api generation to interfere with XML (new MappingJackson2HttpMessageConverter();)
//    // unittest used to debug the issue
//    public void bug_whenConverterNotFound_thenThrowException() throws Exception {
//        String url = "/v3/api-docs/cisco";
//        this.mockMvc.perform(get(url))
//                    .andExpect(status().isInternalServerError())
//                    .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(HttpMessageNotWritableException.class))
//                    .andExpect(result -> assertThat(result.getResolvedException()
//                            .getMessage()).contains("No converter for [class [B] with preset Content-Type 'null'"));
//    }

    @Test
    public void request_swagger_index_unauthorized() throws Exception {
        String url = "/swagger-ui/index.html";
        System.out.println("url: " + url);
        this.mockMvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value="admin123", userDetailsServiceBeanName = "userDetailsService")
    public void request_swagger_index() throws Exception {
        String url = "/swagger-ui/index.html";
        System.out.println("url: " + url);
        this.mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("swagger")));

    }

    @Test
    @WithUserDetails(value="admin123", userDetailsServiceBeanName = "userDetailsService")
    public void request_swagger_config() throws Exception {
        String url = "/v3/api-docs/swagger-config";
        System.out.println("url: " + url);
        this.mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("swagger")));
    }


    @Test
    @WithUserDetails(value="admin123", userDetailsServiceBeanName = "userDetailsService")
    public void request_swagger_api_definition() throws Exception {
        String url = "/v3/api-docs/cisco";
        System.out.println("url: " + url);
        this.mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("openapi")));
    }
}
