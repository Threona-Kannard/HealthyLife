package threona.kannard.healthylife

import android.content.Context
import java.io.*
import java.util.*
import androidx.appcompat.app.AppCompatActivity


class SleepCounter : AppCompatActivity() {
    private val sleepFileName = "sleep_history.txt"
    private var beginTime:Calendar? = null
    private var endTime:Calendar? = null
    fun onChangeState(){

    }
    fun beginCounter(){
        beginTime = Calendar.getInstance()
    }
    fun endCounter(){
        endTime = Calendar.getInstance()
        if (!isSleep()){
            endTime = null
            beginTime = null
            //if Sleep then Save Sleep on to fill
            //something like SaveSleep(beginTime, endTime, Date)
        }else{
            saveSleepTime(beginTime!!, endTime!!)
        }
    }

    private fun isSleep(): Boolean {
        val differHour
                = endTime!!.get(Calendar.HOUR_OF_DAY) - beginTime!!.get(Calendar.HOUR_OF_DAY)
        val differMin : Int
        differMin = if(differHour == 0) {
            endTime!!.get(Calendar.MINUTE) - beginTime!!.get(Calendar.MINUTE)
        } else if(differHour == -23 || differHour == 1){
            60 - (endTime!!.get(Calendar.MINUTE) - beginTime!!.get(Calendar.MINUTE))
        } else 15
        return (differMin < 15)
    }


    private fun saveSleepTime(beginTime : Calendar, endTime : Calendar){
        var file = File(filesDir.absolutePath, sleepFileName)
        val endHour = endTime.get(Calendar.HOUR_OF_DAY)
        val beginHour = beginTime.get(Calendar.HOUR_OF_DAY)
        val endMin = endTime.get(Calendar.MINUTE)
        val beginMin = beginTime.get(Calendar.MINUTE)
        val day = endTime.get(Calendar.DAY_OF_MONTH)
        val month = endTime.get(Calendar.MONTH)
        val differInDay = if (endTime.get(Calendar.MONTH) != beginTime.get(Calendar.MONTH)) 1
        else{
            day - beginTime.get(Calendar.DAY_OF_MONTH) // = 1 or 0 depend on next day or same day
        }
        var differHour = if (differInDay == 0) endHour - beginHour else 24 + endHour - beginHour
        var differMin = if(differHour == 0 || (differHour != 0 && endMin > beginMin)){
            endMin - beginMin
        } else {
            60 - (endMin - beginMin)
        }

        val fileInputStream: FileInputStream = openFileInput(sleepFileName)
        val inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
        val stringBuilder: StringBuilder = StringBuilder()
        var text: String? = null
        while ({ text = bufferedReader.readLine(); text }() != null) {
            stringBuilder.append(text)
        }
        val dataLoad = (stringBuilder.toString())
        val res : MutableList<String> = dataLoad.split(";").toMutableList()
        var check = false
        val iterator = res.listIterator()
        while(iterator.hasNext()){
            val str = iterator.next()
            if(str.contains("$day/$month")){
                check = true
                val existedData = str.split(",")
                val existedTime = existedData[1].split(":")
                differMin += existedTime[1].toInt()
                differHour += existedTime[0].toInt()
                if(differMin >= 60){
                    differMin -= 60
                    differHour++
                }
                val newData = "$existedData[0],$differHour:$differMin"
                iterator.set(newData)
            }
        }
        inputStreamReader.close()
        try {
            val fileOutputStream: FileOutputStream = openFileOutput(sleepFileName, Context.MODE_PRIVATE)
            if(!check){
                fileOutputStream.write("$day/$month,$differHour:$differMin;".toByteArray())
            } else {
                fileOutputStream.flush()
                while(iterator.hasNext()){
                    fileOutputStream.write("$iterator.next();".toByteArray())
                }
            }
            fileOutputStream.close()
        } catch (e: FileNotFoundException){
            e.printStackTrace()
        }catch (e: NumberFormatException){
            e.printStackTrace()
        }catch (e: IOException){
            e.printStackTrace()
        }catch (e: Exception){
            e.printStackTrace()
        }

    }// DD/MM, difH:difM

}