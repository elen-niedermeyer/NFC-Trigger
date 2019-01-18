package de.niedermeyer.nfc_trigger.actions.wifi

import android.content.Context
import de.niedermeyer.nfc_trigger.R
import de.niedermeyer.nfc_trigger.actions.Action
import de.niedermeyer.nfc_trigger.actions.ActionConstants

class WifiAction(val context: Context, var toTurnOn: Int) : Action() {
    companion object {
        const val WIFI_ON = 1
        const val WIFI_OFF = 0
    }

    override var TYPE = ActionConstants.ALARM

    override var VAL = arrayOf(toTurnOn)

    override fun doAction() {

    }

    override fun toString(): String {
        if (toTurnOn == 0) {
            return context.getString(R.string.action_wifi_tostring, context.getString(R.string.off))
        } else {
            return context.getString(R.string.action_wifi_tostring, context.getString(R.string.on))
        }
    }
}