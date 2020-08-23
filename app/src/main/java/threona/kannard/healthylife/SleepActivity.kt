package threona.kannard.healthylife

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader


class SleepActivity : AppCompatActivity() {
    private lateinit var mChart: LineChart
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_sleep)
        mChart = findViewById(R.id.chart)
        mChart.setPinchZoom(true)

        val loadData = loadData()
        val arrValues = dataVal(loadData)

    }

    private fun dataVal(list: MutableList<String>) : ArrayList<Entry> {
        var arrValues : ArrayList<Entry> = ArrayList()
        val iterator = list.iterator()
        while(iterator.hasNext()){
            var str = iterator.next()
            val existedData = str.split(",")
            val existedDate = existedData[0].split("/")
            val existedTime = existedData[1].split(":")
            val month = existedDate[1]
            val day = existedDate[0].padStart(2, '0')
            val date = "$month.$day".toFloat()
            val time = existedTime[0].toFloat() + existedTime[1].toFloat()/60
            arrValues.add(Entry(date, time))
        }
        return arrValues
    }

    private fun loadData(): MutableList<String> {
        val sleepFileName = "sleep_history.txt"
        var file = File(filesDir.absolutePath, sleepFileName)
        var dataLoad: String
        if(sleepFileName.trim()!=""){
            if (!file.exists())
            {
                openFileOutput(sleepFileName, Context.MODE_PRIVATE)
            }
            var fileInputStream: FileInputStream? = null
            fileInputStream = openFileInput(sleepFileName)
            val inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String? = null
            while ({ text = bufferedReader.readLine(); text }() != null) {
                stringBuilder.append(text)
            }
            dataLoad = (stringBuilder.toString())
        }else{
            throw error("CANNOT FIND FILE")
        }
        return dataLoad.split(";").toMutableList()
    }
}

class MyMarkerView(context: Context?, layoutResource: Int) :
    MarkerView(context, layoutResource) {
    private val tvContent: TextView = findViewById(R.id.tvContent)

    override fun refreshContent(e: Entry, highlight: Highlight?) {
        if (e is CandleEntry) {
            tvContent.text = Utils.formatNumber(e.high, 0, true)
        } else {
            tvContent.text = Utils.formatNumber(e.y, 0, true)
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }
}
