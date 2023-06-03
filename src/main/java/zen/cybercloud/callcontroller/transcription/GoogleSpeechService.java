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

package zen.cybercloud.callcontroller.transcription;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.speech.v1.LongRunningRecognizeMetadata;
import com.google.cloud.speech.v1.LongRunningRecognizeRequest;
import com.google.cloud.speech.v1.LongRunningRecognizeResponse;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.cloud.speech.v1.SpeechSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
@Service
public class GoogleSpeechService {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleSpeechService.class);

    @Value("${google.cloud.projectId}")
    private String projectId;

    @Async
    public CompletableFuture<String> transcribeAudio(String uri) throws IOException {
        return transcribeAudio(uri, RecognitionConfig.AudioEncoding.LINEAR16, 8000, "nl-NL");
    }

    @Autowired
    SpeechSettings speechSettings;

    @Async
    public CompletableFuture<String> transcribeAudio(String uri, RecognitionConfig.AudioEncoding audioEncoding, int sampleRate, String languageCode) throws IOException {

        try (SpeechClient speechClient = SpeechClient.create(speechSettings)) {

            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(audioEncoding)
                    .setSampleRateHertz(sampleRate)
                    .setLanguageCode(languageCode)
                    .build();

            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setUri(uri)
                    .build();

            LongRunningRecognizeRequest request = LongRunningRecognizeRequest.newBuilder()
                    .setConfig(config)
                    .setAudio(audio)
                    .build();


            OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> responseFuture = speechClient.longRunningRecognizeAsync(request);
            // wait for the transcript to finish
            var response = responseFuture.get();
            StringBuilder transcript = new StringBuilder();
            for (SpeechRecognitionResult result : response.getResultsList()) {
                SpeechRecognitionAlternative alternative = result.getAlternatives(0);
                transcript.append(alternative.getTranscript());
            }
            return CompletableFuture.completedFuture(transcript.toString());

        } catch (InterruptedException | ExecutionException e) {
            LOG.error("Error transcribing audio", new Throwable(e));
            // Restore interrupted state...
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}