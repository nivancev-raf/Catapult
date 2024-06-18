package rs.edu.raf.rma.auth


import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.catapult.users.UserProfile
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object UserProfileSerializer : Serializer<UserProfile> {
    override val defaultValue: UserProfile = UserProfile("", "", "")

    override suspend fun readFrom(input: InputStream): UserProfile {
        try {
            return Json.decodeFromString(
                UserProfile.serializer(),
                input.readBytes().decodeToString()
            )
        } catch (exception: SerializationException) {
            throw CorruptionException("Cannot read UserProfile.", exception)
        }
    }

    override suspend fun writeTo(t: UserProfile, output: OutputStream) {
        output.write(
            Json.encodeToString(UserProfile.serializer(), t).encodeToByteArray()
        )
    }
}



//class UserProfileSerializer : Serializer<String> {
//
//    override val defaultValue: String = ""
//
//    override suspend fun readFrom(input: InputStream): String {
//        return withContext(Dispatchers.IO) {
//            val sb = StringBuilder()
//            var ch: Int
//            while ((input.read().also { ch = it }) != -1) {
//                sb.append(ch.toChar())
//            }
//            sb.toString()
//        }
//    }
//
//    override suspend fun writeTo(t: String, output: OutputStream) {
//        withContext(Dispatchers.IO) {
//            output.write(t.toByteArray())
//        }
//    }
//}