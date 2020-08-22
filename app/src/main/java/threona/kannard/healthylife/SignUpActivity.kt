package threona.kannard.healthylife

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.regex.Matcher
import java.util.regex.Pattern


class SignUpActivity : AppCompatActivity() {

    private var textInputUserName : TextInputLayout? = null
    private var textInputPassword : TextInputLayout? = null
    private var textInputConfirmPass : TextInputLayout? = null
    private var textInputEmail : TextInputLayout? = null

    // Access a Cloud Firestore instance from your Activity
    private var db: FirebaseFirestore? = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        textInputUserName = findViewById(R.id.textInput_user_name)
        textInputPassword = findViewById(R.id.textInput_password)
        textInputConfirmPass = findViewById(R.id.textInput_confirm_password)
        textInputEmail = findViewById(R.id.textInput_email)
        val checkBox = findViewById<AppCompatCheckBox>(R.id.term_of_services)

        val signUpBtn = findViewById<Button>(R.id.Sign_up_btn)
        signUpBtn.setOnClickListener {
            if(!checkBox.isChecked)
            {
                Toast.makeText(this,"Please accept the terms of our services", Toast.LENGTH_LONG).show()
            }
            else {
                if (!validateConfirmPassword() or !validateEmail() or !validatePassword() or !validateUserName())
                {
                    return@setOnClickListener
                }
                else
                {
                    val source = "0123456789"
                    val outputStrLength = 13
                    val randomString = (1..outputStrLength)
                        .map { i -> kotlin.random.Random.nextInt(0, source.length) }
                        .map(source::get)
                        .joinToString("")

                    Toast.makeText(this, randomString, Toast.LENGTH_SHORT)

//                    // Add a new document with a generated ID
//                    db?.collection("user")?.document("google_$googleId")
//                        ?.set(user)
//                        ?.addOnSuccessListener {
//                            Log.d(
//                                "my Tag",
//                                "DocumentSnapshot successfully written!"
//                            )
//                        }
//                        ?.addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
                }
            }
        }
    }

    //region check valid SignUp Info
    private fun validateUserName(): Boolean {
        val userNameInput = textInputUserName?.editText?.text.toString().trim()

        if(userNameInput.isEmpty()){
            textInputUserName?.error = "This field can't be empty."
            return false
        }
        else {
            if (userNameInput.contains("[\$&+,:;=\\\\\\\\?@#|/'<>.^*()%!-]")) {
                textInputUserName?.error = "The user name can't contain special character"
                return false
            } else {
                textInputUserName?.error = null
                textInputUserName?.isErrorEnabled = false
            }
        }
        return true
    }

    private fun validatePassword() : Boolean {
        val passWordInput = textInputPassword?.editText?.text.toString().trim()

        if(passWordInput.isEmpty())
        {
            textInputPassword?.error = "This field can't be empty"
            return false
        }else
        {
            if(passWordInput.length > 16)
            {
                textInputPassword?.error = "The maximum length of the password is 16"
                return false
            }
            else{
                val pattern: Pattern
                val matcher: Matcher
                val passwordPattern =
                    "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
                pattern = Pattern.compile(passwordPattern)
                matcher = pattern.matcher(passWordInput)
                if (!matcher.matches()) {
                    textInputPassword?.error = "The password must include uppercase letters, lowercase letters, numbers and special characters."
                    return false
                } else {
                    textInputPassword?.error = null
                    textInputPassword?.isErrorEnabled = false
                }
            }
        }
        return true
    }

    private fun validateConfirmPassword() : Boolean {

        val confirmPassword = textInputConfirmPass?.editText?.text.toString().trim()

        if(confirmPassword.isEmpty())
        {
            textInputConfirmPass?.error = "This field can't be empty."
            return false
        }else {
            val pass = textInputPassword?.editText?.text.toString().trim()
            if(confirmPassword != pass)
            {
                textInputConfirmPass?.error = "It must be the same as the password."
                return false
            }
            else {
                textInputConfirmPass?.error = null
                textInputConfirmPass?.isErrorEnabled = false
            }
        }
        return true
    }

    private fun validateEmail() : Boolean {
        val email = textInputEmail?.editText?.text.toString().trim()

        val userRef = db?.collection("user")


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
                    textInputEmail?.error = null
                    textInputEmail?.isErrorEnabled = false
                }
            }
        return true
    }
    //endregion
}