package info.jukov.rijksmuseum.feature.list.di

import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import info.jukov.rijksmuseum.Const
import info.jukov.rijksmuseum.feature.list.data.CollectionApiService
import info.jukov.rijksmuseum.feature.list.data.CollectionRepositoryImpl
import info.jukov.rijksmuseum.feature.list.domain.CollectionRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
abstract class CollectionModule {

    @Binds
    abstract fun bindCollectionRepository(
        impl: CollectionRepositoryImpl
    ): CollectionRepository

    companion object {
        @Provides
        fun provideCollectionApiService(
            moshi: Moshi,
            okHttpClient: OkHttpClient
        ): CollectionApiService {
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Const.Network.API_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(CollectionApiService::class.java)
        }
    }
}