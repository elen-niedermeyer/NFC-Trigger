package de.niedermeyer.nfc_trigger.CreateTrigger

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.niedermeyer.nfc_trigger.R
import de.niedermeyer.nfc_trigger.actions.Action
import de.niedermeyer.nfc_trigger.actions.alarm.AlarmAction
import de.niedermeyer.nfc_trigger.actions.alarm.NewAlarmDialog
import kotlinx.android.synthetic.main.activity_new_trigger.*
import kotlinx.android.synthetic.main.dialog_spinner.*
import java.util.*

class NewTriggerActivity : AppCompatActivity() {

    val triggerActions = LinkedList<Action>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_trigger)

        activity_new_trigger_btn_add.setOnClickListener {
            val dialog = ChooseActionDialog(this@NewTriggerActivity)
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Next", { dialog, _ ->
                dialog.dismiss()

                if (dialog is ChooseActionDialog) {
                    var chosenAction = dialog.spinner.selectedItem.toString()

                    if (chosenAction == "Alarm") {
                        val alarmDialog = NewAlarmDialog(this@NewTriggerActivity)
                        val dialogAlarm = alarmDialog.getDialog()
                        dialogAlarm.setButton(Dialog.BUTTON_POSITIVE, "OK", { dialog, _ ->
                            // freuen
                        })

                        dialogAlarm.show()
                    }
                }
            })

            dialog.show()

        }
    }

    private fun addChosenAction(actionName: String) {
        var dialog: Dialog? = null

        if (actionName == "Alarm") {
            val alarmDialog = NewAlarmDialog(this@NewTriggerActivity)
            dialog = alarmDialog.getDialog()
            dialog.setButton(Dialog.BUTTON_POSITIVE, "OK", { dialog, _ ->
                triggerActions.add(AlarmAction(alarmDialog.hours, alarmDialog.minutes))
            })
        }

        dialog!!.show()
    }
}
