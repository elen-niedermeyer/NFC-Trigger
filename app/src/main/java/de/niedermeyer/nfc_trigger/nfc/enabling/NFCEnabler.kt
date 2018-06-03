package de.niedermeyer.nfc_trigger.nfc.enabling

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Build
import android.provider.Settings


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
                .setTitle("Enable NFC")
                .setMessage("Please, enable your NFC to use this app.")
                .setPositiveButton("Open settings", { _, _ -> openNFCSettings() })
                .setNegativeButton("Cancel", { dialog, _ -> dialog.dismiss() })
                .show()
    }

    private fun openNFCSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            val intent = Intent(Settings.ACTION_NFC_SETTINGS)
            context.startActivity(intent)
        } else {
            val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            context.startActivity(intent)
        }
    }

}