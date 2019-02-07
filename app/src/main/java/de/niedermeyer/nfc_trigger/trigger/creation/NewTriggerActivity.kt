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
import de.niedermeyer.nfc_trigger.actions.wifi.WifiAction
import de.niedermeyer.nfc_trigger.nfc.setting.NFCSettingChecker
import de.niedermeyer.nfc_trigger.nfc.writing.NFCTagWriter
import kotlinx.android.synthetic.main.activity_new_trigger.*
import kotlinx.android.synthetic.main.dialog_spinner.*
import org.jetbrains.anko.toast
import java.util.*

/**
 * Activity to make an action list and write it to an nfc tag.
 *
 * @author Elen Niedermeyer, last update 2019-01-30
 */
class NewTriggerActivity : AppCompatActivity() {

    /** adapter with action list */
    private lateinit var adapter: ActionListAdapter

    /** nfc tag writer */
    private lateinit var nfcWriter: NFCTagWriter

    /** Key for saving the action list in instance state */
    private val actionsKey: String = "savedActions"

    /**
     * @see android.support.v7.app.AppCompatActivity#onCreate(savedInstanceState: Bundle?)
     * Initialize fields and buttons.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_trigger)

        // initialize the nfc tag writer
        nfcWriter = NFCTagWriter(this)
        val pendingIntent = PendingIntent.getActivity(
                this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        // set the writing action to the button
        activity_new_trigger_btn_write.setOnClickListener {
            nfcWriter.activateNfcIntents(pendingIntent)
        }

        // set the action to the button for adding an action
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
        if (savedActions is LinkedList<*> && savedActions.isNotEmpty() && savedActions[0] is Action ) {
            // cast to LinkedList<Action> is checked
            adapter = ActionListAdapter(this@NewTriggerActivity, savedActions as LinkedList<Action>)
        } else {
            adapter = ActionListAdapter(this@NewTriggerActivity, LinkedList())
        }

        activity_new_trigger_actions.adapter = adapter
    }

    /**
     * @see android.support.v7.app.AppCompatActivity#onSaveInstanceState(outState: Bundle?)
     * Saves the action list.
     */
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putSerializable(actionsKey, adapter.values)
    }

    /**
     * @see android.support.v7.app.AppCompatActivity#onResume()
     * Check if nfc is enabled and display a containing dialog if not.
     * Enableds/Disables the write button depending on nfc setting.
     */
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

    /**
     * @see android.support.v7.app.AppCompatActivity#onPause()
     * Deactivates the nfc intents.
     */
    override fun onPause() {
        super.onPause()

        nfcWriter.deactivateNfcIntents()
    }

    /**
     * @see android.support.v7.app.AppCompatActivity#onNewIntent(intent: Intent)
     * Writes an nfc tag if possible. Shows a message about the success.
     */
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

    /**
     * Displays a dialog to specify the chosen action, if necessary.
     * Adds the action to the list of {@link #adapter}.
     *
     * @param actionName name of the action
     */
    private fun addChosenAction(actionName: String) {

        if (actionName == getString(R.string.action_alarm_name)) {
            // alarm action
            // makes time picker dialog
            val timePicker = TimePickerDialog(this@NewTriggerActivity,
                    TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                        // create action with the given time and add it to the list
                        val action = AlarmAction(this@NewTriggerActivity, hourOfDay, minute)
                        adapter.add(action)
                        toast(getString(R.string.action_added, action.toString()))
                    },
                    0, 0, true)
            // show dialog
            timePicker.show()

        } else if (actionName == getString(R.string.action_wifi_name)) {
            // wifi action
            // makes a dialog to choose on/off
            val dialogHolder = WifiDialogHolder(this)
            val dialog = dialogHolder.dialog
            dialog.setOnDismissListener {
                // create action with the given value and add it to the list
                val action = WifiAction(this@NewTriggerActivity, dialogHolder.chosenValue)
                adapter.add(action)
                toast(getString(R.string.action_added, action.toString()))
            }
            // show dialog
            dialog.show()
        }
    }

    /**
     * Makes a short vibration.
     */
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
