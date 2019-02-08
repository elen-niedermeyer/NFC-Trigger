package de.niedermeyer.nfc_trigger.trigger.creation.onOffDialogs

import android.content.Context
import de.niedermeyer.nfc_trigger.R

/**
 * Manages the dialog for creating a wifi action.
 *
 * @param context
 *
 * @author Elen Niedermeyer, last update 2019-02-08
 */
class WifiDialogHolder(context: Context) : OnOffDialogHolder(context) {

    /**
     * Constructor wit parameter.
     *
     * @param context
     * @param currentValue if there's a value to select, it's given here
     */
    constructor(context: Context, currentValue: Int) : this(context) {
        super.chosenValue = currentValue
    }

    init {
        super.dialog.setTitle(R.string.action_wifi_dialog_title)
    }

}
