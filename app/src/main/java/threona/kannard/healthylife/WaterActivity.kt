package threona.kannard.healthylife

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.io.*
import java.time.LocalDate

class WaterActivity : AppCompatActivity() {

    val currentDate = LocalDate.now()

    private var total: Float = 0f
    val Waterfilename: String= "info_water.txt"
    var Waterdata: String ="0.00"
    var dataLoad: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water)

        val btn_water_icon = findViewById<Button>(R.id.add_water)

        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, WaterActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        var builder = NotificationCompat.Builder(this, R.string.CHANNEL_ID.toString())
            .setSmallIcon(R.drawable.water_icon)
            .setContentTitle("Water")
            .setContentText("Drink Now !!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        var file = File(filesDir.absolutePath, Waterfilename)

        if(Waterfilename.trim()!=""){
            if (file.exists() == false)
            {
                openFileOutput(Waterfilename, Context.MODE_PRIVATE)
            }
            var fileInputStream: FileInputStream? = null
            fileInputStream = openFileInput(Waterfilename)
            val inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String? = null
            while ({ text = bufferedReader.readLine(); text }() != null) {
                stringBuilder.append(text)
            }
            dataLoad = (stringBuilder.toString())
        }else{
            Toast.makeText(applicationContext,"file name cannot be blank",Toast.LENGTH_LONG).show()
        }

        //work with load data
        val res = dataLoad.split(";")

        if(res.size == 2) {

            //getcurrent date
            var eventDate: LocalDate = LocalDate.parse(res[1])
            var dateMargin = currentDate.isAfter(eventDate)

            if (dateMargin) {
                total = 0f
            }
            else {
                total = res[0].toFloat()
                findViewById<TextView>(R.id.Water_count).text = total.toString()
            }
        }

        btn_water_icon?.setOnClickListener()
        {
            //calculate for edittext
            if (total<2){
                val total_water = findViewById<TextView>(R.id.Water_count)
                total = total+0.25f
                total_water.text = total.toString()
            }

            Waterdata = SaveString(total,currentDate.toString())

            //write data


            try {
                val fileOutputStream: FileOutputStream
                fileOutputStream = openFileOutput(Waterfilename, Context.MODE_PRIVATE)
                fileOutputStream.write(Waterdata.toByteArray())
            } catch (e: FileNotFoundException){
                e.printStackTrace()
            }catch (e: NumberFormatException){
                e.printStackTrace()
            }catch (e: IOException){
                e.printStackTrace()
            }catch (e: Exception){
                e.printStackTrace()
            }
            Toast.makeText(applicationContext,Waterdata, Toast.LENGTH_LONG).show()
        }

        val btn_5sec = findViewById<Button>(R.id.timer5sec)

        btn_5sec?.setOnClickListener()
        {
            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(100, builder.build())
            }
        }
    }


    private fun SaveString(total: Float, dateInString: String): String {
        return total.toString() + ";" + dateInString
    }
}