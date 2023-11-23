# Rijksmuseum Android Test App

## API key

To use this app, you need to get API key by registering in [Rijks Studio](https://www.rijksmuseum.nl/en/rijksstudio).
After registration, you can request API key in your [profile settings](https://www.rijksmuseum.nl/nl/rijksstudio/mijn/gegevens).

After receiving API key, create file `apikey.properties` in root folder with following content:
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

* Art object images may use Instagram-like gesture for zoom-in the image.
* ArtDetails description can be synchronized with the web version of Rijksmuseum catalog to display data in a similar way.
* Reload button for images should be provided in case of network error.

### Technical

* Since art details appear to be final data that will not change over time, they can be cached in memory using LRU or in persistent memory with more complex invalidation mechanism.
* I haven't written  unit tests for Repositories, but it might be a good idea since they contain mappers with small parts of logic.
* Project can be migrated to KSP, but I decided use kapt, because Dagger KSP still in alpha.
* More instrumented tests should be written.
* I have used Coil's SubcomposeAsyncImage in art collection list, although Coil devs said that is can cause some performance issues. It might be good idea to investigate this functionality more precisely.
* Code minification may be introduced to make app reverse engineering more challenging. However, it also requires extensive testing, so I decided not to implement it.