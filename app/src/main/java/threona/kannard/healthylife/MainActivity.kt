package threona.kannard.healthylife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Button Water
        val button = findViewById<Button>(R.id.water_activity_button)
        button?.setOnClickListener()
        {
            var intent: Intent = Intent(applicationContext, WaterActivity::class.java)

            startActivity(intent)
        }
    }
}