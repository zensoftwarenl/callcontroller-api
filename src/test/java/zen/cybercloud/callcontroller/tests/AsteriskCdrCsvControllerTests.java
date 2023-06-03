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
import zen.cybercloud.callcontroller.security.SecurityConstants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static zen.cybercloud.callcontroller.security.SecurityConstants.EXPIRATION_TIME;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AsteriskCdrCsvControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    SecurityConstants securityConstants;

    @Value("${callcontroller.signingSecret}")
    String signingSecret;
    private String jwt;

    @BeforeAll
    public void setup() {
        Algorithm algorithm = Algorithm.HMAC256(signingSecret);
        this.jwt = JWT.create()
                .withSubject("test")
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(signingSecret.getBytes()));
    }

    @Test
    public void parse_csv_master_file() throws Exception {
        this.mockMvc.perform(get("/recordings/csv").header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"accountcode\":\"\\\"\\\"\\t\\t\\t\",\"src\":\"\\\"mac\\\"\\t\",\"dst\":\"\\\"1\\\"\\t\",\"dcontext\":\"\\\"local_phone\\\"\\t\",\"clid\":\"\\\"\\\"\\\"Arjan Franzen\\\"\\\" <mac>\\\"\\t\",\"channel\":\"\\\"SIP/mac-00000001\\\"\\t\",\"dstchannel\":\"\\\"SIP/mac-00000002\\\"\\t\",\"lastapp\":\"\\\"Dial\\\"\\t\\t\",\"lastdata\":\"\\\"SIP/mac,20\\\"\\t\",\"start\":null,\"answer\":null,\"end\":null,\"duration\":3,\"billsec\":0,\"disposition\":\"\\\"BUSY\\\"\\t\\t\\t\",\"amaflags\":\"\\\"DOCUMENTATION\\\"\",\"userfield\":\"\\\"\\\"\",\"uniqueid\":\"\\\"1340449656.1\\\"\\t\"},{\"accountcode\":\"\\\"\\\"\",\"src\":\"\\\"cisco7970\\\"\",\"dst\":\"\\\"2\\\"\",\"dcontext\":\"\\\"local_phone\\\"\",\"clid\":\"\\\"\\\"\\\"ext-2\\\"\\\" <cisco7970>\\\"\",\"channel\":\"\\\"SIP/cisco7970-00000000\\\"\",\"dstchannel\":\"\\\"SIP/cisco7970-00000001\\\"\",\"lastapp\":\"\\\"Dial\\\"\",\"lastdata\":\"\\\"SIP/cisco7970,20\\\"\",\"start\":\"2012-06-23T11:30:03.000+00:00\",\"answer\":null,\"end\":\"2012-06-23T11:30:08.000+00:00\",\"duration\":5,\"billsec\":0,\"disposition\":\"\\\"NO ANSWER\\\"\",\"amaflags\":\"\\\"DOCUMENTATION\\\"\",\"userfield\":\"\\\"\\\"\",\"uniqueid\":\"\\\"1340451003.0\\\"\"},{\"accountcode\":\"\\\"\\\"\",\"src\":\"\\\"cisco7970\\\"\",\"dst\":\"\\\"1\\\"\",\"dcontext\":\"\\\"local_phone\\\"\",\"clid\":\"\\\"\\\"\\\"ext-2\\\"\\\" <cisco7970>\\\"\",\"channel\":\"\\\"SIP/cisco7970-00000002\\\"\",\"dstchannel\":\"\\\"SIP/mac-00000003\\\"\",\"lastapp\":\"\\\"Dial\\\"\",\"lastdata\":\"\\\"SIP/mac,20\\\"\",\"start\":\"2012-06-23T11:30:22.000+00:00\",\"answer\":\"2012-06-23T11:30:25.000+00:00\",\"end\":\"2012-06-23T11:30:27.000+00:00\",\"duration\":5,\"billsec\":2,\"disposition\":\"\\\"ANSWERED\\\"\",\"amaflags\":\"\\\"DOCUMENTATION\\\"\",\"userfield\":\"\\\"\\\"\",\"uniqueid\":\"\\\"1340451022.2\\\"\"},{\"accountcode\":\"\\\"\\\"\",\"src\":\"\\\"mac\\\"\",\"dst\":\"\\\"1\\\"\",\"dcontext\":\"\\\"local_phone\\\"\",\"clid\":\"\\\"\\\"\\\"Arjan Franzen\\\"\\\" <mac>\\\"\",\"channel\":\"\\\"SIP/mac-00000004\\\"\",\"dstchannel\":\"\\\"SIP/mac-00000005\\\"\",\"lastapp\":\"\\\"Dial\\\"\",\"lastdata\":\"\\\"SIP/mac,20\\\"\",\"start\":\"2012-06-23T17:24:07.000+00:00\",\"answer\":null,\"end\":\"2012-06-23T17:24:09.000+00:00\",\"duration\":2,\"billsec\":0,\"disposition\":\"\\\"BUSY\\\"\",\"amaflags\":\"\\\"DOCUMENTATION\\\"\",\"userfield\":\"\\\"\\\"\",\"uniqueid\":\"\\\"1340472247.4\\\"\"},{\"accountcode\":\"\\\"\\\"\",\"src\":\"\\\"mac\\\"\",\"dst\":\"\\\"2\\\"\",\"dcontext\":\"\\\"local_phone\\\"\",\"clid\":\"\\\"\\\"\\\"Arjan Franzen\\\"\\\" <mac>\\\"\",\"channel\":\"\\\"SIP/mac-00000006\\\"\",\"dstchannel\":\"\\\"SIP/cisco7970-00000007\\\"\",\"lastapp\":\"\\\"Dial\\\"\",\"lastdata\":\"\\\"SIP/cisco7970,20\\\"\",\"start\":\"2012-06-23T17:24:14.000+00:00\",\"answer\":null,\"end\":\"2012-06-23T17:24:17.000+00:00\",\"duration\":3,\"billsec\":0,\"disposition\":\"\\\"NO ANSWER\\\"\",\"amaflags\":\"\\\"DOCUMENTATION\\\"\",\"userfield\":\"\\\"\\\"\",\"uniqueid\":\"\\\"1340472254.6\\\"\"}]"));
    }
}