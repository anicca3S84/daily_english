package com.codework.dailyenglish.core.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
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
        } catch (_: GetCredentialCancellationException) {
            Log.d("GoogleSignIn", "Cancel")
            null
        } catch (e: NoCredentialException) {
            Log.e("GoogleSignIn", "Thiết bị không có tài khoản Google nào.")
            openAddGoogleAccountScreen(context)
            null
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "${e.javaClass.simpleName}")
            Log.e("GoogleSignIn", "${e.message}")
            e.printStackTrace()
            null
        }
    }

    private fun openAddGoogleAccountScreen(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_ADD_ACCOUNT).apply {
                putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "Không thể mở cài đặt thêm tài khoản: ${e.message}")
        }
    }
}