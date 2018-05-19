package de.niedermeyer.nfc_trigger.actions.alarm

import de.niedermeyer.nfc_trigger.actions.Action

class AlarmAction(val hours: Int, val minutes: Int) : Action() {

    override var NFC_MESSAGE: String? = null

    init {
        NFC_MESSAGE = "ALARM:$hours,$minutes"
    }

}