package threona.kannard.healthylife

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

class TimeUsageActivity : AppCompatActivity(){
    private val timeFileName = "usage_history.txt"
    private lateinit var data : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_usage)
        findViewById<TextView>(R.id.average_time).text = averageUsageTime().toString()
        lateinit var dataArray : Array<DataPoint>
        for(x in 0..9){
            dataArray[x] = DataPoint(x.toDouble(), data[x].substringAfter(":").toDouble())
        }
        val graph : GraphView = findViewById(R.id.graph)
        val series : LineGraphSeries<DataPoint> = LineGraphSeries<DataPoint>(dataArray)
        graph.addSeries(series)
    }
    private fun loadData(){
        var dataLoad : String
        var file = File(filesDir.absolutePath, timeFileName)
        var fileInputStream: FileInputStream? = null
        fileInputStream = openFileInput(timeFileName)
        val inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
        val stringBuilder: StringBuilder = StringBuilder()
        var text: String? = null
        while ({ text = bufferedReader.readLine(); text }() != null) {
            stringBuilder.append(text)
        }
        dataLoad = (stringBuilder.toString())
        data  = dataLoad.split(";").toTypedArray()
    }
    private fun averageUsageTime() : Float{
        var average : Float = 0.toFloat()
        for(time in data){
            average += time.substringAfter(":").toFloat()
        }
        return average/10
    }
}