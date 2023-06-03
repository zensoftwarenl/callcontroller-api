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

package zen.cybercloud.callcontroller.storage;

import java.io.IOException;

public class GoogleCloudStorageService {

    private final CloudStorageRepository repository;

    public GoogleCloudStorageService(CloudStorageRepository repository) {
        this.repository = repository;
    }

    public void uploadFile(String bucketName, String objectName, byte[] content) throws IOException {
        repository.uploadObject(bucketName, objectName, content);
    }

    public byte[] downloadFile(String bucketName, String objectName) {
        return repository.downloadObject(bucketName, objectName);
    }

    public void deleteFile(String bucketName, String objectName) {
        repository.deleteObject(bucketName, objectName);
    }

    public void moveFile(String sourceBucketName, String sourceObjectName, String destinationBucketName, String destinationObjectName) {
        repository.copyObject(sourceBucketName, sourceObjectName, destinationBucketName, destinationObjectName);
        repository.deleteObject(sourceBucketName, sourceObjectName);
    }

    public boolean doesFileExist(String bucketName, String objectName) {
        return repository.doesObjectExist(bucketName, objectName);
    }
}
