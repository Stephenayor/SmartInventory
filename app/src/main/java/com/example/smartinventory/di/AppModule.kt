package com.example.smartinventory.di

import android.content.Context
import androidx.room.Room
import com.example.smartinventory.data.WebService
import com.example.smartinventory.data.database.AppDatabase
import com.example.smartinventory.data.database.ProductDao
import com.example.smartinventory.data.repository.DashBoardRepositoryImpl
import com.example.smartinventory.data.repository.ProductsRepositoryImpl
import com.example.smartinventory.domain.DashBoardRepository
import com.example.smartinventory.domain.ProductsRepository
import com.example.smartinventory.utils.AppConstants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(provideHttpLoggingInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okhHttpClient: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        return Retrofit.Builder()
            .client(okhHttpClient)
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideWebService(retrofit: Retrofit): WebService {
        return retrofit.create(WebService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "smartinventory_app_db")
            .build()
    }

    @Provides
    fun provideProductDao(appDatabase: AppDatabase): ProductDao {
        return appDatabase.productDao()
    }

    @Provides
    @Singleton
    fun provideDashboardRepository(
        webService: WebService,
        productDao: ProductDao,
        @ApplicationContext context: Context
    ): DashBoardRepository {
        return DashBoardRepositoryImpl(webService, productDao, context)
    }

    @Provides
    @Singleton
    fun provideProductsRepository(
        productDao: ProductDao,
        @ApplicationContext context: Context
    ): ProductsRepository {
        return ProductsRepositoryImpl(productDao, context)
    }

}