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
import kotlinx.android.synthetic.main.activity_new_trigger_action_bar.view.*
import java.util.*
import org.jetbrains.anko.toast

class ActionListAdapter(private val adapterContext: Context, var values: LinkedList<Action>):ArrayAdapter<Action>(adapterContext, -1, values) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val layoutInflater = adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val actionBarLayout = layoutInflater.inflate(R.layout.activity_new_trigger_action_bar, null)
        val currentAction = values[position]

        actionBarLayout.activity_new_trigger_action_bar_text.text = values[position].toString()

        if (currentAction is AlarmAction) {
            // set functionality for action edit button
            actionBarLayout.activity_new_trigger_action_bar_btn_edit.setOnClickListener {
                val timePicker = TimePickerDialog(context,
                        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                            currentAction.hours = hourOfDay
                            currentAction.minutes = minute
                            notifyDataSetChanged()
                            context.toast(context.getString(R.string.action_updated, currentAction.toString()))
                        },
                        currentAction.hours, currentAction.minutes, true)
                timePicker.show()
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