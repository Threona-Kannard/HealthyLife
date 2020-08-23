package threona.kannard.healthylife

import java.util.*

class SleepCounter {
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
        }
    }

    fun isSleep(): Boolean {
        var differHour = endTime!!.get(Calendar.HOUR_OF_DAY) - beginTime!!.get(Calendar.HOUR_OF_DAY)
        var differMin : Int
        differMin = if(differHour == 0) {
            endTime!!.get(Calendar.MINUTE) - beginTime!!.get(Calendar.MINUTE)
        } else if(differHour == -23 || differHour == 1){
            60 - (endTime!!.get(Calendar.MINUTE) - beginTime!!.get(Calendar.MINUTE))
        } else 15
        return (differMin < 15)
    }


    companion object{
        var beginTime:Calendar? = null
        var endTime:Calendar? = null
    }
}