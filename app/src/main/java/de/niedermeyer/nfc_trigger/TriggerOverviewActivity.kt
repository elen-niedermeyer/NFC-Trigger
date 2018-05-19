package de.niedermeyer.nfc_trigger

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.niedermeyer.nfc_trigger.CreateTrigger.NewTriggerActivity
import de.niedermeyer.nfc_trigger.actions.alarm.AlarmAction
import de.niedermeyer.nfc_trigger.nfc.NFCTagWriter
import kotlinx.android.synthetic.main.activity_trigger_overview.*


class TriggerOverviewActivity : AppCompatActivity() {

    lateinit var nfcWriter: NFCTagWriter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trigger_overview)

        activity_trigger_overview_btn_add.setOnClickListener { startActivity(Intent(this@TriggerOverviewActivity, NewTriggerActivity::class.java)) }

        nfcWriter = NFCTagWriter(this)
    }

    override fun onResume() {
        super.onResume()

        val pendingIntent = PendingIntent.getActivity(
                this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        nfcWriter.activateNfcIntents(pendingIntent)
    }

    override fun onPause() {
        super.onPause()

        nfcWriter.deactivateNfcIntents()
    }

    override fun onNewIntent(intent: Intent) {
        val tagFromIntent: Tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)

        nfcWriter.writeTag(tagFromIntent, listOf(AlarmAction(8, 45)))
    }

}
