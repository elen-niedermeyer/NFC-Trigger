package de.niedermeyer.nfc_trigger.trigger.creation

import android.app.AlertDialog
import android.content.Context
import de.niedermeyer.nfc_trigger.R

/**
 * Manages the dialog for creating a wifi action. Gives the chosen values.
 *
 * @param context
 */
class WifiDialogHolder(context: Context) {

    /**
     * Constructor wit parameter.
     *
     * @param context
     * @param currentValue if there's a value to select, it's given here
     */
    constructor(context: Context, currentValue: Int) : this(context) {
        this.chosenValue = currentValue
    }

    /** the dialog */
    var dialog: AlertDialog
        private set

    /** the value chosen by the user */
    var chosenValue = -1
        private set

    /** array of items to set as choice in the dialog */
    private val items = arrayOf(
            context.getString(R.string.off).toUpperCase(),
            context.getString(R.string.on).toUpperCase())

    init {
        // make a builder, set title and choices
        val builder = AlertDialog.Builder(context)
        builder
                .setTitle(R.string.action_wifi_dialog_title)
                .setSingleChoiceItems(items, chosenValue) { dialog, itemIndex ->
                    // index in array is the same like the value
                    chosenValue = itemIndex
                    dialog.dismiss()
                }
        // initialize dialog field
        dialog = builder.create()
    }

}