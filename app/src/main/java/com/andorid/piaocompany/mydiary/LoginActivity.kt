package com.andorid.piaocompany.mydiary

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.andorid.piaocompany.mydiary.Utility.RC_GOOGLE_SIGN_IN
import com.andorid.piaocompany.mydiary.Utility.getTodayString2
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity()
{
    private lateinit var gso : GoogleSignInOptions
    private lateinit var gsc : GoogleSignInClient
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var account : GoogleSignInAccount



    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser?.uid?:null
        if(user != null)
        {
            goMainActivity(user)
        }
    }

    private fun goMainActivity(userEmail: String) {

        FirebaseDatabase.getInstance().getReference().child("users").child("userId").setValue(userEmail)
                .addOnSuccessListener {
                    val intent : Intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("userID", userEmail)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { Toast.makeText(this,"잠시 후 다시 이용해 주시기 바랍니다 :( ", Toast.LENGTH_SHORT). show()}
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupUI()
        configureGoogleSignIn()

    }

    private fun setupUI() {
        firebaseAuth = FirebaseAuth.getInstance()
        btn_googleSign_in.setOnClickListener { signIn() }
    }


    private fun signIn() {
        val signInIntent : Intent = gsc.signInIntent
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

    private fun configureGoogleSignIn() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        gsc = GoogleSignIn.getClient(this, gso)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_GOOGLE_SIGN_IN) {
            val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch ( e: ApiException) {
                Toast.makeText(this, "${e.statusCode}\n${e.message}",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful) {
                val user : String? = FirebaseAuth.getInstance().currentUser?.uid?:null
                if(user != null)
                {
                    goMainActivity(user)
                }
            }
            else {
                Toast.makeText(this, "잠시 후 다시 이용해주시기 바랍니다.", Toast.LENGTH_SHORT).show()
            }
    }
    }
}
