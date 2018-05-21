package de.niedermeyer.nfc_trigger.CreateTrigger

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import de.niedermeyer.nfc_trigger.R
import de.niedermeyer.nfc_trigger.actions.Action
import java.util.*

class ActionListAdapter(private val adapterContext: Context, var values: LinkedList<Action>):ArrayAdapter<Action>(adapterContext, -1, values) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val layoutInflater = adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val actionBarLayout = layoutInflater.inflate(R.layout.activity_new_trigger_action_bar, null)

        return actionBarLayout

    }

}