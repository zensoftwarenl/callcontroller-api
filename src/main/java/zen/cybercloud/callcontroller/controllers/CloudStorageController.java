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

package zen.cybercloud.callcontroller.controllers;

import zen.cybercloud.callcontroller.storage.GoogleCloudStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/storage")
public class CloudStorageController {

    private final GoogleCloudStorageService googleCloudStorageService;

    @Autowired
    public CloudStorageController(GoogleCloudStorageService googleCloudStorageService) {
        this.googleCloudStorageService = googleCloudStorageService;
    }

    @PostMapping("/{bucketName}/{objectName}")
    public ResponseEntity<String> uploadFile(
            @PathVariable String bucketName,
            @PathVariable String objectName,
            @RequestParam("file") MultipartFile file) throws IOException {
        googleCloudStorageService.uploadFile(bucketName, objectName, file.getBytes());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{bucketName}/{objectName}")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable String bucketName,
            @PathVariable String objectName) {
        byte[] content = googleCloudStorageService.downloadFile(bucketName, objectName);
        if (content == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(content);
    }

    @DeleteMapping("/{bucketName}/{objectName}")
    public ResponseEntity<String> deleteFile(
            @PathVariable String bucketName,
            @PathVariable String objectName) {
        googleCloudStorageService.deleteFile(bucketName, objectName);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{sourceBucketName}/{sourceObjectName}/move/{destinationBucketName}/{destinationObjectName}")
    public ResponseEntity<String> moveFile(
            @PathVariable String sourceBucketName,
            @PathVariable String sourceObjectName,
            @PathVariable String destinationBucketName,
            @PathVariable String destinationObjectName) {
        googleCloudStorageService.moveFile(sourceBucketName, sourceObjectName, destinationBucketName, destinationObjectName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{bucketName}/{objectName}/exists")
    public ResponseEntity<String> doesFileExist(
            @PathVariable String bucketName,
            @PathVariable String objectName) {
        boolean exists = googleCloudStorageService.doesFileExist(bucketName, objectName);
        if (exists) {
            return ResponseEntity.ok("File exists");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
