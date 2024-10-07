package com.example.tehtava3

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.provider.AlarmClock
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val openBrowserButton: Button = findViewById(R.id.openBrowserButton)
        val showLocationButton: Button = findViewById(R.id.showLocationButton)
        val setAlarmButton: Button = findViewById(R.id.setAlarmButton)
        val dialPhoneButton: Button = findViewById(R.id.dialPhoneButton)

        openBrowserButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))
            startActivity(browserIntent)
        }

        showLocationButton.setOnClickListener {
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=latitude,longitude"))
            startActivity(mapIntent)
        }

        setAlarmButton.setOnClickListener {
            val alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, "Alarm Title")
                putExtra(AlarmClock.EXTRA_HOUR, 7) // Esimerkki: Aseta herätys kello 7
                putExtra(AlarmClock.EXTRA_MINUTES, 30)
            }
            startActivity(alarmIntent)
        }

        dialPhoneButton.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:123456789"))
            startActivity(dialIntent)
        }
    }
}
