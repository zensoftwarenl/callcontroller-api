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

package zen.cybercloud.callcontroller.firestore;
import com.google.cloud.firestore.annotation.DocumentId;
import org.springframework.cloud.gcp.data.firestore.Document;

import java.util.Objects;

//Not supported (yet) by Spring Data Firestore
//https://github.com/GoogleCloudPlatform/spring-cloud-gcp/issues/1385
// @Document(collectionName = "#{callControllerUserCollectionName}")
@Document(collectionName = "CallControllerUser")
public class CallControllerUser {

    @DocumentId
    private String id;

    // full name
    private String username;

    private String hashedPassword;

    private String email;


    // getters and setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CallControllerUser other = (CallControllerUser) o;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class Builder {
        private String id;
        private String username;
        private String hashedPassword;
        private String email;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }
        public Builder hashedPassword(String hashedPassword) {
            this.hashedPassword = hashedPassword;
            return this;
        }
        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public CallControllerUser build() {
            CallControllerUser callControllerUser = new CallControllerUser();
            callControllerUser.setId(id);
            callControllerUser.setUsername(username);
            callControllerUser.setHashedPassword(hashedPassword);
            callControllerUser.setEmail(email);
            return callControllerUser;
        }
    }
}
