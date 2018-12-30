package de.niedermeyer.nfc_trigger.actions

import kotlinx.serialization.Serializable

@Serializable
abstract class Action {

    abstract var TYPE: Int

    abstract var VAL: Array<Int>

    abstract fun doAction()
}