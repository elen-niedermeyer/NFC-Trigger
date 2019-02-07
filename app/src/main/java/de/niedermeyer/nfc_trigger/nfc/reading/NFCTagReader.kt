package de.niedermeyer.nfc_trigger.nfc.reading

import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import de.niedermeyer.nfc_trigger.actions.Action
import de.niedermeyer.nfc_trigger.actions.ActionTypes
import de.niedermeyer.nfc_trigger.actions.alarm.AlarmAction
import de.niedermeyer.nfc_trigger.actions.wifi.WifiAction
import kotlinx.serialization.ImplicitReflectionSerializer
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset
import java.util.*

/**
 * Handles nfc messages.
 *
 * @param context
 *
 * @author Elen Niedermeyer, last update 2019-01-30
 */
class NFCTagReader(val context: Context) {

    /**
     * Gets an intent and parses all actions given in the message.
     *
     * @param intent the intent with nfc message
     * @return a list of {@link Action}
     */
    @UseExperimental(ImplicitReflectionSerializer::class)
    fun getActionsFromIntent(intent: Intent): List<Action?> {
        // get the message from intent
        val jsonString = getMessageFromIntent(intent)

        // make intent message to json array
        val jsonArray = JSONArray(jsonString)
        // initialize list of actions and fill it with the elements of json array
        val actions = LinkedList<Action?>()
        for (i in 0..(jsonArray.length() - 1)) {
            val item = jsonArray.getJSONObject(i)
            actions.add(convertJsonObjectToAction(item))
        }

        return actions
    }

    /**
     * Extracts the intent message.
     *
     * @param intent the intent with nfc message
     * @return the intent message as string
     */
    private fun getMessageFromIntent(intent: Intent): String {
        var text = ""

        // check the intent action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            // get the raw message
            val rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)

            if (rawMessages != null) {
                // parse the raw message to an ndef message
                val messages = Array<NdefMessage>(rawMessages.size, init = { rawMessages[it] as NdefMessage })
                // get all records and add it to the return text
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

    /**
     * Parses a json object to an action.
     *
     * @param json the json object
     * @return an action or null if the action type is unknown
     */
    private fun convertJsonObjectToAction(json: JSONObject): Action? {
        // get the action type
        val actionType = json.getInt("TYPE")
        // get the values
        val params = json.getJSONArray("VAL")

        // create the correct action and return it
        when (actionType) {
            ActionTypes.ALARM -> {
                return AlarmAction(context, params.getInt(0), params.getInt(1))
            }
            ActionTypes.WIFI -> {
                return WifiAction(context, params.getInt(0))
            }
        }

        return null
    }

}