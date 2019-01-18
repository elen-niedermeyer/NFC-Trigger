package de.niedermeyer.nfc_trigger.actions

import kotlinx.serialization.Serializable

/**
 * Superclass for all actions.
 */
@Serializable
abstract class Action {

    /** the actions type, one value of {@link ActionConstants} */
    abstract var TYPE: Int

    /** values if needed for this action */
    abstract var VAL: Array<Int>

    /** the actions execution */
    abstract fun doAction()
}