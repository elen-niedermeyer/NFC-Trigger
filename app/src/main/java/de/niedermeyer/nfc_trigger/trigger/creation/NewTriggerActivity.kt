package de.niedermeyer.nfc_trigger.trigger.creation

import android.app.AlertDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.niedermeyer.nfc_trigger.CreateTrigger.ActionListAdapter
import de.niedermeyer.nfc_trigger.R
import de.niedermeyer.nfc_trigger.actions.Action
import de.niedermeyer.nfc_trigger.actions.alarm.AlarmAction
import de.niedermeyer.nfc_trigger.nfc.writing.NFCTagWriter
import kotlinx.android.synthetic.main.activity_new_trigger.*
import kotlinx.android.synthetic.main.dialog_spinner.*
import org.jetbrains.anko.toast
import java.util.*

class NewTriggerActivity : AppCompatActivity() {

    val triggerActions = LinkedList<Action>()

    lateinit var nfcWriter: NFCTagWriter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_trigger)

        activity_new_trigger_btn_add.setOnClickListener {
            val dialog = ChooseActionDialog(this@NewTriggerActivity)
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.next)) { dialog, _ ->
                dialog.dismiss()

                if (dialog is ChooseActionDialog) {
                    val chosenAction = dialog.spinner.selectedItem.toString()

                    addChosenAction(chosenAction)
                }
            }

            dialog.show()
        }

        nfcWriter = NFCTagWriter(this)
        val pendingIntent = PendingIntent.getActivity(
                this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        activity_new_trigger_btn_write.setOnClickListener {
            nfcWriter.activateNfcIntents(pendingIntent)
            // TODO: visual feedback
        }

        updateActionList()
    }

    private fun updateActionList(){
        activity_new_trigger_actions.adapter = ActionListAdapter(this@NewTriggerActivity, triggerActions)
    }

    override fun onPause() {
        super.onPause()

        nfcWriter.deactivateNfcIntents()
    }

    private fun addChosenAction(actionName: String) {

        if (actionName == getString(R.string.action_alarm_name)) {
            val timePicker = TimePickerDialog(this@NewTriggerActivity,
                    TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                        val action = AlarmAction(this@NewTriggerActivity, hourOfDay, minute)
                        triggerActions.add(action)
                        toast(getString(R.string.action_added, action.toString()))
                        updateActionList()
                    },
                    0, 0, true)
            timePicker.show()
        }
    }

    override fun onNewIntent(intent: Intent) {
        val tagFromIntent: Tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)

        nfcWriter.writeTag(tagFromIntent, triggerActions)
    }

}
