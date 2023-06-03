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

import zen.cybercloud.callcontroller.firestore.CallControllerUser;
import zen.cybercloud.callcontroller.firestore.CallControllerUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static zen.cybercloud.callcontroller.security.SecurityConstants.SIGN_UP_URL;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthenticationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    private CallControllerUserRepository callControllerUserRepository;

    //to be set by logging in, then re-used for all tests
    private String jwt = null;
    @Value("${contacts.file}")
    private String contactsUrl;

    @BeforeEach
    public void login_to_app() throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        var testUser = new CallControllerUser.Builder().id("test").hashedPassword(passwordEncoder.encode("test")).build();
        Mono<CallControllerUser> saveResult = callControllerUserRepository.save(testUser);

        StepVerifier.create(saveResult)
                .expectNextCount(1)
                .verifyComplete();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(SIGN_UP_URL)
                        .param("username", "test")
                        .param("password", "test")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        jwt = result.getResponse().getContentAsString();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity()) // Enable Spring Security
                .build();
    }

    @Test
    public void testDownloadFile() throws Exception {

        Pattern pattern = Pattern.compile("^gs://([^/]+)/(.*)$");
        Matcher matcher = pattern.matcher(contactsUrl);

        Assertions.assertThat(matcher.find()).isTrue();
        String bucketName = matcher.group(1);
        String objectName = matcher.group(2);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(String.format("/storage/%s/%s",bucketName,objectName))
                        .header("Authorization", "Bearer " + jwt)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = Objects.requireNonNull(result.getResponse().getContentAsString());
        assert content.startsWith("First Name,Middle Name,Last Name,Title,Suffix,Initials,Web Page,Gender,Birthday,Anniversary,Location,Language,Internet Free Busy");
    }
}