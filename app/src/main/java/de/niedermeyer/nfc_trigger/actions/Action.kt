package de.niedermeyer.nfc_trigger.actions

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.Serializable

/**
 * Superclass for all actions.
 *
 * @author Elen Niedermeyer, last update 2019-01-30
 */
@Serializable
abstract class Action : Parcelable {

    /** the actions type, one value of {@link ActionTypes} */
    abstract var TYPE: Int

    /** values if needed for this action */
    abstract var VAL: Array<Int>

    /** the actions execution */
    abstract fun doAction()

    override fun describeContents(): Int {
        return hashCode()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(TYPE)
        dest?.writeArray(VAL)
    }
}
