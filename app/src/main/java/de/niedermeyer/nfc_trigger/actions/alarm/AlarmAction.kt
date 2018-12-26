package de.niedermeyer.nfc_trigger.actions.alarm

import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import de.niedermeyer.nfc_trigger.R
import de.niedermeyer.nfc_trigger.actions.Action
import kotlinx.serialization.json.JSON
import kotlinx.serialization.stringify
import org.json.JSONArray

class AlarmAction(val context: Context, val hours: Int, val minutes: Int) : Action() {
    override var TYPE: String = context.getString(R.string.action_alarm_type)

    override var VALUES: Array<String> = arrayOf(hours.toString(), minutes.toString())

    override fun doAction() {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_HOUR, hours)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes)
        context.startActivity(intent)
    }
}