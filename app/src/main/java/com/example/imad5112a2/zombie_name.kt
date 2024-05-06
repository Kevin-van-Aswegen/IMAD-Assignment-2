package com.example.imad5112a2

import android.annotation.SuppressLint
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class zombie_name : AppCompatActivity()
{

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_zombie_name)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main))
        { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nextbutton = findViewById<Button>(R.id.nextbutton)
        val backbutton = findViewById<Button>(R.id.backbutton)
        val zombiename = findViewById<EditText>(R.id.zombiename)
        val usersName = findViewById<TextView>(R.id.usersnameTextview)

        val intent = getIntent()

        val username = intent.getStringExtra("username")

        usersName.text = ""+username+""

        nextbutton.setOnClickListener {

            var zombiename = zombiename.text.toString()

            val intent = Intent(this,main_page_stats::class.java)

            intent.putExtra("zombiename", zombiename)

            startActivity(intent)
        }


        backbutton.setOnClickListener {
            val intent = Intent(this,login_page::class.java)
            startActivity(intent)
            
        }

        Thread{
            val source:ImageDecoder.Source = ImageDecoder.createSource(
                resources, R.drawable.giga_football_zombie_idle
            )
            val drawable:Drawable = ImageDecoder.decodeDrawable(source)

            val imageView = findViewById<ImageView>(R.id.zombie_image)
            imageView.post{
                imageView.setImageDrawable(drawable)
                (drawable as? AnimatedImageDrawable)?.start()
            }
        }.start()

    }
}


