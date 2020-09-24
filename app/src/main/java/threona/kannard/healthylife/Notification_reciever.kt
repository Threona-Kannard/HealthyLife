package threona.kannard.healthylife

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class Notification_reciever: BroadcastReceiver() {
    override fun onReceive(p0: Context, p1: Intent) {
        Log.i("Notify"," on")

        val intent = Intent(p0, WaterActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(p0, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(p0,R.string.CHANNEL_ID.toString())
            .setSmallIcon(R.drawable.water_icon)
            .setContentTitle("Water")
            .setContentText("Drink Now !!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(p0)) {
            // notificationId is a unique int for each notification that you must define
            notify(0, builder.build())
        }
    }
}