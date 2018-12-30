package de.niedermeyer.nfc_trigger.actions.alarm

import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import de.niedermeyer.nfc_trigger.R
import de.niedermeyer.nfc_trigger.actions.Action
import de.niedermeyer.nfc_trigger.actions.ActionConstants
import kotlinx.serialization.json.JSON
import kotlinx.serialization.stringify
import org.json.JSONArray

class AlarmAction(val context: Context, var hours: Int, var minutes: Int) : Action() {
    override var TYPE = ActionConstants.ALARM

    override var VAL = arrayOf(hours, minutes)

    override fun doAction() {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_HOUR, hours)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes)
        context.startActivity(intent)
    }

    override fun toString(): String {
        return context.getString(R.string.action_alarm_tostring, hours, minutes)
    }
}