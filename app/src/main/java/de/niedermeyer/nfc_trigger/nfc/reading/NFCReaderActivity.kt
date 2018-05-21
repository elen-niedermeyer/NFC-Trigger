package de.niedermeyer.nfc_trigger.nfc.reading

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.niedermeyer.nfc_trigger.R

class NFCReaderActivity : AppCompatActivity() {

    private lateinit var nfcReader: NFCTagReader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_reader)

        nfcReader = NFCTagReader(this)
        nfcReader.getActionsFromIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        nfcReader.getActionsFromIntent(intent)
    }

}
