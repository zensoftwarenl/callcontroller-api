


# How to generate test-audio-file-for transcription.

```thymeleafurlexpressions
https://texttospeech.googleapis.com/v1/text:synthesize?key=<GOOGLEAPIKEY>
```

then text edit the json in Postman "Send and Download"
remove the json from file and keep the base64 encoded WAV file.

next
convert the b64 to a real binary wav file
```bash
base64 --decode -i Desktop/editted_from_postman.json -o Desktop/test-audio-file-for-transcription.wav
```


```json
{
  "input":{
    "text":"Google Cloud Text-to-Speech enables developers to synthesize natural-sounding speech with 100+ voices, available in multiple languages and variants. It applies DeepMind’s groundbreaking research in WaveNet and Google’s powerful neural networks to deliver the highest fidelity possible. As an easy-to-use API, you can create lifelike interactions with your users, across many applications and devices."
  },
  "voice": {
    "languageCode": "en-GB",
    "name": "en-GB-Neural2-A"
  },
  "audioConfig": {
    "audioEncoding": "LINEAR16",
    "effectsProfileId": [
      "large-home-entertainment-class-device"
    ],
    "pitch": 0,
    "speakingRate": 1
  }
}
```