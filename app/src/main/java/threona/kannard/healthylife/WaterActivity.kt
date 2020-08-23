package threona.kannard.healthylife

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.*
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class WaterActivity : AppCompatActivity() {

    private var total: Float = 0f;
    val Waterfilename: String= "info_water.txt"
    var Waterdata: String ="0.00"
    var dataLoad: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water)



        val btn_water_icon = findViewById<Button>(R.id.add_water)
        btn_water_icon?.setOnClickListener()
        {

            if(Waterfilename.toString()!=null && Waterfilename.toString().trim()!=""){
                var fileInputStream: FileInputStream? = null
                fileInputStream = openFileInput(Waterfilename)
                var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
                val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
                val stringBuilder: StringBuilder = StringBuilder()
                var text: String? = null
                while ({ text = bufferedReader.readLine(); text }() != null) {
                    stringBuilder.append(text)
                }
                dataLoad = (stringBuilder.toString()).toString()
            }else{
                Toast.makeText(applicationContext,"file name cannot be blank",Toast.LENGTH_LONG).show()
            }

            var res = dataLoad.split(";");

            val date = getCurrentDateTime()
            val dateInString = date.toString("yyyy/MM/dd HH:mm:ss")

            if(ZonedDateTime.parse(
                dateInString ,
                DateTimeFormatter.ofPattern( "yyyy/MM/dd HH:mm:ss" )
                    .withLocale( Locale.ENGLISH )
            )
                .isAfter(
                    ZonedDateTime.parse(
                        res[1] ,
                        DateTimeFormatter.ofPattern( "yyyy/MM/dd HH:mm:ss" )
                            .withLocale( Locale.ENGLISH )
                    )
                ))
            {total=0f
            }

            //calculate for edittext
            if (total<2){
            val total_water = findViewById<TextView>(R.id.Water_count)
            total = total+0.25f
            total_water.setText(total.toString())}


            Waterdata = SaveString(total,dateInString)

            //write data
            val fileOutputStream: FileOutputStream

            try {
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
            Toast.makeText(applicationContext,"+ 1 glass of water", Toast.LENGTH_LONG).show()
        }
    }

    private fun SaveString(total: Float, dateInString: String): String {
        return total.toString() + ";" + dateInString
    }
}

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}