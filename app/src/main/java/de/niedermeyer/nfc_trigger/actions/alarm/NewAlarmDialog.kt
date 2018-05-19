package de.niedermeyer.nfc_trigger.actions.alarm

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import de.niedermeyer.nfc_trigger.R

class NewAlarmDialog(val context: Context) {

    var hours = 0
    var minutes = 0

    public fun getDialog(): TimePickerDialog {

        val onTimeSetListener = TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
            // TODO: this doesn't work
            hours = selectedHour
            minutes = selectedMinute
        }

        val dialog = TimePickerDialog(context, onTimeSetListener, 0, 0, true)
        dialog.setTitle(context.getString(R.string.alarm))

        dialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", {dialog, _ -> dialog.cancel() })

        return dialog
    }

}