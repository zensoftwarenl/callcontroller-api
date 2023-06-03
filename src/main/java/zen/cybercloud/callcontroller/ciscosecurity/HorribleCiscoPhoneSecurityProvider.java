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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class HorribleCiscoPhoneSecurityProvider implements AuthenticationProvider {

    private final Set<String> allowedUserAgents;
    private final Set<String> allowedSourceIps;
    private final Pattern allowedPhones;

    public HorribleCiscoPhoneSecurityProvider(@Value("${allowedUserAgents}") Set<String> allowedUserAgents,
                                              @Value("${allowedSourceIps}") Set<String> allowedSourceIps,
                                              @Value("${allowedPhonesRegEx}") String allowedPhonesRegEx) {
        this.allowedUserAgents = allowedUserAgents;
        this.allowedSourceIps = allowedSourceIps;
        this.allowedPhones = Pattern.compile(allowedPhonesRegEx);
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        HorribleCiscoPhoneSecurityToken token = (HorribleCiscoPhoneSecurityToken) authentication;
        if ((token == null) || (token.getUserAgent() ==null ) || (!allowedUserAgents.contains(token.getUserAgent()))) {
            throw new BadCredentialsException("You are not a phone");
        }
        if ((token.getSourceIp() == null) ||!allowedSourceIps.contains(token.getSourceIp())) {
            throw new BadCredentialsException("You are not from a phoney location");
        }
        String sepName = token.getSepName();
        if (sepName == null) {
            sepName = "Unknown";
        }
        // Uncomment this to require a SEP name to be set on the phone
//        if((token.getSepName() == null) || (!allowedPhones.matcher(token.getSepName()).matches())) {
//            throw new BadCredentialsException("You are a phoney phone");
//        }
        return new UsernamePasswordAuthenticationToken(sepName, token.getSourceIp(), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return HorribleCiscoPhoneSecurityToken.class.isAssignableFrom(authentication);
    }
}