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

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class GoogleCloudStorageRepository implements CloudStorageRepository {

    private final Storage storage;

    public GoogleCloudStorageRepository() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    @Override
    public void uploadObject(String bucketName, String objectName, byte[] content) {
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, content);
    }

    @Override
    public byte[] downloadObject(String bucketName, String objectName) {
        BlobId blobId = BlobId.of(bucketName, objectName);
        Blob blob = storage.get(blobId);
        if (blob == null) {
            return null;
        }
        return blob.getContent();
    }

    @Override
    public void deleteObject(String bucketName, String objectName) {
        BlobId blobId = BlobId.of(bucketName, objectName);
        storage.delete(blobId);
    }

    @Override
    public void copyObject(String sourceBucketName, String sourceObjectName, String destinationBucketName, String destinationObjectName) {
        BlobId sourceBlobId = BlobId.of(sourceBucketName, sourceObjectName);
        BlobId destinationBlobId = BlobId.of(destinationBucketName, destinationObjectName);
        storage.copy(new Storage.CopyRequest.Builder()
                .setSource(sourceBlobId)
                .setTarget(destinationBlobId)
                .build());
    }

    @Override
    public boolean doesObjectExist(String bucketName, String objectName) {
        BlobId blobId = BlobId.of(bucketName, objectName);
        return storage.get(blobId) != null;
    }
}
