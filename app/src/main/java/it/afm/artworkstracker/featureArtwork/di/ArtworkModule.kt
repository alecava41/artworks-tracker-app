package it.afm.artworkstracker.featureArtwork.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.afm.artworkstracker.featureArtwork.data.dataSource.local.ArtworkDatabase
import it.afm.artworkstracker.featureArtwork.data.util.UUIDConverter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ArtworkModule {

    @Provides
    @Singleton
    fun provideArtworkDatabase(app: Application): ArtworkDatabase {
        return Room.databaseBuilder(app, ArtworkDatabase::class.java, ArtworkDatabase.DB_NAME)
            .addTypeConverter(UUIDConverter())
            .build()
    }

}