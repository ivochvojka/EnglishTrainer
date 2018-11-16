package cz.ich.englishtrainer.service

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import cz.ich.englishtrainer.R
import timber.log.Timber

class LoginService private constructor() {

    private object Holder {
        val INSTANCE = LoginService()
    }

    companion object {
        private const val RC_SIGN_IN = 1
        val instance: LoginService by lazy { Holder.INSTANCE }
    }

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    fun init(ctx: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(ctx.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(ctx, gso)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    fun signIn(activity: Activity) {
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun signOut(activity: Activity, updateUI: (currentUser: FirebaseUser?) -> Unit) {
        // Firebase sign out
        firebaseAuth.signOut()

        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(activity) {
            updateUI(null)
        }
    }

    fun onActivityResult(activity: Activity, requestCode: Int, data: Intent, updateUI: (currentUser: FirebaseUser?) -> Unit) {
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(activity, task, updateUI)
        }
    }

    private fun handleSignInResult(activity: Activity, completedTask: Task<GoogleSignInAccount>, updateUI: (currentUser: FirebaseUser?) -> Unit) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(activity, account!!, updateUI)
//            // Signed in successfully, show authenticated UI.
//            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Timber.w("signInResult:failed code=%d", e.statusCode)
            updateUI(null)
        }

    }

    private fun firebaseAuthWithGoogle(activity: Activity, acct: GoogleSignInAccount, updateUI: (currentUser: FirebaseUser?) -> Unit) {
        Timber.d("firebaseAuthWithGoogle:" + acct.id!!)
        // [START_EXCLUDE silent]
//        showProgressDialog() TODO
        // [END_EXCLUDE]

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Timber.d("signInWithCredential:success")
                        val user = firebaseAuth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.w("signInWithCredential:failure", task.exception)
//                        Snackbar.make(container, "Authentication Failed.", Snackbar.LENGTH_SHORT).show() // FIXME add to update as error
                        updateUI(null)
                    }

                    // [START_EXCLUDE]
//                    hideProgressDialog() TODO
                    // [END_EXCLUDE]
                }
    }

    fun getFirebaseUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

}