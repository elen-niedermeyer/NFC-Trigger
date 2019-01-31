package de.niedermeyer.nfc_trigger.nfc.reading

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.niedermeyer.nfc_trigger.R

/**
 * This activity is called the phone reads a matching NFC tag.
 *
 * @author Elen Niedermeyer, last update 2019-01-30
 */
class NFCReaderActivity : AppCompatActivity() {

    /** the nfc tag reader instance */
    private lateinit var nfcReader: NFCTagReader

    /**
     * @see android.support.v7.app.AppCompatActivity#onCreate(savedInstanceState: Bundle?)
     * Initializes the {@link #nfcReader} and let it parse the nfc tag info and execute the actions.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_reader)

        nfcReader = NFCTagReader(this)
        nfcReader.getActionsFromIntent(intent).forEach { if (it != null) it.doAction() }
    }

    /**
     * @see android.support.v7.app.AppCompatActivity#onNewIntent(intent: Intent)
     * Gives the intent to {@link nfcReader}.
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        nfcReader.getActionsFromIntent(intent)
    }

}
