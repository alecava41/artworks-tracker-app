package it.afm.artworkstracker.featureVisitedArtworksList.presentation

import it.afm.artworkstracker.core.domain.model.Artwork

data class VisitedArtworksListState(
    val visitedArtworksList: List<Artwork> = listOf()
)
