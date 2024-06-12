package com.bengisusahin.e_commerce.dialog

import android.widget.EditText
import androidx.fragment.app.Fragment
import com.bengisusahin.e_commerce.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton

// This extension func if the same dialog is going to be used in multiple fragments
// We can write it once and use it in multiple times
fun Fragment.setUpBottomSheetDialog(
    onSendClick: (String) -> Unit
){
    val dialog = BottomSheetDialog(requireContext())
    val view = layoutInflater.inflate(R.layout.reset_password_dialog, null)
    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val edEmail = view.findViewById<EditText>(R.id.etResetPassword)
    val btnSend = view.findViewById<MaterialButton>(R.id.btnSendResetPassword)
    val btnCancel = view.findViewById<MaterialButton>(R.id.btnCancelResetPassword)

    btnSend.setOnClickListener {
        // send reset password email
        val email = edEmail.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }

    btnCancel.setOnClickListener {
        dialog.dismiss()
    }
}