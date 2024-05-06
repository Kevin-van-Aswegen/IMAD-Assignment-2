package com.example.imad5112a2

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.WindowInsetsCompat
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat

class login_page : AppCompatActivity()
{


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main))
        { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val login_button = findViewById<Button>(R.id.login_button)
        val login_text = findViewById<TextView>(R.id.logintextView)
        val users_name = findViewById<EditText>(R.id.users_name)

        login_button.setOnClickListener {

            var username = users_name.text.toString()

            val intent = Intent(this,zombie_name::class.java)

            intent.putExtra("username", username)

            startActivity(intent)
        }

    }
}
