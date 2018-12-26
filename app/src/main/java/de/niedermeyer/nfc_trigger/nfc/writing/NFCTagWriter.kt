package de.niedermeyer.nfc_trigger.nfc.writing

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.IntentFilter
import android.nfc.*
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import de.niedermeyer.nfc_trigger.R
import de.niedermeyer.nfc_trigger.actions.Action
import kotlinx.serialization.ImplicitReflectionSerializer
import java.io.IOException
import java.nio.charset.Charset
import kotlinx.serialization.json.JSON
import kotlinx.serialization.list
import kotlinx.serialization.stringify
import org.json.JSONArray

class NFCTagWriter(private val context: Context) {

    private val nfcAdapter: NfcAdapter = NfcAdapter.getDefaultAdapter(context)

    fun activateNfcIntents(pendingIntent: PendingIntent) {
        nfcAdapter.enableForegroundDispatch(context as Activity, pendingIntent, getIntentFilters(), null)
    }

    fun deactivateNfcIntents() {
        nfcAdapter.disableForegroundDispatch(context as Activity)
    }

    fun writeTag(tag: Tag, actions: List<Action>) {
        val message: String = createMessage(actions)

        // uri
        val uriRecord = NdefRecord.createUri(context.getString(R.string.uri_scheme) + "://" + context.getString(R.string.uri_host))
        // message
        val mimeRecord = NdefRecord.createMime("text/plain", message.toByteArray(Charset.forName("US-ASCII")))

        // write full message
        writeMessageToTag(NdefMessage(arrayOf(uriRecord, mimeRecord)), tag)
        // TODO: reaction for success or not
    }


    @UseExperimental(ImplicitReflectionSerializer::class)
    private fun createMessage(actions: List<Action>): String {
        return JSON.stringify(Action.serializer().list, actions)
    }

    private fun writeMessageToTag(nfcMessage: NdefMessage, tag: Tag?): Boolean {
        try {
            val nDefTag = Ndef.get(tag)

            nDefTag?.let {
                it.connect()

                var maxsize = it.maxSize
                var size = nfcMessage.toByteArray().size

                if (it.maxSize < nfcMessage.toByteArray().size) {
                    //Message to large to write to NFC tag
                    return false
                }

                if (it.isWritable) {
                    it.writeNdefMessage(nfcMessage)
                    it.close()
                    //Message is written to tag
                    return true

                } else {
                    //NFC tag is read-only
                    return false
                }
            }

            val nDefFormatableTag = NdefFormatable.get(tag)

            nDefFormatableTag?.let {
                try {
                    it.connect()
                    it.format(nfcMessage)
                    it.close()
                    //The data is written to the tag
                    return true

                } catch (e: IOException) {
                    //Failed to format tag
                    return false
                }
            }

            //NDEF is not supported
            return false

        } catch (e: Exception) {
            e.printStackTrace()
            //Write operation has failed
        }
        return false
    }


    private fun getIntentFilters(): Array<IntentFilter> {
        val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        val ndefDetected = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        val techDetected = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        return arrayOf(techDetected, tagDetected, ndefDetected)
    }

}