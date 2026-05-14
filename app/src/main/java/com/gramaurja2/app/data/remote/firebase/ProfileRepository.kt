package com.gramaurja2.app.data.remote.firebase

import com.google.firebase.database.FirebaseDatabase
import com.gramaurja2.app.data.awaitResult
import com.gramaurja2.app.domain.model.UserProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val database: FirebaseDatabase
) {
    suspend fun saveProfile(profile: UserProfile): Result<Unit> = runCatching {
        database.getReference("users")
            .child(profile.uid)
            .updateChildren(
                mapOf(
                    "uid" to profile.uid,
                    "name" to profile.name,
                    "followedZoneIds" to profile.followedZoneIds.associateWith { true },
                    "mutedZoneIds" to profile.mutedZoneIds.associateWith { true },
                    "language" to profile.language.name,
                    "updatedAt" to System.currentTimeMillis()
                )
            )
            .awaitResult()
    }
}
