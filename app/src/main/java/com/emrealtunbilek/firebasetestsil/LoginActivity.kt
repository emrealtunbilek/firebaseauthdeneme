package com.emrealtunbilek.firebasetestsil

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception


class LoginActivity : AppCompatActivity() {

    //Firebase
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupFirebase()

        btnEmail_sign_in_button.setOnClickListener {
            if (etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()) {

                showProgressbar()

                FirebaseAuth.getInstance().signInWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
                        .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                            override fun onComplete(p0: Task<AuthResult>) {
                                closeProgressBar()
                               // Toast.makeText(this@LoginActivity, "GİRİŞ YAPILDI:" + p0.isSuccessful, Toast.LENGTH_SHORT).show()
                            }

                        })
                        .addOnFailureListener(object : OnFailureListener {
                            override fun onFailure(p0: Exception) {
                                Toast.makeText(this@LoginActivity, "GİRİŞ YAPILAMADI:" + p0.message, Toast.LENGTH_SHORT).show()
                                closeProgressBar()
                            }

                        })


            } else {
                Toast.makeText(this, "Tüm alanları doldurun", Toast.LENGTH_LONG).show()
            }
        }


        tvlink_register.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        tvresend_verification_email.setOnClickListener {
            val dialog = ResendVerificationDialog()
            dialog.show(supportFragmentManager, "dialog_resend_email_verification")
        }
    }

    //FIREBASE SETUP
    private fun setupFirebase() {

        mAuthListener = object : FirebaseAuth.AuthStateListener {

            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = p0.currentUser

                if (user != null) {

                    if (user.isEmailVerified) {
                        Toast.makeText(this@LoginActivity, "MAIL ONAYLI BIRAKIN GIRSIN : " + user.uid, Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@LoginActivity, "Mailinizi onaylayın : ", Toast.LENGTH_LONG).show()
                        FirebaseAuth.getInstance().signOut()
                    }


                } else {
                    Toast.makeText(this@LoginActivity, "ÇIKIŞ YAPILDI : ", Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener)
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener)
    }

    public fun showProgressbar() {
        progressBar.visibility = View.VISIBLE


    }

    public fun closeProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }
}
