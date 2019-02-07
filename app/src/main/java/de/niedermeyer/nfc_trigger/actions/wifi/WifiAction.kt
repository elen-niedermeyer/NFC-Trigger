package de.niedermeyer.nfc_trigger.actions.wifi

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Parcel
import android.os.Parcelable
import de.niedermeyer.nfc_trigger.R
import de.niedermeyer.nfc_trigger.actions.Action
import de.niedermeyer.nfc_trigger.actions.ActionTypes

/**
 * Action that presents the action of turning wifi on/off.
 * @author Elen Niedermeyer, Milan Höllner, 2019-02-07
 */
class WifiAction() : Action() {

    /**
     * constants for the wifi action
     * @see Parcelable.Creator
     */
    companion object CREATOR : Parcelable.Creator<WifiAction> {

        /** Value for saying the wifi should be turned on*/
        const val WIFI_ON = 1

        /** Value for saying the wifi should be turned off*/
        const val WIFI_OFF = 0

        /** @see Parcelable.Creator.newArray */
        override fun newArray(size: Int): Array<WifiAction?> {
            return arrayOfNulls(size)
        }

        /** @see Parcelable.Creator.createFromParcel */
        override fun createFromParcel(source: Parcel?): WifiAction? {
            if (source is Parcel) {
                val action = WifiAction()
                action.TYPE = source.readInt()
                action.VAL = source.createIntArray().toTypedArray()
                return action
            }
            return null
        }
    }

    /** @see de.niedermeyer.nfc_trigger.actions.Action#TYPE */
    override var TYPE = ActionTypes.WIFI

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
     * @param toTurnOn here are the values {@link #WIFI_ON} and {@link #WIFI_OFF} allowed
     */
    constructor(context: Context, toTurnOn: Int) : this() {
        this.context = context
        VAL = arrayOf(toTurnOn)
    }

    /**
     * @see de.niedermeyer.nfc_trigger.actions.Action#doAction()
     * Turns the wifi on or off.
     */
    override fun doAction() {
        val wifiManager = context!!.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (toTurnOn == WifiAction.WIFI_ON) {
            wifiManager.isWifiEnabled = true
        } else {
            wifiManager.isWifiEnabled = false
        }
    }

    /** @see Object#toString() */
    override fun toString(): String {
        if (toTurnOn == WifiAction.WIFI_OFF) {
            return context!!.getString(R.string.action_wifi_tostring, context!!.getString(R.string.off))
        } else {
            return context!!.getString(R.string.action_wifi_tostring, context!!.getString(R.string.on))
        }
    }

}