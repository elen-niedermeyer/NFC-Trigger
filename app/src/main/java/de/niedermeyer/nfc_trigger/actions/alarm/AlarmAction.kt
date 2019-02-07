package de.niedermeyer.nfc_trigger.actions.alarm

import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.provider.AlarmClock
import de.niedermeyer.nfc_trigger.R
import de.niedermeyer.nfc_trigger.actions.Action
import de.niedermeyer.nfc_trigger.actions.ActionTypes

/**
 * Action that presents the action setting an alarm.
 *
 * @author Elen Niedermeyer, Milan Höllner,  last update 2019-02-07
 */

class AlarmAction() : Action() {

    /** @see de.niedermeyer.nfc_trigger.actions.Action#TYPE */
    override var TYPE: Int = ActionTypes.ALARM

    /** @see de.niedermeyer.nfc_trigger.actions.Action#VAL */
    override var VAL: Array<Int> = arrayOf()

    /** the activity context to use */
    var context: Context? = null

    /** getter and setter for the hours */
    var hours: Int
        get() = VAL[0]
        set(value) {
            VAL[0] = value
        }

    /** getter and setter for the minutes */
    var minutes: Int
        get() = VAL[1]
        set(value) {
            VAL[1] = value
        }

    /**
     * @param context
     * @param hours the number of hours, allowed are values between 0 and 23 (including both)
     * @param minutes the number of minutes, allowed are values between 0 and 59 (including both)
     */
    constructor (context: Context, hours: Int, minutes: Int) : this() {
        this.context = context
        VAL = arrayOf(hours, minutes)
    }

    /**
     * Needed to be able to create an action from a parcel
     * @see Parcelable.Creator
     */
    companion object CREATOR: Parcelable.Creator<AlarmAction> {

        /** @see Parcelable.Creator.newArray */
        override fun newArray(size: Int): Array<AlarmAction?> {
            return arrayOfNulls(size)
        }

        /** @see Parcelable.Creator.createFromParcel */
        override fun createFromParcel(source: Parcel?): AlarmAction? {
            if (source is Parcel){
                val action = AlarmAction()
                action.TYPE = source.readInt()
                action.VAL = source.createIntArray().toTypedArray()
                return action
            }
            return null
        }
    }

    /**
     * @see de.niedermeyer.nfc_trigger.actions.Action#doAction()
     * Starts a new clock activity to set the alarm.
     */
    override fun doAction() {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_HOUR, hours)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes)
        context!!.startActivity(intent)
    }

    /** @see Object#toString() */
    override fun toString(): String {
        return context!!.getString(R.string.action_alarm_tostring, hours, minutes)
    }
}