package it.afm.artworkstracker.featureMuseumMap.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.afm.artworkstracker.core.data.local.ArtworkDatabase
import it.afm.artworkstracker.core.data.remote.MuseumApi
import it.afm.artworkstracker.featureMuseumMap.data.dataSource.beacon.BeaconDataSourceImpl
import it.afm.artworkstracker.featureMuseumMap.data.dataSource.beacon.BeaconsDataSource
import it.afm.artworkstracker.featureMuseumMap.data.repository.ArtworkRepositoryImpl
import it.afm.artworkstracker.featureMuseumMap.data.repository.BeaconsRepositoryImpl
import it.afm.artworkstracker.featureMuseumMap.data.repository.RoomRepositoryImpl
import it.afm.artworkstracker.featureMuseumMap.domain.repository.ArtworkRepository
import it.afm.artworkstracker.featureMuseumMap.domain.repository.BeaconsRepository
import it.afm.artworkstracker.featureMuseumMap.domain.repository.RoomRepository
import it.afm.artworkstracker.featureMuseumMap.domain.useCase.GetArtworksIdsUseCase
import it.afm.artworkstracker.featureMuseumMap.domain.useCase.GetCloserBeaconsUseCase
import it.afm.artworkstracker.featureMuseumMap.domain.useCase.GetRoomUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MuseumMapModule {

    // "Beacons" DI
    @Provides
    @Singleton
    fun provideBeaconsDataSource(app: Application): BeaconsDataSource {
        return BeaconDataSourceImpl(app)
    }

    @Provides
    @Singleton
    fun provideBeaconsRepository(beaconsDataSource: BeaconsDataSource): BeaconsRepository {
        return BeaconsRepositoryImpl(beaconsDataSource)
    }

    @Provides
    @Singleton
    fun provideGetCloserBeaconsUseCase(beaconsRepository: BeaconsRepository): GetCloserBeaconsUseCase {
        return GetCloserBeaconsUseCase(beaconsRepository)
    }

    // Room DI
    @Provides
    @Singleton
    fun provideRoomRepository(api: MuseumApi): RoomRepository {
        return RoomRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideGetRoomUseCase(roomRepository: RoomRepository): GetRoomUseCase {
        return GetRoomUseCase(roomRepository)
    }

    // Artwork DI
    @Provides
    @Singleton
    fun provideArtworkRepository(db: ArtworkDatabase): ArtworkRepository {
        return ArtworkRepositoryImpl(db.dao)
    }

    @Provides
    @Singleton
    fun provideGetArtworksIdsUseCase(repository: ArtworkRepository): GetArtworksIdsUseCase {
        return GetArtworksIdsUseCase(repository)
    }

}