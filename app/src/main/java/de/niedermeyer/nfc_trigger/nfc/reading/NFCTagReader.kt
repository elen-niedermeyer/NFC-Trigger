package de.niedermeyer.nfc_trigger.nfc.reading

import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import de.niedermeyer.nfc_trigger.R
import de.niedermeyer.nfc_trigger.actions.Action
import de.niedermeyer.nfc_trigger.actions.alarm.AlarmAction
import java.nio.charset.Charset
import java.util.*

class NFCTagReader(val context: Context) {

    fun getActionsFromIntent(intent: Intent): List<Action> {
        val text = getMessageFromIntent(intent)
        val stringArray = parseActions(text)

        val actions = LinkedList<Action>()
        stringArray.forEach {
            val action = makeStringToAction(it)
            if (action != null) {
                actions.add(action)
            }
        }
        return actions
    }

    private fun getMessageFromIntent(intent: Intent): String {
        var text = ""
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            val rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            if (rawMessages != null) {
                val messages = Array<NdefMessage>(rawMessages.size, init = { rawMessages[it] as NdefMessage })
                val records = messages[0].records
                records.forEach {
                    if (it.toMimeType() != null) {
                        text = String(it.payload, Charset.forName("US-ASCII"))
                    }
                }
            }
        }

        return text
    }

    private fun parseActions(text: String): List<String> {
        val actions = text.split("$")
        return actions.subList(1, actions.size - 1)
    }

    private fun makeStringToAction(string: String): Action? {
        val parts = string.split(":")
        if (parts[0] == context.getString(R.string.action_alarm_name)) {
            val params = parts[1].split(",")
            return AlarmAction(context, params[0].toInt(), params[1].toInt())
        }

        return null
    }

}