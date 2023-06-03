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

import com.google.cloud.speech.v1.RecognitionConfig;
import zen.cybercloud.callcontroller.storage.GoogleCloudStorageService;
import zen.cybercloud.callcontroller.transcription.GoogleSpeechService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SpringBootTest
@AutoConfigureMockMvc
public class TranscriptionTests {

    @Autowired
    GoogleSpeechService googleSpeechService;

    @Autowired
    GoogleCloudStorageService googleCloudStorageService;

    @Value("${test.audio.transcription.file}")
    String testAudioTranscriptionFile;
    @Test
    void async_transcription() {

        try {
            CompletableFuture<String> transcription = googleSpeechService.transcribeAudio(testAudioTranscriptionFile,
                    RecognitionConfig.AudioEncoding.LINEAR16, 24000,"en-GB");

            System.out.println("Waiting for transcription to complete...");
            while (!transcription.isDone()) {
                Thread.sleep(1000);
            }
            try {
                // Get the result of the computation (blocking call)
                String transcript = transcription.get();
                Assertions.assertEquals(transcript, "Google Cloud text to speech enables developers to synthesise natural sounding speech with 100plus voices available in multiple languages and variance it supplies deepmind groundbreaking Google powerful neural networks to deliver the highest fidelity possible as an easy-to-use API you can create lifelike interactions with your users across many applications and devices");
                System.out.println("Transcription result: " + transcript);
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Error executing the computation: " + e.getMessage());
                Assertions.fail();
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error executing the computation: " + e.getMessage());
            Assertions.fail();
        }
    }
}
