package de.niedermeyer.nfc_trigger.actions.alarm

import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import de.niedermeyer.nfc_trigger.R
import de.niedermeyer.nfc_trigger.actions.Action

class AlarmAction(val context: Context, val hours: Int, val minutes: Int) : Action() {
    override var NFC_MESSAGE: String? = null

    init {
        NFC_MESSAGE = context.getString(R.string.action_alarm_nfc, context.getString(R.string.action_alarm_name), hours, minutes)
    }

    override fun doAction() {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_HOUR, hours)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes)
        context.startActivity(intent)
    }
}