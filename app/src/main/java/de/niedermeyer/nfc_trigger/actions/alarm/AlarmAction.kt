package de.niedermeyer.nfc_trigger.actions.alarm

import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import de.niedermeyer.nfc_trigger.R
import de.niedermeyer.nfc_trigger.actions.Action
import de.niedermeyer.nfc_trigger.actions.ActionTypes

/**
 * Action that presents the action setting an alarm.
 *
 * @param context
 * @param hours the number of hours, allowed are values between 0 and 23 (including both)
 * @param minutes the number of minutes, allowed are values between 0 and 59 (including both)
 *
 * @author Elen Niedermeyer, last update 2019-01-30
 */
class AlarmAction(val context: Context, var hours: Int, var minutes: Int) : Action() {

    /** @see de.niedermeyer.nfc_trigger.actions.Action#TYPE */
    override var TYPE = ActionTypes.ALARM

    /** @see de.niedermeyer.nfc_trigger.actions.Action#VAL */
    override var VAL = arrayOf(hours, minutes)

    /**
     * @see de.niedermeyer.nfc_trigger.actions.Action#doAction()
     * Starts a new clock activity to set the alarm.
     */
    override fun doAction() {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_HOUR, hours)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes)
        context.startActivity(intent)
    }

    /** @see Object#toString() */
    override fun toString(): String {
        return context.getString(R.string.action_alarm_tostring, hours, minutes)
    }
}