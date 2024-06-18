package rs.edu.raf.rma.auth

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
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = runBlocking { persistence.data.first() },
        )

    suspend fun updateAuthData(newAuthData: UserProfile) {
        persistence.updateData { oldAuthData ->
            // Ukoliko je potrbno možete obraditi nove i stare podatke
            // pre nego što upišite konačnu novu vrednost u persistence
            newAuthData
        }
    }
}
