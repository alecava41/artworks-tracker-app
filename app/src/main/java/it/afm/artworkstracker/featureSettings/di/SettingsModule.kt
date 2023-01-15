package it.afm.artworkstracker.featureSettings.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.afm.artworkstracker.core.data.local.ArtworkDatabase
import it.afm.artworkstracker.featureSettings.data.repository.DeleteArtworksRepositoryImpl
import it.afm.artworkstracker.featureSettings.domain.repository.DeleteArtworksRepository
import it.afm.artworkstracker.featureSettings.domain.useCase.DeleteArtworksUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {

    @Provides
    @Singleton
    fun provideDeleteArtworksUseCase(repository: DeleteArtworksRepository): DeleteArtworksUseCase {
        return DeleteArtworksUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteArtworksRepository(
        db: ArtworkDatabase
    ): DeleteArtworksRepository {
        return DeleteArtworksRepositoryImpl(db.dao)
    }
}