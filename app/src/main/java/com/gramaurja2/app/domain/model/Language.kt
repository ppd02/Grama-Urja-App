package com.gramaurja2.app.domain.model

enum class Language { English, Kannada }

fun dualText(en: String, kn: String, language: Language): String =
    if (language == Language.Kannada) "$kn\n$en" else en
