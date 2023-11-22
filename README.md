# Rijksmuseum Android Test App

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