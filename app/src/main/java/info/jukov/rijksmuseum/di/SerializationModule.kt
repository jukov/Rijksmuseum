package info.jukov.rijksmuseum.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SerializationModule {

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

}