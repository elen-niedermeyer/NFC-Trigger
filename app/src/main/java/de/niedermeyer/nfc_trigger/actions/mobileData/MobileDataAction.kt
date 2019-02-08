package de.niedermeyer.nfc_trigger.actions.mobileData

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import de.niedermeyer.nfc_trigger.R
import de.niedermeyer.nfc_trigger.actions.Action
import de.niedermeyer.nfc_trigger.actions.ActionTypes

/**
 * Action that presents the action of turning mobile data on/off.
 * @author Elen Niedermeyer 2019-02-08
 */
class MobileDataAction() : Action() {

    /**
     * constants for the wifi action
     * @see Parcelable.Creator
     */
    companion object CREATOR : Parcelable.Creator<MobileDataAction> {

        /** Value for saying the mobile data should be turned on*/
        const val MOBILE_DATA_ON = 1

        /** Value for saying the mobile data should be turned off*/
        const val MOBILE_DATA_OFF = 0

        /** @see Parcelable.Creator.newArray */
        override fun newArray(size: Int): Array<MobileDataAction?> {
            return arrayOfNulls(size)
        }

        /** @see Parcelable.Creator.createFromParcel */
        override fun createFromParcel(source: Parcel?): MobileDataAction? {
            if (source is Parcel) {
                val action = MobileDataAction()
                action.TYPE = source.readInt()
                action.VAL = source.createIntArray().toTypedArray()
                return action
            }
            return null
        }
    }

    /** @see de.niedermeyer.nfc_trigger.actions.Action#TYPE */
    override var TYPE = ActionTypes.MOBILE_DATA

    /** @see de.niedermeyer.nfc_trigger.actions.Action#VAL */
    override var VAL: Array<Int> = arrayOf()

    /** the activity context */
    var context: Context? = null

    /** getter and setter for the turnOn setting */
    var toTurnOn: Int
        get() = VAL[0]
        set(value) {
            VAL[0] = value
        }

    /**
     * Constructor with parameter
     *
     * @param context
     * @param toTurnOn here are the values {@link #MOBILE_DATA_ON} and {@link #MOBILE_DATA_OFF} allowed
     */
    constructor(context: Context, toTurnOn: Int) : this() {
        this.context = context
        VAL = arrayOf(toTurnOn)
    }

    /**
     * @see de.niedermeyer.nfc_trigger.actions.Action#doAction()
     * Turns the mobile data on or off.
     */
    override fun doAction() {

        }

    /** @see Object#toString() */
    override fun toString(): String {
        if (toTurnOn == MobileDataAction.MOBILE_DATA_OFF) {
            return context!!.getString(R.string.action_mobile_data_tostring, context!!.getString(R.string.off))
        } else {
            return context!!.getString(R.string.action_mobile_data_tostring, context!!.getString(R.string.on))
        }
    }

}