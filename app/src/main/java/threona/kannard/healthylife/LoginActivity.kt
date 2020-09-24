package threona.kannard.healthylife

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.BuildConfig
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {
    //region Variables
    //Firebase Instance
    var storage = FirebaseStorage.getInstance()

    // Access a Cloud Firestore instance from your Activity
    private var db: FirebaseFirestore? = FirebaseFirestore.getInstance()

    //Facebook Login Callback variable
    lateinit var callbackManager: CallbackManager

    //Google Login Config Variables
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    //Personal Login variables
    private var textInputEmail : TextInputLayout? = null
    private var textInputPass : TextInputLayout? = null
    private var checkBox : CheckBox? = null
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        if (isLoggedIn() || isSignedIn(this)) {
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //region Personal Login
        //Sign Up
        var signUpBtn = findViewById<Button>(R.id.app_sign_up)
        signUpBtn.setOnClickListener {
            val intent: Intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent,12)
        }
        //Sign In
        textInputEmail = findViewById(R.id.text_input_email)
        textInputPass = findViewById(R.id.text_input_password)
        val signInBtn = findViewById<Button>(R.id.sign_in_btn)
        signInBtn.setOnClickListener {
            if(!isEmailExist() or !isPassMatch())
                return@setOnClickListener
            else
            {
                val intent : Intent = Intent(this, MainActivity::class.java)
                intent.putExtra("email", textInputEmail?.editText?.text.toString())
                startActivityForResult(intent,12)
            }
        }
        //endregion

        //region Facebook Login Config

        callbackManager = CallbackManager.Factory.create()

        facebook_login_btn.setOnClickListener {
            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("public_profile", "email"))
        }

        // Callback registration
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    Log.d("TAG", "Success Login")
                    // Get User's Info
                    getUserProfile(loginResult?.accessToken, loginResult?.accessToken?.userId)
                }

                override fun onCancel() {
                    Toast.makeText(this@LoginActivity, "Login Cancelled", Toast.LENGTH_LONG).show()
                }

                override fun onError(exception: FacebookException) {
                    Toast.makeText(this@LoginActivity, exception.message, Toast.LENGTH_LONG).show()
                }
            })
        //endregion

        //region Google Login Config
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_API_key))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        google_login_btn.setOnClickListener {
            signIn()
        }
        //endregion
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivityForResult(intent, 12)
    }


    //region Personal Login
    private fun isEmailExist() :Boolean{
        val email = textInputEmail?.editText?.text.toString().trim()

        if(email.isEmpty())
        {
            textInputEmail?.error = "This field can't be empty"
            return false
        }
        else {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                textInputEmail?.error = "It should be a valid email."
                return false
            } else {
                val allUsersRef: CollectionReference? = db?.collection("user")
                val emailQuery: Query? = allUsersRef?.whereEqualTo("email", email)
                val checkMail = emailQuery?.get()
                    ?.addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                        if (task.isSuccessful) {
                            var check : Boolean = false
                            for (document in task.result!!) {
                                if (document.exists()) {
                                    check = true
                                }
                            }
                            if(!check)
                                textInputEmail?.error = "This email is not registered"
                            else{
                                textInputEmail?.error = null
                                textInputEmail?.isErrorEnabled = false
                            }
                        }
                        return@OnCompleteListener
                    })
            }
        }
        return true
    }
    private fun isPassMatch() :Boolean {
        val pass = textInputPass?.editText?.text.toString().trim()
        val email = textInputEmail?.editText?.text.toString().trim()

        if (pass.isEmpty()) {
            textInputPass?.error = "This field can't be empty"
            return false
        } else {
            if (pass.length > 16) {
                textInputPass?.error = "The maximum length of the password is 16"
                return false
            } else {
                val pattern: Pattern
                val matcher: Matcher
                val passwordPattern =
                    "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
                pattern = Pattern.compile(passwordPattern)
                matcher = pattern.matcher(pass)
                if (!matcher.matches()) {
                    textInputPass?.error =
                        "The password must include uppercase letters, lowercase letters, numbers and special characters."
                    return false
                } else {
                    val allUsersRef: CollectionReference? = db?.collection("user")
                    val passQuery: Query? =
                        allUsersRef?.whereEqualTo("email", email)?.whereEqualTo("pass", pass)
                            ?.whereEqualTo("type", "HL")
                    val checkMail = passQuery?.get()
                        ?.addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                            if (task.isSuccessful) {
                                var check : Boolean = false
                                for (document in task.result!!) {
                                    if (document.exists()) {
                                        check = true
                                    }
                                }
                                if(!check)
                                    textInputPass?.error = "Password is not correct"
                                else
                                {
                                    textInputPass?.error = null
                                    textInputPass?.isErrorEnabled = false
                                }
                            }
                            return@OnCompleteListener
                        })
                }
            }
        }
        return true
    }
    //endregion

    //region Facebook Login helper
    fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        return isLoggedIn
    }

    fun logOut() {
        LoginManager.getInstance().logOut()
    }

    fun getUserProfile(token: AccessToken?, userId: String?) {
        var facebookEmail: String = ""
        var facebookName: String = ""
        var facebookId: String = ""

        val parameters = Bundle()
        parameters.putString(
            "fields",
            "id, first_name, middle_name, last_name, name, picture, email"
        )
        GraphRequest(token,
            "/$userId/",
            parameters,
            HttpMethod.GET,
            GraphRequest.Callback { response ->
                val jsonObject = response.jsonObject

                // Facebook Access Token
                // You can see Access Token only in Debug mode.
                // You can't see it in Logcat using Log.d, Facebook did that to avoid leaking user's access token.
                if (BuildConfig.DEBUG) {
                    FacebookSdk.setIsDebugEnabled(true)
                    FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
                }

                // Facebook Id
                if (jsonObject.has("id")) {
                    facebookId = jsonObject.getString("id")
                    Log.i("Facebook Id: ", facebookId.toString())
                } else {
                    Log.i("Facebook Id: ", "Not exists")
                }
                // Facebook Name
                if (jsonObject.has("name")) {
                    facebookName = jsonObject.getString("name")
                    Log.i("Facebook Name: ", facebookName)
                } else {
                    Log.i("Facebook Name: ", "Not exists")
                }

                // Facebook Email
                if (jsonObject.has("email")) {
                    facebookEmail = jsonObject.getString("email")
                    Log.i("Facebook Email: ", facebookEmail)
                } else {
                    Log.i("Facebook Email: ", "Not exists")
                }

                val user = hashMapOf("id" to facebookId, "name" to facebookName, "email" to facebookEmail, "type" to "facebook")

                // Add a new document with a generated ID
                db?.collection("user")?.document("fb_$facebookId")
                    ?.set(user)
                    ?.addOnSuccessListener {
                        Log.d(
                            "my Tag",
                            "DocumentSnapshot successfully written!"
                        )
                    }
                    ?.addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }

            }).executeAsync()

    }

    //endregion

    //region Google Login helper
    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(
            signInIntent, RC_SIGN_IN
        )
    }

    private fun isSignedIn(context: Context): Boolean {
        return GoogleSignIn.getLastSignedInAccount(context) != null
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        var googleId : String? = ""
        var googleFirstName : String? = ""
        var googleLastName : String? = ""
        var googleEmail : String? = ""
        try {
            val account = completedTask.getResult(
                ApiException::class.java)
                // Signed in successfully
            googleId = account?.id ?: ""
            Log.i("Google ID", googleId)

            googleFirstName = account?.givenName ?: ""
            Log.i("Google First Name", googleFirstName)

            googleLastName = account?.familyName ?: ""
            Log.i("Google Last Name", googleLastName)

            googleEmail = account?.email ?: ""
            Log.i("Google Email", googleEmail)

            val googleProfilePicURL = account?.photoUrl.toString()
            Log.i("Google Profile Pic URL", googleProfilePicURL)

            val googleIdToken = account?.idToken ?: ""
            Log.i("Google ID Token", googleIdToken)

        } catch (e: ApiException) {
            // Sign in was unsuccessful
            Log.e(
                "failed code=", e.statusCode.toString()
            )
        }

        val userName = "$googleFirstName $googleLastName"
        val user = hashMapOf("id" to googleId, "name" to userName, "email" to googleEmail, "type" to "google")
        // Add a new document with a generated ID
        db?.collection("user")?.document("google_$googleId")
            ?.set(user)
            ?.addOnSuccessListener {
                Log.d(
                    "my Tag",
                    "DocumentSnapshot successfully written!"
                )
            }
            ?.addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }

    }

    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                // Update your UI here
            }
    }

    private fun revokeAccess() {
        mGoogleSignInClient.revokeAccess()
            .addOnCompleteListener(this) {
                // Update your UI here
            }
    }

    //endregion
}
