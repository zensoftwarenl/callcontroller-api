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

import com.google.cloud.firestore.Firestore;
import zen.cybercloud.callcontroller.firestore.CallControllerUser;
import zen.cybercloud.callcontroller.firestore.CallControllerUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class CallControllerUserRepositoryTests {

    @Autowired
    private CallControllerUserRepository repository;

    @Autowired
    private Firestore  firestore;

    //Someday this can be replaced with a property from application.properties
    @Value("CallControllerUser")
    private String collectionName;

    @BeforeEach
    public void setup() {
        // Clear the collection before each test
        firestore.collection(collectionName).listDocuments().forEach(documentReference -> documentReference.delete());
    }
//
//    @AfterEach
//    public void cleanup() {
//        // Clean up any remaining documents after all tests
//        firestore.collection("callControllerUser").listDocuments().forEach(documentReference -> documentReference.delete());
//    }

    @Test
    public void testSave() {
        CallControllerUser user = new CallControllerUser();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("john.doe");
        user.setEmail("john@example.com");

        Mono<CallControllerUser> saveResult = repository.save(user);

        StepVerifier.create(saveResult)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void testFindById() {
        CallControllerUser user = new CallControllerUser();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("john.doe");
        user.setEmail("john@example.com");

        Mono<CallControllerUser> saveResult = repository.save(user);

        Mono<CallControllerUser> findResult = saveResult.flatMap(savedUser -> repository.findById(savedUser.getId()));

        StepVerifier.create(findResult)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    public void testUpdate() {
        CallControllerUser user = new CallControllerUser();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("john.doe");
        user.setEmail("john@example.com");

        Mono<CallControllerUser> saveResult = repository.save(user);

        Mono<CallControllerUser> updateResult = saveResult.flatMap(savedUser -> {
            savedUser.setEmail("updated@example.com");
            return repository.save(savedUser);
        });

        StepVerifier.create(updateResult)
                .expectNextCount(1)
                .verifyComplete();

        Mono<CallControllerUser> findResult = updateResult.flatMap(updatedUser -> repository.findById(updatedUser.getId()));

        StepVerifier.create(findResult)
                .expectNextMatches(foundUser -> foundUser.getEmail().equals("updated@example.com"))
                .verifyComplete();
    }

    @Test
    @Disabled
    public void testDelete() {
        CallControllerUser user = new CallControllerUser();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("john.doe");
        user.setEmail("john@example.com");

        Mono<CallControllerUser> saveResult = repository.save(user);

        Mono<Void> deleteResult = saveResult.flatMap(savedUser -> repository.deleteById(savedUser.getId()));

        StepVerifier.create(deleteResult)
                .verifyComplete();

        Mono<CallControllerUser> findResult = saveResult.flatMap(savedUser -> repository.findById(savedUser.getId()));

        StepVerifier.create(findResult)
                .verifyComplete();
    }
}
