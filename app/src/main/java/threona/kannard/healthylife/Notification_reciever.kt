package threona.kannard.healthylife

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class Notification_reciever: BroadcastReceiver() {
    override fun onReceive(p0: Context, p1: Intent) {
        Log.i("Notify"," on")

        val builder = NotificationCompat.Builder(p0,R.string.CHANNEL_ID.toString())

            .setSmallIcon(R.drawable.water_icon)
            .setContentTitle("Water")
            .setContentText("Drink Now !!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(p0)) {
            // notificationId is a unique int for each notification that you must define
            notify(100, builder.build())
        }
    }
}