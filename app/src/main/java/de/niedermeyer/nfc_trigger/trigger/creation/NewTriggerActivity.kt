package de.niedermeyer.nfc_trigger.trigger.creation

import android.app.AlertDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v7.app.AppCompatActivity
import de.niedermeyer.nfc_trigger.R
import de.niedermeyer.nfc_trigger.actions.Action
import de.niedermeyer.nfc_trigger.actions.alarm.AlarmAction
import de.niedermeyer.nfc_trigger.nfc.setting.NFCSettingChecker
import de.niedermeyer.nfc_trigger.nfc.writing.NFCTagWriter
import kotlinx.android.synthetic.main.activity_new_trigger.*
import kotlinx.android.synthetic.main.dialog_spinner.*
import org.jetbrains.anko.toast
import java.util.*

class NewTriggerActivity : AppCompatActivity() {

    private lateinit var adapter: ActionListAdapter
    private lateinit var nfcWriter: NFCTagWriter

    private val actionsKey: String = "savedActions"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_trigger)

        nfcWriter = NFCTagWriter(this)
        val pendingIntent = PendingIntent.getActivity(
                this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        activity_new_trigger_btn_write.setOnClickListener {
            nfcWriter.activateNfcIntents(pendingIntent)
        }

        activity_new_trigger_btn_add.setOnClickListener {
            val chooseActionDialog = ChooseActionDialog(this@NewTriggerActivity)
            chooseActionDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.next)) { dialog, _ ->
                dialog.dismiss()

                if (dialog is ChooseActionDialog) {
                    val chosenAction = dialog.spinner.selectedItem.toString()

                    addChosenAction(chosenAction)
                }
            }

            chooseActionDialog.show()
        }

        // restore saved actions from instance state
        val savedActions = savedInstanceState?.getSerializable(actionsKey)
        if (savedActions is LinkedList<*> && savedActions.count() > 0) {
            adapter = ActionListAdapter(this@NewTriggerActivity, savedActions as LinkedList<Action>)
        } else {
            adapter = ActionListAdapter(this@NewTriggerActivity, LinkedList())
        }

        activity_new_trigger_actions.adapter = adapter
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putSerializable(actionsKey, adapter.values)
    }

    override fun onResume() {
        super.onResume()

        val nfcChecker = NFCSettingChecker(this)
        if (nfcChecker.isNFCEnabled()) {
            activity_new_trigger_btn_write.isEnabled = true
        } else {
            activity_new_trigger_btn_write.isEnabled = false
            nfcChecker.checkNFCSetting()
        }
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
                        adapter.add(action)
                        toast(getString(R.string.action_added, action.toString()))
                    },
                    0, 0, true)
            timePicker.show()
        }
    }

    override fun onNewIntent(intent: Intent) {
        val tagFromIntent: Tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)

        try {
            if (nfcWriter.writeTag(tagFromIntent, adapter.values)) {
                toast(getString(R.string.writing_success))
                vibrateShortly()
            }
        } catch (e: Exception) {
            e.message?.let { toast(it) }
            vibrateShortly()
        }
    }

    private fun vibrateShortly() {
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android Oreo or newer
            v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            // older version than Android Oreo
            // deprecated in API 26
            v.vibrate(200)
        }

    }


}
