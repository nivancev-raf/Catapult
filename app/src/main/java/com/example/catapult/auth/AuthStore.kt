package com.example.catapult.auth

import androidx.datastore.core.DataStore
import com.example.catapult.users.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthStore @Inject constructor(
    private val persistence: DataStore<UserProfile>
) {

    private val scope = CoroutineScope(Dispatchers.IO)

    val authData = persistence.data
        .stateIn( // .stateIn
            scope = scope,
            started = SharingStarted.Eagerly, // .Eagerly znači da će se flow pokrenuti odmah
            initialValue = runBlocking { persistence.data.first() },
        )
   // runBlocking ne obezbeđuje drugu nit,
   // već blokira trenutnu nit dok se ne završi izvršavanje suspendovane funkcije unutar runBlocking bloka
    suspend fun updateAuthData(newAuthData: UserProfile) {
        persistence.updateData { oldAuthData -> newAuthData }
    }
}
