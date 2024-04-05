package com.amonteiro.testnfc

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.amonteiro.testnfc.ui.screen.TestNFCScreen
import com.amonteiro.testnfc.ui.theme.TestNFCTheme


class MainActivity : ComponentActivity() {

    private val nfcAdapter: NfcAdapter by lazy { NfcAdapter.getDefaultAdapter(this) }

    private val mainViewModel by viewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("onCreate")

        setContent {
            TestNFCTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    TestNFCScreen(mainViewModel)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val intent = PendingIntent.getActivity(this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE)

        //Si on veut filtrer sur certain type de NFC
//        val nfcIntentFilter = arrayOf(
//            IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply { addDataType("text/plain") } //*/*
//        )

        //Si on veut filtrer sur certaine techno de NFC
        //val techListsArray = arrayOf(arrayOf(NfcF::class.java.name))

        //Prends la priorité sur les tagnfc
        nfcAdapter.enableForegroundDispatch(this, intent, null, null)
    }

    //On quitte l'écran on desactive l'écoute
    override fun onPause() {
        super.onPause()
        nfcAdapter.disableForegroundDispatch(this)
    }

    //Appeler chaque fois q'un intent se déclenche
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        println("onNewIntent")

        mainViewModel.extractNFCTagBundle(intent)
    }
}
