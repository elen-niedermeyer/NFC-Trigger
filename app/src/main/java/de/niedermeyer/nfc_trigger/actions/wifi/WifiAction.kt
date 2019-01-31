package de.niedermeyer.nfc_trigger.actions.wifi

import android.content.Context
import android.net.wifi.WifiManager
import de.niedermeyer.nfc_trigger.R
import de.niedermeyer.nfc_trigger.actions.Action
import de.niedermeyer.nfc_trigger.actions.ActionTypes


/**
 * Action that presents the action of turning wifi on/off.
 *
 * @param context
 * @param toTurnOn here are the values {@link #WIFI_ON} and {@link #WIFI_OFF} allowed
 *
 * @author Elen Niedermeyer, 2019-01-18
 */
class WifiAction(val context: Context, var toTurnOn: Int) : Action() {

    /** constants for the wifi action */
    companion object {
        /** Value for saying the wifi should be turned on*/
        const val WIFI_ON = 1

        /** Value for saying the wifi should be turned off*/
        const val WIFI_OFF = 0
    }

    /** @see de.niedermeyer.nfc_trigger.actions.Action#TYPE */
    override var TYPE = ActionTypes.WIFI

    /** @see de.niedermeyer.nfc_trigger.actions.Action#VAL */
    override var VAL = arrayOf(toTurnOn)

    /**
     * @see de.niedermeyer.nfc_trigger.actions.Action#doAction()
     * Turns the wifi on or off.
     */
    override fun doAction() {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (toTurnOn == WIFI_ON) {
            wifiManager.isWifiEnabled = true
        } else {
            wifiManager.isWifiEnabled = false
        }
    }

    /** @see Object#toString() */
    override fun toString(): String {
        if (toTurnOn == WIFI_OFF) {
            return context.getString(R.string.action_wifi_tostring, context.getString(R.string.off))
        } else {
            return context.getString(R.string.action_wifi_tostring, context.getString(R.string.on))
        }
    }
}