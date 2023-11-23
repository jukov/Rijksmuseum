package info.jukov.rijksmuseum.feature.art.collection.di

import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import info.jukov.rijksmuseum.Const
import info.jukov.rijksmuseum.di.PageSize
import info.jukov.rijksmuseum.feature.art.collection.data.ArtCollectionApiService
import info.jukov.rijksmuseum.feature.art.collection.data.ArtCollectionRepositoryImpl
import info.jukov.rijksmuseum.feature.art.collection.domain.ArtCollectionRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
interface ArtCollectionModule {

    @Binds
    fun bindCollectionRepository(
        impl: ArtCollectionRepositoryImpl
    ): ArtCollectionRepository

    companion object {

        @Provides
        @PageSize
        fun providePageSize(): Int = Const.Network.PAGE_SIZE

        @Provides
        fun provideCollectionApiService(
            moshi: Moshi,
            okHttpClient: OkHttpClient
        ): ArtCollectionApiService {
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Const.Network.API_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(ArtCollectionApiService::class.java)
        }
    }
}