package de.niedermeyer.nfc_trigger.actions

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.Serializable

@Serializable
abstract class Action : Parcelable {

    abstract var TYPE: Int

    abstract var VAL: Array<Int>

    abstract fun doAction()

    override fun describeContents(): Int {
        return hashCode()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(TYPE)
        dest?.writeArray(VAL)
    }
}