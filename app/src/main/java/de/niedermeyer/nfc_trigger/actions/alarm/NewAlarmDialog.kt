package de.niedermeyer.nfc_trigger.actions.alarm

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker
import de.niedermeyer.nfc_trigger.R

class NewAlarmDialog(context: Context) : TimePickerDialog(context, null, 0, 0, true) {

    var hours = 0
    var minutes = 0

    init {
        this.setTitle(context.getString(R.string.alarm))
        this.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", { dialog, _ -> dialog.cancel() })
    }

    override fun onTimeChanged(view: TimePicker?, hourOfDay: Int, minute: Int) {
        super.onTimeChanged(view, hourOfDay, minute)

        hours = hourOfDay
        minutes = minute
    }

}