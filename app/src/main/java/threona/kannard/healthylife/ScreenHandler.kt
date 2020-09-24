package threona.kannard.healthylife

import android.app.Service
import android.content.Intent
import android.os.IBinder
import threona.kannard.healthylife.ScreenListener.ScreenStateListener
import java.util.*

class ScreenHandler : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var counter = SleepCounter()
        val screenListener = ScreenListener(this)
        screenListener.begin(object : ScreenStateListener {
            override fun onUserPresent() {
                counter.beginCounter()
            }//unlock screen

            override fun onScreenOn() {

            }//screen on

            override fun onScreenOff() {
                counter.endCounter()
            }//screen off
        })
        return super.onStartCommand(intent, flags, startId)
    }
}
