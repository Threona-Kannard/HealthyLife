package threona.kannard.healthylife

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.*


class SleepCounter : AppCompatActivity() {
    private val timeFileName = "usage_history.txt"
    private var beginTime:Calendar? = null
    private var endTime:Calendar? = null

    fun onChangeState(){

    }
    fun beginCounter(){
        beginTime = Calendar.getInstance()
    }
    fun endCounter(){
        endTime = Calendar.getInstance()
        if (!isUsing() || beginTime == null){
            endTime = null
            beginTime = null
        }else{
            SaveUsageTime(differInTime())
        }
    }

    private fun isUsing(): Boolean {
        val differHour
                = endTime!!.get(Calendar.HOUR_OF_DAY) - beginTime!!.get(Calendar.HOUR_OF_DAY)
        val differMin : Int

        differMin = if(differHour == 0) {
            endTime!!.get(Calendar.MINUTE) - beginTime!!.get(Calendar.MINUTE)
        } else if(differHour == -23 || differHour == 1){
            60 - (endTime!!.get(Calendar.MINUTE) - beginTime!!.get(Calendar.MINUTE))
        } else 3
        return (differMin < 3)
    }

    private fun differInTime() : Float{
        var differHour : Float = (endTime!!.get(Calendar.HOUR_OF_DAY) - beginTime!!.get(Calendar.HOUR_OF_DAY)).toFloat()
        var differMin :Float = if(differHour == 0.toFloat()) {
            endTime!!.get(Calendar.MINUTE) - beginTime!!.get(Calendar.MINUTE).toFloat()
        } else {
            60 - (endTime!!.get(Calendar.MINUTE) - beginTime!!.get(Calendar.MINUTE)).toFloat()
        }
        return differHour + differMin/60
    }

    inline fun <T> MutableList<T>.mapInPlace(mutator: (T)->T) {
        val iterate = this.listIterator()
        while (iterate.hasNext()) {
            val oldValue = iterate.next()
            val newValue = mutator(oldValue)
            if (newValue !== oldValue) {
                iterate.set(newValue)
            }
        }
    }

    private fun SaveUsageTime(differTime: Float) {
        var dataLoad : String
        var file = File(filesDir.absolutePath, timeFileName)
        if (file.exists() == false)
        {
            openFileOutput(timeFileName, Context.MODE_PRIVATE)
        }
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
        var res  = dataLoad.split(";").toTypedArray()

        if(endTime!!.get(Calendar.HOUR_OF_DAY) != 23){
            var data = res[0].substringAfter(":").toFloat()
            data += differInTime()
            val newData = "0:$data"
            res[0] = newData
        }else{
            for(x in 9 downTo 1){
                res[x] = res[x-1]
            }
            res[0] = "0:${differInTime()}"
        }
        val fileOutputStream = openFileOutput(timeFileName, Context.MODE_PRIVATE)
        for(re in res){
            fileOutputStream.write(re.toByteArray())
        }
    }
   // day:Time;


}