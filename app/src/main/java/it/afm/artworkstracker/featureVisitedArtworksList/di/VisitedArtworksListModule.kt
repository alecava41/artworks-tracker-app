package it.afm.artworkstracker.featureVisitedArtworksList.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.afm.artworkstracker.core.data.local.ArtworkDatabase
import it.afm.artworkstracker.featureVisitedArtworksList.data.repository.VisitedArtworksListRepositoryImpl
import it.afm.artworkstracker.featureVisitedArtworksList.domain.repository.VisitedArtworksListRepository
import it.afm.artworkstracker.featureVisitedArtworksList.domain.useCase.GetVisitedArtworksListUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VisitedArtworksListModule {

    @Provides
    @Singleton
    fun provideGetVisitedArtworksListUseCase(visitedArtworksListRepository: VisitedArtworksListRepository): GetVisitedArtworksListUseCase {
        return GetVisitedArtworksListUseCase(visitedArtworksListRepository)
    }

    @Provides
    @Singleton
    fun provideVisitedArtworksRepository(database: ArtworkDatabase): VisitedArtworksListRepository {
        return VisitedArtworksListRepositoryImpl(database.dao)
    }
}