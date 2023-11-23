package info.jukov.rijksmuseum.feature.art.details.di

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import info.jukov.rijksmuseum.feature.art.details.data.ArtDetailsRepositoryTestImpl
import info.jukov.rijksmuseum.feature.art.details.domain.ArtDetailsRepository

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ArtDetailsModule::class],
)
interface TestArtDetailsModule {
    @Binds
    fun bindArtDetailsRepository(
        impl: ArtDetailsRepositoryTestImpl
    ): ArtDetailsRepository
}
