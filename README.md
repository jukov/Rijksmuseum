# Rijksmuseum Android Test App

## API key

To use this app, you need to get API key by registering in [Rijks Studio](https://www.rijksmuseum.nl/en/rijksstudio).
After registration, you can request API key in your [profile settings](https://www.rijksmuseum.nl/nl/rijksstudio/mijn/gegevens).

After receiving API key, create file `apikey.properties` with following content:
```
apikey=%YOUR_API_KEY%
```

## Technological Stack

* Jetpack Compose
* RxJava
* Hilt
* Moshi
* OkHttp
* Retrofit
* Coil
* JUnit
* Mockk

## Possible improvements

### UX / Features

* Art object images may have Instagram-like gesture for zoom image.
* ArtDetails description can be synchronized with web version of Rijksmuseum catalog to display data in similar way.

### Technical

* Since art details looks as final data that will not change over time, they can be cached in memory using LRU or in persistent memory with more complex invalidation mechanism.
* I didn't write unit tests for Repositories, but it might be a good idea since they contain mappers with small parts of logic.
* Project can be migrated to KSP, but I decided use kapt, because Dagger KSP still in alpha
* More instrumented tests should be written