package de.niedermeyer.nfc_trigger.nfc.reading

import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import de.niedermeyer.nfc_trigger.R
import de.niedermeyer.nfc_trigger.actions.Action
import de.niedermeyer.nfc_trigger.actions.ActionConstants
import de.niedermeyer.nfc_trigger.actions.alarm.AlarmAction
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.JSON
import kotlinx.serialization.parse
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset
import java.util.*

class NFCTagReader(val context: Context) {

    @UseExperimental(ImplicitReflectionSerializer::class)
    fun getActionsFromIntent(intent: Intent): List<Action?> {
        val jsonString = getMessageFromIntent(intent)
        val jsonArray = JSONArray(jsonString)

        val actions = LinkedList<Action?>()
        for (i in 0..(jsonArray.length() - 1)) {
            val item = jsonArray.getJSONObject(i)
            actions.add(convertJsonObjectToAction(item))
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

    private fun convertJsonObjectToAction(json: JSONObject): Action? {
        val actionType = json.getInt("TYPE")
        when (actionType) {
            ActionConstants.ALARM -> {
                val params = json.getJSONArray("VAL")
                return AlarmAction(context, params.getInt(0), params.getInt(1))
            }
        }

        return null
    }

}