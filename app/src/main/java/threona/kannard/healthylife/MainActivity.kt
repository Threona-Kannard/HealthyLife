package threona.kannard.healthylife

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Button Water
        val waterButton = findViewById<Button>(R.id.water_activity_button)
        waterButton?.setOnClickListener()
        {
            var intent: Intent = Intent(applicationContext, WaterActivity::class.java)
            startActivity(intent)
        }

        val foodButton = findViewById<Button>(R.id.food_activity)
        foodButton?.setOnClickListener {
            val intent : Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val sleepButton = findViewById<Button>(R.id.sleep_activity_button)
        foodButton?.setOnClickListener {
            val intent : Intent = Intent(this,SleepActivity::class.java)
            startActivity(intent)
        }
    }
}