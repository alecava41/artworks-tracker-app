package it.afm.artworkstracker.util

import java.util.*

object LanguageUtil {
    val supportedLanguages = listOf<String>(
        Locale.ITALIAN.language,
        Locale.GERMAN.language,
        Locale.FRENCH.language,
        Locale("es").language
    )
}