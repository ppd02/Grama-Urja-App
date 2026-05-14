package com.gramaurja2.app.ui.components

import com.gramaurja2.app.domain.model.Language

fun t(en: String, kn: String, language: Language): String = if (language == Language.Kannada) "$kn\n$en" else en
