package info.jukov.rijksmuseum.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import info.jukov.rijksmuseum.util.error.ErrorMapper

@Module
@InstallIn(SingletonComponent::class)
object ErrorHandlingModule {

    @Provides
    fun provideErrorMapper(): ErrorMapper = ErrorMapper()
}