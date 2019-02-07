package de.niedermeyer.nfc_trigger.trigger.creation

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import de.niedermeyer.nfc_trigger.R
import kotlinx.android.synthetic.main.dialog_spinner.view.*

/**
 * An alert dialog where the user can choose an action
 *
 * @param context
 *
 * @author Elen Niedermeyer, last update 2019-01-30
 */
class ChooseActionDialog(context: Context) : AlertDialog(context) {

    init {
        setTitle(context.getString(R.string.choose_action))

        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_spinner, null)

        // set the content of the drop down box
        val adapter = ArrayAdapter.createFromResource(context, R.array.actions_list, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogView.spinner.adapter = adapter
        setView(dialogView)

        setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.cancel), { dialog, _ -> dialog.dismiss() })
    }

}