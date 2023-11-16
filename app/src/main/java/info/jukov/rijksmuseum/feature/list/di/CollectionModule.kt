package info.jukov.rijksmuseum.feature.list.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import info.jukov.rijksmuseum.feature.list.data.CollectionRepositoryImpl
import info.jukov.rijksmuseum.feature.list.domain.CollectionRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class CollectionModule {

    @Binds
    abstract fun bindCollectionRepository(
        impl: CollectionRepositoryImpl
    ): CollectionRepository
}