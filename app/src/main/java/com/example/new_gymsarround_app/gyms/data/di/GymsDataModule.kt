package com.example.new_gymsarround_app.gyms.data.di

import android.content.Context
import androidx.room.Room
import com.example.new_gymsarround_app.gyms.data.Local.GymsDao
import com.example.new_gymsarround_app.gyms.data.Local.GymsDatabase
import com.example.new_gymsarround_app.gyms.data.Remote.GymsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GymsDataModule {

    @Provides
    fun provideApiService(
        retrofit: Retrofit
    ):GymsApiService{
        return retrofit.create(GymsApiService::class.java)
    }


    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .baseUrl("https://cairo-gyms-427a3-default-rtdb.firebaseio.com/")
            .build()
    }

    @Provides
    fun provideRoomDao(
        db:GymsDatabase
    ) :GymsDao{
     return db.dao
    }

    @Singleton
    @Provides
    fun provideRoomDataBase(
       @ApplicationContext context: Context
    ) :GymsDatabase{
       return Room.databaseBuilder(
            context,
            GymsDatabase::class.java ,
            "gyms_Database"
        ).fallbackToDestructiveMigration().build()

    }
}