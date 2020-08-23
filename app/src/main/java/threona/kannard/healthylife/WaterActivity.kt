package threona.kannard.healthylife

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.*
import java.time.LocalDate

class WaterActivity : AppCompatActivity() {

    val currentDate = LocalDate.now()

    private var total: Float = 0f;
    val Waterfilename: String= "info_water.txt"
    var Waterdata: String ="0.00"
    var dataLoad: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water)

        val btn_water_icon = findViewById<Button>(R.id.add_water)

        var file = File(getFilesDir().getAbsolutePath(), Waterfilename)

        if(Waterfilename.trim()!=""){
            if (!file.exists())
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
        val res = dataLoad.split(";");

        if(res.size == 2) {

            //get current date
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
            //calculate for edit text
            if (total<2){
                val total_water = findViewById<TextView>(R.id.Water_count)
                total += 0.25f
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
    }

    private fun SaveString(total: Float, dateInString: String): String {
        return "$total;$dateInString"
    }
}