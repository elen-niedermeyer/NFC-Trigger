package de.niedermeyer.nfc_trigger.nfc.enabling

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.provider.Settings
import de.niedermeyer.nfc_trigger.R


class NFCEnabler(val context: Context) {

    fun proofNFC() {
        if (!isNFCEnabled()) {
            displayNFCSettingsDialog()
        }
    }

    fun isNFCEnabled(): Boolean {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        return nfcAdapter.isEnabled
    }

    fun displayNFCSettingsDialog() {
        AlertDialog.Builder(context)
                .setTitle(R.string.enable_nfc)
                .setMessage(R.string.enable_nfc_message)
                .setPositiveButton(R.string.settings, { _, _ -> openNFCSettings() })
                .setNegativeButton(R.string.cancel, { dialog, _ -> dialog.dismiss() })
                .show()
    }

    private fun openNFCSettings() {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        context.startActivity(intent)
    }

}