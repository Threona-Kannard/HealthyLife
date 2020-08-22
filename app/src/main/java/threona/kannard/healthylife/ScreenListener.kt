package threona.kannard.healthylife

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.PowerManager


class ScreenListener(context: Context) {
            private val mContext : Context = context
            private val mScreenReceiver: ScreenBroadcastReceiver
            private var mScreenStateListener: ScreenStateListener? = null

            private inner class ScreenBroadcastReceiver : BroadcastReceiver() {
                private var action: String? = null
        override fun onReceive(context: Context?, intent: Intent) {
            action = intent.action
            when {
                Intent.ACTION_SCREEN_ON == action -> {
                    mScreenStateListener!!.onScreenOn()
                }
                Intent.ACTION_SCREEN_OFF == action -> {
                    mScreenStateListener!!.onScreenOff()
                }
                Intent.ACTION_USER_PRESENT == action -> {
                    mScreenStateListener!!.onUserPresent()
                }
            }
        }
    }


    fun begin(listener: ScreenStateListener?) {
        mScreenStateListener = listener
        registerListener()
        screenState
    }

    private val screenState: Unit
        get() {
            val manager = mContext
                .getSystemService(Context.POWER_SERVICE) as PowerManager
            if (manager.isInteractive) {
                if (mScreenStateListener != null) {
                    mScreenStateListener!!.onScreenOn()
                }
            } else {
                if (mScreenStateListener != null) {
                    mScreenStateListener!!.onScreenOff()
                }
            }
        }

    fun unregisterListener() {
        mContext.unregisterReceiver(mScreenReceiver)
    }

    private fun registerListener() {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_USER_PRESENT)
        mContext.registerReceiver(mScreenReceiver, filter)
    }

    interface ScreenStateListener {
        fun onScreenOn()
        fun onScreenOff()
        fun onUserPresent()
    }

    init {
        mScreenReceiver = ScreenBroadcastReceiver()
    }
}