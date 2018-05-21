package de.niedermeyer.nfc_trigger.nfc.writing

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.IntentFilter
import android.nfc.*
import android.nfc.tech.Ndef
import de.niedermeyer.nfc_trigger.R
import de.niedermeyer.nfc_trigger.actions.Action
import java.io.IOException
import java.nio.charset.Charset

class NFCTagWriter(private val context: Context) {

    private val nfcAdapter: NfcAdapter = NfcAdapter.getDefaultAdapter(context)

    fun activateNfcIntents(pendingIntent: PendingIntent) {
        nfcAdapter.enableForegroundDispatch(context as Activity, pendingIntent, getIntentFilters(), null)
    }

    fun deactivateNfcIntents() {
        nfcAdapter.disableForegroundDispatch(context as Activity)
    }

    fun writeTag(tag: Tag, actions: List<Action>) {
        var message = "$"
        actions.forEach { message += it.NFC_MESSAGE + "$" }

        val ndef = Ndef.get(tag)
        try {
            ndef.connect()

            // uri
            val uriRecord = NdefRecord.createUri(context.getString(R.string.url_scheme) + "://" + context.getString(R.string.url_host))
            // message
            val mimeRecord = NdefRecord.createMime("text/plain", message.toByteArray(Charset.forName("US-ASCII")))
            // application record
            val appRecord = NdefRecord.createApplicationRecord(context.packageName)
            // write full message
            ndef.writeNdefMessage(NdefMessage(arrayOf(uriRecord, mimeRecord, appRecord)))

            ndef.close()
            // TODO: message

        } catch (e: IOException) {
            e.printStackTrace()
            // TODO

        } catch (e: FormatException) {
            e.printStackTrace()
            // TODO
        }

    }

    private fun getIntentFilters(): Array<IntentFilter> {
        val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        val ndefDetected = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        val techDetected = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        return arrayOf(techDetected, tagDetected, ndefDetected)
    }

}