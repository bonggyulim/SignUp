package com.example.signup.presentation

import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.signup.R

fun EditText.addValidationTextWatcher(
    validator: (String) -> Boolean,
    errorText: String,
    validationMap: MutableMap<EditText, Boolean>,
    checkButton: Button? = null,
    errorTextView: TextView? = null // 오류 메시지를 표시할 TextView
) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val text = s.toString()
            val isValid = validator(text)
            validationMap[this@addValidationTextWatcher] = isValid

            // 다른 TextView에 오류 메시지 표시
            errorTextView?.text = if (isValid) "" else errorText

            // 버튼 활성화 및 배경색 변경 로직
            checkButton?.let { button ->
                val allValid = validationMap.values.all { it } && validationMap.size >= 3
                button.isEnabled = allValid
                button.background = if (allValid) {
                    ContextCompat.getDrawable(context, R.drawable.bg_primary_radius)
                } else {
                    ContextCompat.getDrawable(context, R.drawable.bg_enable_radius)
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    })
}
fun EditText.addValidationTextWatcher2(
    errorText: String,
    validationMap: MutableMap<EditText, Boolean>,
    checkButton: Button? = null,
    editTextView: EditText,
    errorTextView: TextView? = null // 오류 메시지를 표시할 TextView
) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val isValid = this@addValidationTextWatcher2.text.toString() == editTextView.text.toString()
            validationMap[this@addValidationTextWatcher2] = isValid

            // 오류 메시지 표시
            errorTextView?.text = if (isValid) "" else errorText

            // 버튼 활성화 및 배경색 변경 로직
            checkButton?.let { button ->
                val allValid = validationMap.values.all { it } && validationMap.size >= 3
                button.isEnabled = allValid
                button.background = if (allValid) {
                    ContextCompat.getDrawable(context, R.drawable.bg_primary_radius)
                } else {
                    ContextCompat.getDrawable(context, R.drawable.bg_enable_radius)
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    })
}

fun EditText.addValidationTextWatcher3(
    validationMap: MutableMap<EditText, Boolean>,
    checkButton: Button? = null
) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val isValid = this@addValidationTextWatcher3.text.isNotEmpty()
            validationMap[this@addValidationTextWatcher3] = isValid

                checkButton?.let { button ->
                    val allValid = validationMap.values.all { it } && validationMap.size >= 2
                    button.isEnabled = allValid
                    button.background = if (allValid) {
                        ContextCompat.getDrawable(context, R.drawable.bg_primary_radius)
                    } else {
                        ContextCompat.getDrawable(context, R.drawable.bg_enable_radius)
                    }
                }
        }

        override fun afterTextChanged(s: Editable?) {}
    })
}

fun EditText.addValidationTextWatcher4(
    num: Int,
    errorText: String,
    validationMap: MutableMap<EditText, Boolean>,
    checkButton: Button? = null,
    errorTextView: TextView? = null // 오류 메시지를 표시할 TextView
) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val isValid = this@addValidationTextWatcher4.text.count() == num
            validationMap[this@addValidationTextWatcher4] = isValid

            errorTextView?.text = if (isValid) "" else errorText

            checkButton?.let { button ->
                val allValid = validationMap.values.all { it }
                button.isEnabled = allValid
                button.background = if (allValid) {
                    ContextCompat.getDrawable(context, R.drawable.bg_primary_radius)
                } else {
                    ContextCompat.getDrawable(context, R.drawable.bg_enable_radius)
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    })
}


fun String.isValidEmail(): Boolean {
    val emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    return this.matches(emailPattern.toRegex())
}

fun String.isValidPassword(): Boolean { //8~20, 영문 + 숫자
    val passwordPattern = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{8,20}$"
    return this.matches(passwordPattern.toRegex())
}

fun String.isValidPhoneNumber(): Boolean {
    val phoneNumberPattern = "^\\d{3}-\\d{4}-\\d{4}$"
    return this.matches(phoneNumberPattern.toRegex())
}