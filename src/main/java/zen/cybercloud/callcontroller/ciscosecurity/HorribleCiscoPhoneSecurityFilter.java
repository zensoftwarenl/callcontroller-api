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

package zen.cybercloud.callcontroller.ciscosecurity;

import zen.cybercloud.callcontroller.ciscoxml.CiscoError;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HorribleCiscoPhoneSecurityFilter extends OncePerRequestFilter {

    private final HorribleCiscoPhoneSecurityProvider provider;

    private final  List<HttpMessageConverter<?>> messageConverters;

    private final static MediaType MY_MEDIA_TYPE = new MediaType("application", "xml", StandardCharsets.UTF_8);

    public HorribleCiscoPhoneSecurityFilter(HorribleCiscoPhoneSecurityProvider provider,
                                            List<HttpMessageConverter<?>> messageConverters) {

        this.provider = provider;
        this.messageConverters = messageConverters;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws IOException, ServletException {
        try {
            // FirewalledRequest[ Request(GET //192.168.1.144:8080/cisco/index?locale=English_United_States&name=SEPE8046212907A)@b3c2656]
            String sepName = request.getParameter("name");
            String locale = request.getParameter("locale");
            String userAgent = request.getHeader("User-Agent").split("/")[0].toLowerCase();
            String sourceIp = request.getRemoteAddr();
            HorribleCiscoPhoneSecurityToken token = new HorribleCiscoPhoneSecurityToken(userAgent, sourceIp, sepName, locale);
            Authentication authentication = provider.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            for (HttpMessageConverter<?> converter : messageConverters) {
                if (converter.getSupportedMediaTypes().contains(MY_MEDIA_TYPE)) {
                    response.setHeader("Content-Type", MediaType.APPLICATION_XML_VALUE);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    HttpMessageConverter<Object> messageConverter = (HttpMessageConverter<Object>) converter;
                    HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
                    messageConverter.write(new CiscoError(5, e.getMessage()), MY_MEDIA_TYPE, outputMessage);
                    outputMessage.getBody().flush();
                    return;
                }
            }
            throw new ServletException(e);
        }
    }
}
