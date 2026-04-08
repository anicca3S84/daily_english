package com.codework.daily_english.core.utils

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

object GoogleAuthHelper {

    suspend fun signIn(
        context: Context,
        serverClientId: String
    ): String? {
        return try {
            val credentialManager = CredentialManager.create(context)

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(serverClientId)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            val credential = result.credential

            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                return googleIdTokenCredential.idToken
            }

            null
        } catch (e: GetCredentialCancellationException) {
            Log.d("GoogleSignIn", "Người dùng đã hủy.")
            null
        } catch (e: Exception) {
            // IN ĐẬM LỖI RA ĐÂY ĐỂ BIẾT CHÍNH XÁC NÓ BỊ GÌ
            Log.e("GoogleSignIn", "Lỗi ném ra là class: ${e.javaClass.simpleName}")
            Log.e("GoogleSignIn", "Chi tiết lỗi: ${e.message}")
            e.printStackTrace()
            null
        }
    }
}