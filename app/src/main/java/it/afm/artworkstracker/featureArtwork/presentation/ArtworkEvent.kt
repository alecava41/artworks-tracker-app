package it.afm.artworkstracker.featureArtwork.presentation

sealed class ArtworkEvent{
/*    object FirstSlide: ArtworkEvent()
    data class ImageNext(val currentPage: Int): ArtworkEvent()
    data class ImagePrevious(val currentPage: Int): ArtworkEvent()
    object LastSlide: ArtworkEvent()*/
    object AudioChange: ArtworkEvent()
}