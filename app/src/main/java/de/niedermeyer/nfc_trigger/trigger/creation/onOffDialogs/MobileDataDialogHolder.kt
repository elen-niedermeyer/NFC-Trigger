package de.niedermeyer.nfc_trigger.trigger.creation.onOffDialogs

import android.content.Context
import de.niedermeyer.nfc_trigger.R

class MobileDataDialogHolder(context: Context) :OnOffDialogHolder(context){

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
        super.dialog.setTitle(R.string.action_mobile_data_dialog_title)
    }

}