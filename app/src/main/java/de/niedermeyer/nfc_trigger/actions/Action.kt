package de.niedermeyer.nfc_trigger.actions

import kotlinx.serialization.Serializable

@Serializable
abstract class Action {

    abstract var TYPE: String

    abstract var VALUES: Array<String>

    abstract fun doAction()
}