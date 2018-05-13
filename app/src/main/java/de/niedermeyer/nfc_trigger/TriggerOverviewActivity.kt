package de.niedermeyer.nfc_trigger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import de.niedermeyer.nfc_trigger.CreateTrigger.NewTriggerActivity
import kotlinx.android.synthetic.main.activity_trigger_overview.*

class TriggerOverviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trigger_overview)

        activity_trigger_overview_btn_add.setOnClickListener { startActivity(Intent(this@TriggerOverviewActivity, NewTriggerActivity::class.java)) }
    }
}
