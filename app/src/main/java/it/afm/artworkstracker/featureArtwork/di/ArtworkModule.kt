package it.afm.artworkstracker.featureArtwork.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.afm.artworkstracker.core.data.remote.MuseumApi
import it.afm.artworkstracker.core.data.local.ArtworkDatabase
import it.afm.artworkstracker.featureArtwork.data.repository.ArtworkRepositoryImpl
import it.afm.artworkstracker.featureArtwork.data.util.UUIDConverter
import it.afm.artworkstracker.featureArtwork.domain.repository.ArtworkRepository
import it.afm.artworkstracker.featureArtwork.domain.useCase.DeleteAllArtworksUseCase
import it.afm.artworkstracker.featureArtwork.domain.useCase.GetArtworkUseCase
import it.afm.artworkstracker.featureArtwork.domain.useCase.GetVisitedArtworksUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ArtworkModule {

    @Provides
    @Singleton
    fun provideDeleteAllArtworksUseCase(artworkRepository: ArtworkRepository): DeleteAllArtworksUseCase {
        return DeleteAllArtworksUseCase(artworkRepository)
    }

    @Provides
    @Singleton
    fun provideGetArtworkUseCase(artworkRepository: ArtworkRepository): GetArtworkUseCase {
        return GetArtworkUseCase(artworkRepository)
    }

    @Provides
    @Singleton
    fun provideGetVisitedArtworks(artworkRepository: ArtworkRepository): GetVisitedArtworksUseCase {
        return GetVisitedArtworksUseCase(artworkRepository)
    }

    @Provides
    @Singleton
    fun provideRoomRepository(
        api: MuseumApi,
        db: ArtworkDatabase
    ): ArtworkRepository {
        return ArtworkRepositoryImpl(api, db.dao)
    }

    @Provides
    @Singleton
    fun provideArtworkDatabase(app: Application): ArtworkDatabase {
        return Room.databaseBuilder(app, ArtworkDatabase::class.java, ArtworkDatabase.DB_NAME)
            .addTypeConverter(UUIDConverter())
            .build()
    }

}