package de.niedermeyer.nfc_trigger.trigger.creation.onOffDialogs

import android.app.AlertDialog
import android.content.Context
import de.niedermeyer.nfc_trigger.R

/**
 * Manages the dialog for turning an on/off action. Gives the chosen values.
 *
 * @param context
 *
 * @author Elen Niedermeyer, last update 2019-02-08
 */
abstract class OnOffDialogHolder(context: Context) {

    /** the dialog */
    var dialog: AlertDialog
        private set

    /** the value chosen by the user, it's 0 for off and 1 for on when dialog dismisses */
    var chosenValue = -1

    /** array of items to set as choice in the dialog */
    private val items = arrayOf(
            context.getString(R.string.off).toUpperCase(),
            context.getString(R.string.on).toUpperCase())

    init {
        // make a builder, set choices
        val builder = AlertDialog.Builder(context)
        builder
                .setSingleChoiceItems(items, chosenValue) { dialog, itemIndex ->
                    // index in array is the same like the value
                    chosenValue = itemIndex
                    dialog.dismiss()
                }
        // initialize dialog field
        dialog = builder.create()
    }

}
