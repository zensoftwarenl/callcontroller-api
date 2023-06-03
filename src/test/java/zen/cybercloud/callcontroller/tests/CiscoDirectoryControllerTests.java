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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test for local or remote service based on the env var
 * "SERVICE_URL". See java/CONTRIBUTING.MD for more information.
 */
//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CiscoDirectoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void request_cisco_index() throws Exception {
        this.mockMvc.perform(get("/cisco/index").header("User-Agent", "Cisco/7950").param("name", "SEP123456789"))
                .andExpect(status().isOk())
                .andExpect(content().xml("<CiscoIPPhoneMenu><title>Franzen PBX Directory</title><prompt>Selecteer een telefoonboek</prompt><MenuItem><Name>Telefoonboek</Name><URL>http://localhost/cisco/directory?query=ALL&amp;sidx=1</URL></MenuItem><MenuItem><Name>Zoeken</Name><URL>http://localhost/cisco/directorysearch</URL></MenuItem></CiscoIPPhoneMenu>"));
    }

    @Test
    public void request_directory_first_page() throws Exception {
        this.mockMvc.perform(get("/cisco/directory?query=ALL&sidx=1").header("User-Agent", "Cisco/7950").param("name", "SEP123456789"))
                .andExpect(status().isOk())
                .andExpect(content().xml("<CiscoIPPhoneDirectory><title>Franzen Telefoonboek</title><prompt>resultaten 1 tot 31 voor: ALL</prompt><DirectoryEntry><Name>TestFirstname  TestLastname(Home Phone)</Name><Telephone>01234567890HOME</Telephone></DirectoryEntry><DirectoryEntry><Name>TestFirstname  TestLastname(Mobile Phone)</Name><Telephone>00987654321CELL</Telephone></DirectoryEntry><SoftKeyItem><Name>Bellen</Name><URL>SoftKey:Dial</URL><Position>1</Position></SoftKeyItem><SoftKeyItem><Name>Bew.Bellen</Name><URL>SoftKey:EditDial</URL><Position>2</Position></SoftKeyItem><SoftKeyItem><Name>Stop</Name><URL>SoftKey:Exit</URL><Position>3</Position></SoftKeyItem></CiscoIPPhoneDirectory>"));
    }

    @Test
    public void request_as_mozzila() throws Exception {
        this.mockMvc.perform(get("/cisco/index").header("User-Agent", "Mozilla/5.0"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().xml("<CiscoError><Message>You are not a phone</Message><Number>5</Number></CiscoError>"));
    }

}
