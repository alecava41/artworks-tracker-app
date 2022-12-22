package it.afm.artworkstracker.featureMuseumMap.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.afm.artworkstracker.featureMuseumMap.data.dataSource.BeaconDataSourceImpl
import it.afm.artworkstracker.featureMuseumMap.data.dataSource.BeaconsDataSource
import it.afm.artworkstracker.featureMuseumMap.data.repository.BeaconsRepositoryImpl
import it.afm.artworkstracker.featureMuseumMap.domain.repository.BeaconsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MuseumMapModule {

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
}