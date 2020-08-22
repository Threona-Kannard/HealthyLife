package threona.kannard.healthylife

import android.R.attr.password
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Matcher
import java.util.regex.Pattern


class SignUpActivity : AppCompatActivity() {

    private var textInputUserName : TextInputLayout? = null
    private var textInputPassword : TextInputLayout? = null
    private var textInputConfirmPass : TextInputLayout? = null
    private var textInputEmail : TextInputLayout? = null

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

        if(email.isEmpty())
        {
            textInputEmail?.error = "This field can't be empty"
            return false
        }
        else{
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                textInputEmail?.error = "It should be a valid email."
                return false
            }
            else {
                textInputEmail?.error = null
                textInputEmail?.isErrorEnabled = false
            }
        }
        return true
    }
    //endregion
}