package info.jukov.rijksmuseum.feature.art.details.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import info.jukov.rijksmuseum.di.PageSize
import info.jukov.rijksmuseum.feature.art.collection.di.ArtCollectionModule
import info.jukov.rijksmuseum.feature.art.collection.domain.ArtCollectionRepository
import info.jukov.rijksmuseum.feature.art.details.data.ArtCollectionRepositoryTestImpl

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ArtCollectionModule::class],
)
interface TestArtCollectionModule {
    @Binds
    fun bindArtCollectionRepository(
        impl: ArtCollectionRepositoryTestImpl
    ): ArtCollectionRepository

    companion object {
        @Provides
        @PageSize
        fun providePageSize(): Int = 5
    }
}
