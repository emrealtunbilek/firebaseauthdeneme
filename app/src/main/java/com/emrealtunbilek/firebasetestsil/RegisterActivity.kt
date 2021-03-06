package com.emrealtunbilek.firebasetestsil

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*
import android.content.Intent



class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_register.setOnClickListener {

            //tüm alanlar doldurulmuş
            if (et_input_email.text.isNotEmpty() && et_input_password.text.isNotEmpty() && et_input_confirm_password.text.isNotEmpty()) {

                if (et_input_password.text.toString().equals(et_input_confirm_password.text.toString())) {

                    //Toast.makeText(this,"Sorun yok ekleme yapılabilir",Toast.LENGTH_LONG).show()
                    registerNewEmail(et_input_email.text.toString(), et_input_password.text.toString())

                } else {
                    Toast.makeText(this, "Parolalar eşleşmiyor", Toast.LENGTH_LONG).show()
                }


            }
            //tüm alanlar doldurulmamış
            else {
                Toast.makeText(this, "Boş alanları doldurun", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun registerNewEmail(email: String, password: String) {
        showProgressbar()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener { v ->
            Toast.makeText(this, "BASARILI :" + v.isSuccessful, Toast.LENGTH_SHORT).show()

            if (v.isSuccessful) {
                Toast.makeText(this, "OTURUM  :" + FirebaseAuth.getInstance().currentUser?.uid, Toast.LENGTH_LONG).show()

                sendVerificationEmail()

                FirebaseAuth.getInstance().signOut()
                reDirectLoginPage()


            } else {
                Toast.makeText(this, "EKLENEMEDİ :" + v.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
        closeProgressBar()
    }

    fun sendVerificationEmail() {

        var user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            user?.sendEmailVerification()?.addOnCompleteListener(object : OnCompleteListener<Void> {
                override fun onComplete(p0: Task<Void>) {
                    if (p0.isSuccessful) {
                        Toast.makeText(this@RegisterActivity, "Onaylama maili atıldı lütfen kontrol ediniz", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@RegisterActivity, "Sorun oldu mail atılamadı", Toast.LENGTH_LONG).show()
                    }
                }

            })
        }


    }

    public fun showProgressbar() {
        progressBar.visibility = View.VISIBLE


    }

    public fun closeProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    fun reDirectLoginPage(){
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
