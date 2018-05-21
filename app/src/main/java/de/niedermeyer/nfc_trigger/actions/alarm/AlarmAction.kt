package de.niedermeyer.nfc_trigger.actions.alarm

import android.content.Context
import de.niedermeyer.nfc_trigger.R
import de.niedermeyer.nfc_trigger.actions.Action

class AlarmAction(val context: Context, val hours: Int, val minutes: Int) : Action() {

    override var NFC_MESSAGE: String? = null

    init {
        NFC_MESSAGE = context.getString(R.string.alarm) + ":$hours, $minutes"
    }

}