package com.gramaurja2.app.domain.model

data class UserProfile(
    val uid: String = "",
    val name: String = "Local Farmer",
    val followedZoneIds: Set<String> = setOf(ZoneCatalog.defaultZone.id),
    val mutedZoneIds: Set<String> = emptySet(),
    val language: Language = Language.English
)
