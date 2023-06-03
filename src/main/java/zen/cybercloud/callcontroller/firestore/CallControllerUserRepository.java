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

import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;


public interface CallControllerUserRepository extends FirestoreReactiveRepository<CallControllerUser> {

}

//
/// @Repository
//
//    private final CollectionReference collectionReference;
//
//    public CallControllerUserRepository(Firestore firestore) {
//        this.collectionReference = firestore.collection("testing").document("mainDocumentId").collection("callControllerUsers");
//    }
//
//    public Mono<CallControllerUser> findById(String id) {
//        DocumentReference documentReference = collectionReference.document(id);
//        return getDocumentSnapshot(documentReference)
//                .map(docSnapshot -> docSnapshot.toObject(CallControllerUser.class));
//    }
//
////    public Flux<CallControllerUser> findAll() {
////        return getAllQueryDocuments()
////                .map(queryDocumentSnapshot -> queryDocumentSnapshot.toObject(CallControllerUser.class));
////    }
//
//    public Mono<CallControllerUser> save(CallControllerUser user) {
//        DocumentReference documentReference;
//        if (user.getId() != null) {
//            documentReference = collectionReference.document(user.getId());
//        } else {
//            documentReference = collectionReference.document();
//            user.setId(documentReference.getId());
//        }
//
//        return setDocument(documentReference, user)
//                .thenReturn(user);
//    }
//
//    public Mono<Void> deleteById(String id) {
//        DocumentReference documentReference = collectionReference.document(id);
//        return deleteDocument(documentReference);
//    }
//
//    private Mono<DocumentSnapshot> getDocumentSnapshot(DocumentReference documentReference) {
//        ApiFuture<DocumentSnapshot> future = documentReference.get();
//        CompletableFuture<DocumentSnapshot> completableFuture = CompletableFuture.supplyAsync(() -> {
//            try {
//                return future.get();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//        return Mono.fromFuture(completableFuture);
//    }
//
//    private Mono<List<QueryDocumentSnapshot>> getAllQueryDocuments(CollectionReference collectionReference) {
//        ApiFuture<QuerySnapshot> future = collectionReference.get();
//        CompletableFuture<List<QueryDocumentSnapshot>> completableFuture = CompletableFuture.supplyAsync(() -> {
//            try {
//                QuerySnapshot querySnapshot = future.get();
//                return querySnapshot.getDocuments();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//        return Mono.fromFuture(completableFuture);
//    }
//
//    private Mono<Void> setDocument(DocumentReference documentReference, CallControllerUser user) {
//        ApiFuture<WriteResult> future = documentReference.set(user);
//        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
//            try {
//                future.get();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//        return Mono.fromFuture(completableFuture);
//    }
//
//    private Mono<Void> deleteDocument(DocumentReference documentReference) {
//        ApiFuture<WriteResult> future = documentReference.delete();
//        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
//            try {
//                future.get();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//        return Mono.fromFuture(completableFuture);
//    }
//}

