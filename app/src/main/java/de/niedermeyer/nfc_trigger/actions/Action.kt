package de.niedermeyer.nfc_trigger.actions

abstract class Action {

    abstract var NFC_MESSAGE: String?
        protected set

    abstract fun doAction()
}