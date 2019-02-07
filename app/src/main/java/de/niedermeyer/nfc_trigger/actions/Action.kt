package de.niedermeyer.nfc_trigger.actions

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.Serializable

/**
 * Superclass for all actions.
 *
 * @author Elen Niedermeyer, Milan Höllner, last update 2019-02-07
 */
@Serializable
abstract class Action : Parcelable {

    /** the actions type, one value of {@link ActionTypes} */
    abstract var TYPE: Int

    /** values if needed for this action */
    abstract var VAL: Array<Int>

    /** the actions execution */
    abstract fun doAction()

    /** @see Parcelable.describeContents */
    override fun describeContents(): Int {
        return hashCode()
    }

    /** @see Parcelable.writeToParcel */
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(TYPE)
        dest?.writeArray(VAL)
    }
}
