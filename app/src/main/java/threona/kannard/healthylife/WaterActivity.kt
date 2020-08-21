package threona.kannard.healthylife

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

class WaterActivity : AppCompatActivity() {

    private var total: Float = 0f;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water)



        val btn_water_icon = findViewById<Button>(R.id.add_water)
        btn_water_icon?.setOnClickListener()
        {
            if ()
            if (total<2){
            val total_water = findViewById<TextView>(R.id.Water_count)
            total = total+0.25f
            total_water.setText(total.toString())}
        }
    }
}