package info.jukov.rijksmuseum.feature.art.details.di

import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import info.jukov.rijksmuseum.Const
import info.jukov.rijksmuseum.feature.art.details.data.ArtDetailsApiService
import info.jukov.rijksmuseum.feature.art.details.data.ArtDetailsRepositoryImpl
import info.jukov.rijksmuseum.feature.art.details.domain.ArtDetailsRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
abstract class ArtDetailsModule {

    @Binds
    abstract fun bindArtDetailsRepository(
        impl: ArtDetailsRepositoryImpl
    ): ArtDetailsRepository

    companion object {
        @Provides
        fun provideArtDetailsApiService(
            moshi: Moshi,
            okHttpClient: OkHttpClient
        ): ArtDetailsApiService {
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Const.Network.API_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(ArtDetailsApiService::class.java)
        }
    }
}