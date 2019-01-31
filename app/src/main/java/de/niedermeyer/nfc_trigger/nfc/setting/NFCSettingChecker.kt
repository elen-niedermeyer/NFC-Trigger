package de.niedermeyer.nfc_trigger.nfc.setting

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.provider.Settings
import de.niedermeyer.nfc_trigger.R

/**
 * Handles checking if nfc option is enabled and if not, reminding the user to turn it on.
 *
 * @param context
 *
 * @author Elen Niedermeyer, last update 2019-01-30
 */
class NFCSettingChecker(val context: Context) {

    /**
     * Displays a dialog if NFC is not enabled. Does nothing otherwise.
     */
    fun checkNFCSetting() {
        if (!isNFCEnabled()) {
            displayNFCSettingsDialog()
        }
    }

    /**
     * Gets an {@link NFCAdapter} and check if NFC is enabled.
     *
     * @return true if NFC is enabled, false otherwise
     */
    fun isNFCEnabled(): Boolean {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        return nfcAdapter.isEnabled
    }

    /**
     * Creates and shows a dialog to prompt the user to enable nfc.
     */
    private fun displayNFCSettingsDialog() {
        AlertDialog.Builder(context)
                .setTitle(R.string.enable_nfc)
                .setMessage(R.string.enable_nfc_message)
                .setPositiveButton(R.string.settings, { _, _ -> openNFCSettings() })
                .setNegativeButton(R.string.cancel, { dialog, _ -> dialog.dismiss() })
                .show()
    }

    /**
     * Opens the nfc settings through an intent.
     */
    private fun openNFCSettings() {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        context.startActivity(intent)
    }

}