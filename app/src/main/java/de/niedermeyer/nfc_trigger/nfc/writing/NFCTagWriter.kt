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

/**
 * Handles all activities that are needed to write to a nfc tag.
 *
 * @param context
 *
 * @author Elen Niedermeyer, last update 2019-01-30
 */
class NFCTagWriter(private val context: Context) {

    /**
     * Create a NFC adapter from context
     */
    private val nfcAdapter: NfcAdapter = NfcAdapter.getDefaultAdapter(context)

    /**
     * Enables the possibility to detect an NFC tag in writing mode.
     *
     * @param pendingIntent
     */
    fun activateNfcIntents(pendingIntent: PendingIntent) {
        nfcAdapter.enableForegroundDispatch(context as Activity, pendingIntent, getIntentFilters(), null)
    }

    /**
     * Returns all the intent filters that are needed to detect an NFC tag
     *
     * @return an array of intent filters
     */
    private fun getIntentFilters(): Array<IntentFilter> {
        val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        val ndefDetected = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        val techDetected = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        return arrayOf(techDetected, tagDetected, ndefDetected)
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
     * @param tag NFC tag detected by the activity, needed by {@link #writeMessageToTag}
     * @param actions list of actions to write to NFC tag, processed by {@link #createMessage}
     *
     * @return the result of {@link #writeMessageToTag}; true if writing to NFC tag was successful, false otherwise
     *
     * @throws Exception when thrown in {@link #writeMessageToTag}
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
     * Creates a message that contains the given actions. Returns them as JSON string.
     *
     * @param actions list of actions to write to NFC tag
     *
     * @return a JSON string for writing to NFC tag
     *
     */
    @UseExperimental(ImplicitReflectionSerializer::class)
    private fun createMessage(actions: List<Action>): String {
        return JSON.stringify(Action.serializer().list, actions)
    }

    /**
     * Writes the given message to the given tag.
     *
     * @param nfcMessage
     * @param tag NFC tag detected by the activity
     *
     * @return true if writing to NFC tag was successful, false otherwise
     *
     * @throws Exception when there was a failure with writing the NFC tag
     */
    private fun writeMessageToTag(nfcMessage: NdefMessage, tag: Tag?): Boolean {
        val nDefTag = Ndef.get(tag)
        nDefTag?.let {
            it.connect()

            // proof tag's size
            var maxsize = it.maxSize
            var size = nfcMessage.toByteArray().size
            if (it.maxSize < nfcMessage.toByteArray().size) {
                // Message to large to write to NFC tag
                throw Exception(context.getString(R.string.writing_error_size))
            }

            if (it.isWritable) {
                // write to tag
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
            // write to tag
            it.connect()
            it.format(nfcMessage)
            it.close()
            // The data is written to the tag
            return true
        }

        // NDEF is not supported
        throw Exception(context.getString(R.string.writing_error_support))
    }

}