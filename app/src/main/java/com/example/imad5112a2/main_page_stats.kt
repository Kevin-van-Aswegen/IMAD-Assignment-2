package com.example.imad5112a2

import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job


class main_page_stats : AppCompatActivity()
{

    private lateinit var happiness_number: TextView
    private lateinit var hunger_number: TextView
    private lateinit var decay_number: TextView

    private var currentHappiness = 100
    private var currentHunger = 100

    // Variables to store the displayed values
    private var displayedHappiness = 100
    private var displayedHunger = 100

    private val decreaseRate = 2
    private val delayMillis = 1500L
    private val decreaseJobMap = mutableMapOf<TextView, Job>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_page_stats)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main))
        { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backbutton = findViewById<Button>(R.id.backbutton)
        val zombie_Name = findViewById<TextView>(R.id.zombie_name)
        val play = findViewById<Button>(R.id.playbutton)
        val feed = findViewById<Button>(R.id.feedbutton)

        val intent = getIntent()
        val zombiename = intent.getStringExtra("zombiename")
        zombie_Name.text = ""+zombiename+""

        val initialValue1 = 100
        val initialValue2 = 100

        Thread{
            val source:ImageDecoder.Source = ImageDecoder.createSource(
                resources, R.drawable.giga_football_zombie_idle
            )
            val drawable: Drawable = ImageDecoder.decodeDrawable(source)

            val imageView = findViewById<ImageView>(R.id.zombie_image)
            imageView.post{
                imageView.setImageDrawable(drawable)
                (drawable as? AnimatedImageDrawable)?.start()
            }
        }.start()

        backbutton.setOnClickListener{
            val intent = Intent(this,zombie_name::class.java)
            startActivity(intent)

            // Load zombie idle animation
            loadZombieAnimation(R.drawable.giga_football_zombie_idle)
        }

        play.setOnClickListener {
            increaseNumbersBy(30, isHappiness = true)
            resetDecreaseCoroutine(happiness_number)
            loadZombieAnimation(R.drawable.giga_football_zombie_running)
            // Load zombie idle animation after 3 seconds
            Handler().postDelayed({
                loadZombieAnimation(R.drawable.giga_football_zombie_idle)
            }, 3000)
        }

        feed.setOnClickListener {
            increaseNumbersBy(30, isHappiness = false)
            resetDecreaseCoroutine(hunger_number)
            loadZombieAnimation(R.drawable.eating_zombie_football)
            // Load zombie idle animation after 3 seconds
            Handler().postDelayed({
                loadZombieAnimation(R.drawable.giga_football_zombie_idle)
            }, 3000)
        }

        happiness_number = findViewById<TextView>(R.id.happinessnumber)
        hunger_number = findViewById<TextView>(R.id.hungernumber)
        decay_number = findViewById<TextView>(R.id.decaynumber)

        CoroutineScope(Dispatchers.Main).launch {
            decreaseNumber(initialValue1, happiness_number)
        }

        CoroutineScope(Dispatchers.Main).launch {
            decreaseNumber(initialValue2, hunger_number)
        }

        CoroutineScope(Dispatchers.Main).launch {
            calculateAndDisplayAverage()
        }
    }

    private fun loadZombieAnimation(drawableId: Int) {
        val source = ImageDecoder.createSource(resources, drawableId)
        val drawable = ImageDecoder.decodeDrawable(source)
        val imageView = findViewById<ImageView>(R.id.zombie_image)
        imageView.setImageDrawable(drawable)
        (drawable as? AnimatedImageDrawable)?.start()
    }

    private fun resetDecreaseCoroutine(textView: TextView) {
        decreaseJobMap[textView]?.cancel()
        decreaseNumber(textView)
    }

    private fun increaseNumbersBy(increment: Int, isHappiness: Boolean) {
        val textView = if (isHappiness) happiness_number else hunger_number
        // Increment the appropriate value based on the button clicked
        val currentValue = textView.text.toString().toIntOrNull() ?: 0
        val newValue = (currentValue + increment).coerceAtMost(100)
        textView.text = newValue.toString()
    }

    private fun decreaseNumber(textView: TextView) {
        val initialValue = textView.text.toString().toIntOrNull() ?: 0
        decreaseJobMap[textView] = CoroutineScope(Dispatchers.Main).launch {
            var currentValue = initialValue
            while (currentValue > 0) {
                currentValue -= decreaseRate
                currentValue = maxOf(currentValue, 0)
                textView.text = currentValue.toString()
                delay(delayMillis)
            }
        }
    }
    private suspend fun decreaseNumber(initialValue: Int, textView: TextView) {
        var currentValue = initialValue
        while (currentValue > 0) {
            currentValue -= decreaseRate
            currentValue = maxOf(currentValue, 0)
            textView.text = currentValue.toString()
            delay(delayMillis)
        }
    }

    private suspend fun calculateAndDisplayAverage() {
        while (true) {
            // Ensure that displayedHappiness and displayedHunger are up-to-date
            displayedHappiness = happiness_number.text.toString().toIntOrNull() ?: 0
            displayedHunger = hunger_number.text.toString().toIntOrNull() ?: 0

            val sum = displayedHappiness + displayedHunger
            val count = if (displayedHappiness > 0 && displayedHunger > 0) 2 else 0
            val average = if (count > 0) sum.toDouble() / count else 0.0
            decay_number.text = String.format("%.2f", average)

            if (displayedHappiness <= 0 && displayedHunger <= 0) {
                val intent = Intent(this@main_page_stats, death_screen::class.java)
                startActivity(intent)
                break // Exit the loop if both values are less than or equal to 0
            }

            delay(1000)
        }
    }
}

