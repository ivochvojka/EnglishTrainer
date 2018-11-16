package cz.ich.englishtrainer.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseUser
import cz.ich.englishtrainer.R
import cz.ich.englishtrainer.service.FirestoreService
import cz.ich.englishtrainer.service.LoginService
import kotlinx.android.synthetic.main.activity_startup.*
import timber.log.Timber


/**
 * @author Ivo Chvojka
 */
class StartupActivity : AppCompatActivity() {

//    private lateinit var googleSignInClient: GoogleSignInClient
//    private lateinit var firebaseAuth: FirebaseAuth

    private val loginService: LoginService = LoginService.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_startup)
        loginService.init(this)
        findViewById<ViewGroup>(R.id.sign_in_button).setOnClickListener {
            loginService.signIn(this)
        }
//
//        // Configure sign-in to request the user's ID, email address, and basic
//        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build()
//
//        // Build a GoogleSignInClient with the options specified by gso.
//        googleSignInClient = GoogleSignIn.getClient(this, gso)
//        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        Timber.d("onStart")

//        // Check for existing Google Sign In account, if the user is already signed in
//        // the GoogleSignInAccount will be non-null.
//        val account = GoogleSignIn.getLastSignedInAccount(this)
//        updateUI(account)

        val currentUser = loginService.getFirebaseUser()
        updateUI(currentUser)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        loginService.onActivityResult(this, requestCode, data, ::updateUI)
    }

//    fun signIn(view: View) {
//        Timber.d("signIn")
//        loginService.signIn(this)
////        val signInIntent = googleSignInClient.signInIntent
////        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }

    fun signOut(view: View) {
        loginService.signOut(this, ::updateUI)
//        // Firebase sign out
//        firebaseAuth.signOut()
//
//        // Google sign out
//        googleSignInClient.signOut().addOnCompleteListener(this) {
//            updateUI(null)
//        }
    }

    fun createUser(view: View) {
        FirestoreService.getInstance(loginService.getFirebaseUser()).createUser()
    }


    fun getUser(view: View) {
        FirestoreService.getInstance(loginService.getFirebaseUser()).getUser()
    }

//    fun revokeAccess(view: View) {
//        // Firebase sign out
//        firebaseAuth.signOut()
//
//        // Google sign out
//        googleSignInClient.revokeAccess().addOnCompleteListener(this) {
//            updateUI(null)
//        }
//    }

//    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
//        try {
//            val account = completedTask.getResult(ApiException::class.java)
//            firebaseAuthWithGoogle(account!!)
////            // Signed in successfully, show authenticated UI.
////            updateUI(account)
//        } catch (e: ApiException) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Timber.w("signInResult:failed code=%d", e.statusCode)
//            updateUI(null)
//        }
//
//    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            FirestoreService.getInstance(loginService.getFirebaseUser())
            startActivity(Intent(this, AlbumsActivity::class.java))
            finish()
        } else {
            txt_result.setText("sorry vole error!")
        }

        sign_in_button.visibility = if (currentUser != null) View.GONE else View.VISIBLE
        sign_out_button.visibility = if (currentUser != null) View.VISIBLE else View.GONE
        btn_create_user.visibility = if (currentUser != null) View.VISIBLE else View.GONE
        btn_get_user.visibility = if (currentUser != null) View.VISIBLE else View.GONE
//        btn_revoke_access.visibility = if (currentUser != null) View.VISIBLE else View.GONE
    }

//    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
//        Timber.d("firebaseAuthWithGoogle:" + acct.id!!)
//        // [START_EXCLUDE silent]
////        showProgressDialog() TODO
//        // [END_EXCLUDE]
//
//        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
//        firebaseAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        // Sign in success, update UI with the signed-in user's information
//                        Timber.d("signInWithCredential:success")
//                        val user = firebaseAuth.currentUser
//                        updateUI(user)
//                    } else {
//                        // If sign in fails, display a message to the user.
//                        Timber.w("signInWithCredential:failure", task.exception)
//                        Snackbar.make(container, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
//                        updateUI(null)
//                    }
//
//                    // [START_EXCLUDE]
////                    hideProgressDialog() TODO
//                    // [END_EXCLUDE]
//                }
//    }

}