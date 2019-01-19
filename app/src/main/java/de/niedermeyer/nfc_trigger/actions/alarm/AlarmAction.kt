package de.niedermeyer.nfc_trigger.actions.alarm

import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.provider.AlarmClock
import de.niedermeyer.nfc_trigger.R
import de.niedermeyer.nfc_trigger.actions.Action
import de.niedermeyer.nfc_trigger.actions.ActionConstants

class AlarmAction() : Action() {
    override var TYPE: Int = ActionConstants.ALARM
    override var VAL: Array<Int> = arrayOf()
    var context: Context? = null
    var hours: Int
        get() = VAL[0]
        set(value) {
            VAL[0] = value
        }
    var minutes: Int
        get() = VAL[1]
        set(value) {
            VAL[1] = value
        }

    constructor (context: Context, hours: Int, minutes: Int) : this() {
        this.context = context
        VAL = arrayOf(hours, minutes)
    }

    companion object CREATOR: Parcelable.Creator<AlarmAction>{
        override fun newArray(size: Int): Array<AlarmAction?> {
            return arrayOfNulls(size)
        }

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

    override fun doAction() {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_HOUR, hours)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes)
        context!!.startActivity(intent)
    }

    override fun toString(): String {
        return context!!.getString(R.string.action_alarm_tostring, hours, minutes)
    }
}