package de.niedermeyer.nfc_trigger.nfc.writing

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import de.niedermeyer.nfc_trigger.R
import de.niedermeyer.nfc_trigger.actions.Action
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.JSON
import kotlinx.serialization.list
import java.nio.charset.Charset

class NFCTagWriter(private val context: Context) {

    /**
     * Create a NFC adapter from context
     */
    private val nfcAdapter: NfcAdapter = NfcAdapter.getDefaultAdapter(context)

    /**
     * Enables the possiblity to detect an NFC tag in writing mode.
     *
     * @param pendingIntent
     */
    fun activateNfcIntents(pendingIntent: PendingIntent) {
        nfcAdapter.enableForegroundDispatch(context as Activity, pendingIntent, getIntentFilters(), null)
    }

    /**
     * Disables the possiblity to detect an NFC tag in writing mode.
     */
    fun deactivateNfcIntents() {
        nfcAdapter.disableForegroundDispatch(context as Activity)
    }

    /**
     * Creates a message from the given list of actions.
     * Creates an NDEF with the message and an URI for writing it on the NFC tag.
     *
     * @param
     */
    fun writeTag(tag: Tag, actions: List<Action>): Boolean {
        val message: String = createMessage(actions)

        // uri
        val uriRecord = NdefRecord.createUri(context.getString(R.string.uri_scheme) + "://" + context.getString(R.string.uri_host))
        // message as NDEF record
        val mimeRecord = NdefRecord.createMime("text/plain", message.toByteArray(Charset.forName("US-ASCII")))

        // write full message
        return writeMessageToTag(NdefMessage(arrayOf(uriRecord, mimeRecord)), tag)
    }


    /**
     * Creates a message that contains the given actions.
     *
     */
    @UseExperimental(ImplicitReflectionSerializer::class)
    private fun createMessage(actions: List<Action>): String {
        return JSON.stringify(Action.serializer().list, actions)
    }

    private fun writeMessageToTag(nfcMessage: NdefMessage, tag: Tag?): Boolean {
        val nDefTag = Ndef.get(tag)
        nDefTag?.let {
            it.connect()

            var maxsize = it.maxSize
            var size = nfcMessage.toByteArray().size

            if (it.maxSize < nfcMessage.toByteArray().size) {
                // Message to large to write to NFC tag
                throw Exception(context.getString(R.string.writing_error_size))
            }

            if (it.isWritable) {
                it.writeNdefMessage(nfcMessage)
                it.close()
                // Message is written to tag
                return true

            } else {
                // NFC tag is read-only
                throw Exception(context.getString(R.string.writing_error_restricted))
            }
        }

        val nDefFormatableTag = NdefFormatable.get(tag)
        nDefFormatableTag?.let {
            it.connect()
            it.format(nfcMessage)
            it.close()
            // The data is written to the tag
            return true
        }

        // NDEF is not supported
        throw Exception(context.getString(R.string.writing_error_support))
    }


    private fun getIntentFilters(): Array<IntentFilter> {
        val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        val ndefDetected = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        val techDetected = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        return arrayOf(techDetected, tagDetected, ndefDetected)
    }

}