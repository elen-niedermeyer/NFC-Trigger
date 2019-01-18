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

    override fun doAction() {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_HOUR, VAL[0])
                .putExtra(AlarmClock.EXTRA_MINUTES, VAL[1])
        context!!.startActivity(intent)
    }

    override fun toString(): String {
        return context!!.getString(R.string.action_alarm_tostring, VAL[0], VAL[1])
    }

    constructor(context: Context, hours: Int, minutes: Int) : this() {
        this.context = context
        VAL = arrayOf(hours, minutes)
    }

    private constructor (src: Parcel?) : this() {
        if (src is Parcel){
            TYPE = src.readInt()
            VAL = src.createIntArray().toTypedArray()
        }
    }

    companion object CREATOR: Parcelable.Creator<AlarmAction>{
        override fun newArray(size: Int): Array<AlarmAction?> {
            return arrayOfNulls(size)
        }

        override fun createFromParcel(source: Parcel?): AlarmAction {
            return AlarmAction(source)
        }

    }
}