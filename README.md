## Welcome to the CallController project

The following overview diagram illustrates 

```mermaid
graph TD
  GoogleCloudRun[Google CloudRun]-->|runs container|CallController((CallController))
  CallController((CallController))-->|stores, retrieves, and transcodes|Calls[Call data]
  CallController((CallController))-->|stores and retrieves|CallMetadata[Call metadata]
  CallController((CallController))-->|stores and retrieves|CallTranscripts[Call transcripts]
  CallController((CallController))-->|generate transcript|GoogleCloudSpeechAPI[Google Cloud Speech API]
  Calls[VoiceCall data]-->|persisted on|GoogleCloudStorage[Google Cloud Storage]
  CallMetadata[VoiceCall metadata]-->|in|GoogleFirebase[Google Firebase]
  CallTranscripts[VoiceCall transcripts]-->|in|GoogleFirebase[Google Firebase]
````

In this diagram, the **CallController** component depends on **GoogleCloudStorage** for 
storing, retrieving, and transcoding calls. 
It also depends on **GoogleFirebase** to store and retrieve call metadata 
and call transcripts. Additionally, 
it uses the **GoogleCloudSpeechAPI** to get transcriptions of calls in multiple languages like Dutch and English.


