package de.niedermeyer.nfc_trigger.trigger.creation

import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import de.niedermeyer.nfc_trigger.R
import de.niedermeyer.nfc_trigger.actions.Action
import de.niedermeyer.nfc_trigger.actions.alarm.AlarmAction
import de.niedermeyer.nfc_trigger.actions.wifi.WifiAction
import kotlinx.android.synthetic.main.activity_new_trigger_action_bar.view.*
import org.jetbrains.anko.toast
import java.util.*

class ActionListAdapter(private val adapterContext: Context, var values: LinkedList<Action>) : ArrayAdapter<Action>(adapterContext, -1, values) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater = adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val actionBarLayout = layoutInflater.inflate(R.layout.activity_new_trigger_action_bar, null)
        val currentAction = values[position]

        actionBarLayout.activity_new_trigger_action_bar_text.text = values[position].toString()

        // set functionality for action edit button
        if (currentAction is AlarmAction) {
            // alarm action
            actionBarLayout.activity_new_trigger_action_bar_btn_edit.setOnClickListener {
                // make time picker dialog with given time
                // save new values for hour and minute with the action when the time is changed
                val timePicker = TimePickerDialog(context,
                        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                            currentAction.hours = hourOfDay
                            currentAction.minutes = minute
                            notifyDataSetChanged()
                            // show toast as confirmation
                            context.toast(context.getString(R.string.action_updated, currentAction.toString()))
                        },
                        currentAction.hours, currentAction.minutes, true)
                timePicker.show()
            }

        } else if (currentAction is WifiAction) {
            // wifi action
            actionBarLayout.activity_new_trigger_action_bar_btn_edit.setOnClickListener {
                // get dialog from holder with selected value
                val dialogHolder = WifiDialogHolder(context, currentAction.toTurnOn)
                val dialog = dialogHolder.dialog
                // save new value with action when the dialog is dismissed
                dialog.setOnDismissListener {
                    currentAction.toTurnOn = dialogHolder.chosenValue
                    notifyDataSetChanged()
                    // show toast as confirmation
                    context.toast(context.getString(R.string.action_updated, currentAction.toString()))
                }
                dialog.show()
            }
        }

        // set functionality for action delete button
        actionBarLayout.activity_new_trigger_action_bar_btn_delete.setOnClickListener {
            remove(currentAction)
            context.toast(context.getString(R.string.action_deleted, currentAction.toString()))
        }

        return actionBarLayout
    }

}