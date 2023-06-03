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

package zen.cybercloud.callcontroller.configuration;

import zen.cybercloud.callcontroller.ciscoxml.CiscoDirectoryItem;
import zen.cybercloud.callcontroller.ciscoxml.CiscoDirectoryList;
import zen.cybercloud.callcontroller.ciscoxml.CiscoError;
import zen.cybercloud.callcontroller.ciscoxml.CiscoInputText;
import zen.cybercloud.callcontroller.ciscoxml.CiscoMenu;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@EnableWebMvc
@Configuration
public class CiscoXmlWebConfiguration implements WebMvcConfigurer {

    @Autowired
    Jackson2ObjectMapperBuilder jacksonBuilder;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        ////////////////////////////////////////////////////////////////
        // Quite some trouble with MappingJackson2HttpMessageConverter and it's desire for byte[] output
        // https://stackoverflow.com/questions/51951641/swagger-unable-to-render-this-definition-the-provided-definition-does-not-speci
        // using the plain MappingJackson2HttpMessageConverter object causes:
        // breaks openapi: i.e: http://127.0.0.1:8081/actuator/openapi/recordings (makes base64 out of byte[])
        // it does fix: http://127.0.0.1:8081/actuator/swagger-ui/swagger-config (returns Treemap as Json)
        //converters.add(new MappingJackson2HttpMessageConverter(createObjectMapper()));
        // fixed by adding ByteArrayHttpMessageConverter before MappingJackson2HttpMessageConverter and
        // using the jacksonBuilder.
        // alternate solutions like: StringHttpMessageConverter and GsonHttpMessageConverter
        // did not work for either the openapi or swagger editor part
        ////////////////////////////////////////////////////////////////
        // https://springdoc.org/#why-am-i-getting-an-error-swagger-ui-unable-to-render-definition-when-overriding-the-default-spring-registered-httpmessageconverter
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new MappingJackson2HttpMessageConverter(jacksonBuilder.build()));

        // Output XML for Cisco Phones
        converters.add(createXmlHttpMessageConverter());
    }

    private HttpMessageConverter<Object> createXmlHttpMessageConverter() {
        MarshallingHttpMessageConverter xmlConverter = new MarshallingHttpMessageConverter();

        XStreamMarshaller xstreamMarshaller = new XStreamMarshaller();
        //Make sure XStreamAlias annotations are processed
        xstreamMarshaller.getXStream().processAnnotations(new Class[]{  CiscoMenu.class,
                                                                        CiscoDirectoryList.class,
                                                                        CiscoDirectoryItem.class,
                                                                        CiscoInputText.class,
                                                                        CiscoError.class});

        xstreamMarshaller.setMode(XStream.NO_REFERENCES);
        xstreamMarshaller.setAutodetectAnnotations(true);

        // For compatibility with Cisco XML, we need to remove the namespace from the XML
        // inspired by this solve https://stackoverflow.com/questions/11052423/java-xstream-how-to-ignore-some-elements
        xstreamMarshaller.setMapper(new MapperWrapper(xstreamMarshaller.getXStream().getMapper()) {
            @Override
            public String serializedClass(Class type) {
                return super.serializedClass(type).replace("zen.cybercloud.callcontrollers.ciscoxml.", "");
            }
        });
        xmlConverter.setMarshaller(xstreamMarshaller);
        xmlConverter.setUnmarshaller(xstreamMarshaller);
        xmlConverter.setSupportedMediaTypes(List.of(MediaType.APPLICATION_XML, MediaType.TEXT_XML));
        // xmlConverter.setSupportedMediaTypes(List.of(new org.springframework.http.MediaType("text", "xml", StandardCharsets.UTF_8)));
        return xmlConverter;
    }
}
