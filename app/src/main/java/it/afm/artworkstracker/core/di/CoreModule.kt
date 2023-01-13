package it.afm.artworkstracker.core.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.afm.artworkstracker.core.data.local.ArtworkDatabase
import it.afm.artworkstracker.core.data.remote.MuseumApi
import it.afm.artworkstracker.featureArtwork.data.util.UUIDConverter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideArtworkApi(): MuseumApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://fakeurlnotworking.com")
            .build()
            .create(MuseumApi::class.java)
    }

    @Provides
    @Singleton
    fun provideArtworkDatabase(app: Application): ArtworkDatabase {
        return Room.databaseBuilder(app, ArtworkDatabase::class.java, ArtworkDatabase.DB_NAME)
            .addTypeConverter(UUIDConverter())
            .build()
    }
}