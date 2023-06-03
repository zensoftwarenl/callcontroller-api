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

import com.google.api.gax.core.CredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import zen.cybercloud.callcontroller.callerid.CSVReaderHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.gcp.autoconfigure.core.GcpContextAutoConfiguration;
import org.springframework.cloud.gcp.core.GcpProjectIdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@EnableAutoConfiguration(exclude = GcpContextAutoConfiguration.class)
public class GoogleCredentialsConfiguration {

    @Value("${google.cloud.projectId}")
    private String projectId;

    @Bean
    public GcpProjectIdProvider gcpProjectIdProvider() {
        return () -> projectId;
    }

    private static final Logger LOG = LoggerFactory.getLogger(CSVReaderHelper.class);
    @Bean
    public CredentialsProvider credentialsProvider() throws IOException {
        return new CredentialsProvider() {
            @Override
            public Credentials getCredentials() throws IOException {
                return googleCredentials();
            }
        };
    }
    @Bean
    public GoogleCredentials googleCredentials() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

        String serviceAccountKeyPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        if (serviceAccountKeyPath != null) {
            LOG.info(String.format("Using GOOGLE_APPLICATION_CREDENTIALS based credentials from %s", serviceAccountKeyPath));
            InputStream serviceAccount = new FileInputStream(serviceAccountKeyPath);
            // Load the service account credentials from a JSON key file
            credentials = GoogleCredentials.fromStream(serviceAccount);
        }
        return credentials;
    }

}
