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

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

import static zen.cybercloud.callcontroller.security.SecurityConstants.EXPIRATION_TIME;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@SqlGroup({
//        @Sql(scripts = "/db/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
//        @Sql(scripts = "/db/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//})
public class CloudStorageControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Value("${callcontroller.signingSecret}")
    String signingSecret;
    private final String BUCKET_AND_FILE_NAME = URLEncoder.encode("callcontroller-test-bucket",StandardCharsets.UTF_8)
                                                + "/" + URLEncoder.encode("test.txt",StandardCharsets.UTF_8);
    private final String ENDPOINT_NAME = "/storage/";

    private String jwt;

    @BeforeEach
    public void setup() {
        Algorithm algorithm = Algorithm.HMAC256(signingSecret);
        this.jwt = JWT.create()
                .withSubject("test")
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(signingSecret.getBytes()));
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity()) // Enable Spring Security
                .build();
    }
    @Test
    @Order(1)
    public void testUploadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(MockMvcRequestBuilders.multipart(ENDPOINT_NAME + BUCKET_AND_FILE_NAME)
                .file(file).header("Authorization", "Bearer " + jwt)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @Order(2)
    public void testDownloadFile() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_NAME + BUCKET_AND_FILE_NAME)
                        .header("Authorization", "Bearer " + jwt)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = Objects.requireNonNull(result.getResponse().getContentAsString());
        assert content.equals("Hello, World!");
    }

    @Test
    @Order(3)
    public void testDoesFileExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_NAME + BUCKET_AND_FILE_NAME)
                        .header("Authorization", "Bearer " + jwt)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Order(4)
    public void testMoveFile() throws Exception {
       String MOVE_BUCKET_AND_FILE_NAME = "/move/"
                + URLEncoder.encode("callcontroller-test-bucket",StandardCharsets.UTF_8)
                + "/" + URLEncoder.encode("test2.txt",StandardCharsets.UTF_8);
        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_NAME + BUCKET_AND_FILE_NAME + MOVE_BUCKET_AND_FILE_NAME)
                        .header("Authorization", "Bearer " + jwt)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(5)
    public void testDeleteFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(ENDPOINT_NAME + BUCKET_AND_FILE_NAME)
                        .header("Authorization", "Bearer " + jwt)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
