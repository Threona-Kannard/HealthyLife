package threona.kannard.healthylife

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class WaterSetting : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.water_setting_layout)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        val calendar = Calendar.getInstance();

        val intent = Intent(this, Notification_reciever::class.java)

        val pendingIntent:PendingIntent = PendingIntent.getBroadcast(applicationContext,0, intent,PendingIntent.FLAG_UPDATE_CURRENT)

        val alm_manager:AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager


        val btn_default = findViewById<Button>(R.id.btn_default)
        btn_default?.setOnClickListener()
        {
            calendar.set(0,0,0,7,30,0)
            alm_manager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)
            Toast.makeText(applicationContext,"Set notification default(7:30) " + calendar.timeInMillis, Toast.LENGTH_LONG).show()
        }

        val btn_save = findViewById<Button>(R.id.btn_save_noti)
        btn_save?.setOnClickListener()
        {
            val timeset = findViewById<TimePicker>(R.id.timeset)
            calendar.set(0,0,0,timeset.hour,timeset.minute,0)
            alm_manager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)
            Toast.makeText(applicationContext,"Set notification at " + calendar.timeInMillis, Toast.LENGTH_LONG).show()
        }

        val btn_update = findViewById<Button>(R.id.btn_update_noti)
        btn_update?.setOnClickListener()
        {
            val timeset = findViewById<TimePicker>(R.id.timeset)
            calendar.set(0,0,0,timeset.hour,timeset.minute,0)
            alm_manager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)
            Toast.makeText(applicationContext,"Set notification at " + calendar.timeInMillis, Toast.LENGTH_LONG).show()
        }

        val btn_clear = findViewById<Button>(R.id.btn_clear_noti)
        btn_clear?.setOnClickListener()
        {
            //alm_manager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+ 300,pendingIntent)
            alm_manager.cancel(pendingIntent)
            Toast.makeText(applicationContext,"Clear all notification", Toast.LENGTH_LONG).show()
        }
    }
}