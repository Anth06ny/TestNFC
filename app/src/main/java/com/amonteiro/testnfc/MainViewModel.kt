package com.amonteiro.testnfc

import android.content.Intent
import android.net.Uri
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.Base64

fun main() {


}

class MainViewModel : ViewModel() {

    /* -------------------------------- */
    // Donnée de l'intent
    /* -------------------------------- */
    //Liste des clé qui est transmis avec l'Intent
    var bundleKey by mutableStateOf<List<String>?>(null)

    //Action qui a déclancher l'Intent : ACTION_NDEF_DISCOVERED > ACTION_TECH_DISCOVERED > ACTION_TAG_DISCOVERED
    var action by mutableStateOf<String?>(null)

    /* -------------------------------- */
    // Donnée du Tag
    /* -------------------------------- */
    var tagObservable by mutableStateOf<NFCTagBean?>(null)

    /* -------------------------------- */
    // Donnée pour l'écran
    /* -------------------------------- */
    var writeText by mutableStateOf("")
    var readAction by mutableStateOf(true)//pour les radiobuttons
    var errorMessage by mutableStateOf("")

    fun extractNFCTagBundle(intent: Intent) {
        errorMessage = ""

        try {

            /* -------------------------------- */
            // Propre à l'action recu
            /* -------------------------------- */
            action = intent.action

            //Liste des info dans le Bundle
            // LEs clés :https://developer.android.com/reference/android/nfc/NfcAdapter
            //Les différentes clés qui sont transmis dans le Bundle, dépend du NFC
            bundleKey = intent.extras?.keySet()?.toList()
            println("Key dans le bundle=${intent.extras?.keySet()?.joinToString { it }}")

            /* -------------------------------- */
            // Propre au tag
            /* -------------------------------- */
            //Le tag en quesiton
            val tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG) as? Tag
            println("tag=${tag}")

            val tagBean = NFCTagBean(
                id = Base64.getEncoder().encodeToString(tag?.id),
                techList = tag?.techList?.joinToString("\n") { "-$it" } ?: ""
            )

            //En fonction de la techno on utilise la classe qui va bien
            if(tag?.techList?.contains(Ndef::class.java.getName()) == true) {
                fillWithNdefTag(tagBean, Ndef.get(tag))
            }
            else if(tag?.techList?.contains(NdefFormatable::class.java.getName()) == true){
                fillWithNdefFormatableTag(tagBean, NdefFormatable.get(tag))
            }
            else {
                errorMessage = "Type de Tag non traité"
            }
            tagObservable = tagBean
        }
        catch (e: Exception) {
            e.printStackTrace()
            errorMessage = e.message ?: "Une erreur est survenue"
        }
    }

    private fun fillWithNdefTag(tagBean: NFCTagBean, newTagNdef: Ndef) {
        println("tagNdef=${newTagNdef.cachedNdefMessage}")
        /* -------------------------------- */
        // Lecture
        /* -------------------------------- */
        tagBean.editable = newTagNdef.isWritable
        tagBean.blocable = newTagNdef.canMakeReadOnly()
        tagBean.message = newTagNdef.cachedNdefMessage?.records?.joinToString("\n") {
            //On retire les 3premiers caracthères représenant la langue
            String(it.payload.drop(3).toByteArray())
        } ?: ""

        /* -------------------------------- */
        // Ecriture
        /* -------------------------------- */
        //Si on est dans l'état d'écriture
        if (!readAction) {
            //On regarde si le tag est editable
            if (!newTagNdef.isWritable) {
                throw Exception("Le Tag n'est pas editable")
            }
            else if (writeText.isBlank()) {
                throw Exception("Le texte est vide")
            }

            //Le message a envoyer
            var message = NdefMessage(
                arrayOf(
                    NdefRecord.createTextRecord("fr", writeText),
                    NdefRecord.createUri(Uri.parse("https://www.amonteiro.fr")),
                    //On peut mettre plusieurs message et plusieurs type
                )
            )
            newTagNdef.connect()
            newTagNdef.writeNdefMessage(message)
            newTagNdef.close()
            //Tout a fonctionné on change le message avec copy pour déclancher l'observable
            tagBean.message = writeText
        }
    }

    private fun fillWithNdefFormatableTag(tagBean: NFCTagBean, newTagNdef: NdefFormatable) {
        println("newTagNdef=${newTagNdef}")
        /* -------------------------------- */
        // Lecture
        /* -------------------------------- */

        /* -------------------------------- */
        // Ecriture
        /* -------------------------------- */
        //Si on est dans l'état d'écriture
        if (!readAction) {
            if (writeText.isBlank()) {
                throw Exception("Le texte est vide")
            }

            //Le message a envoyer
            var message = NdefMessage(
                arrayOf(
                    NdefRecord.createTextRecord("fr", writeText),
                )
            )
            newTagNdef.connect()
            //On écrit le 1er message puis ca va devenir un NDEFMessage
            newTagNdef.format(message)
            newTagNdef.close()

            //Tout a fonctionné on change le message avec copy pour déclancher l'observable

            tagBean.message = writeText
        }
    }

//    private fun fillWithMifareTag(tagBean: NFCTagBean, mifareTag: MifareClassic) {
//        println("mifareTag=${mifareTag}")
//        /* -------------------------------- */
//        // Lecture
//        /* -------------------------------- */
//
//        /* -------------------------------- */
//        // Ecriture
//        /* -------------------------------- */
//        //Si on est dans l'état d'écriture
//        if (!readAction) {
//            if (writeText.isBlank()) {
//                throw Exception("Le texte est vide")
//            }
//
//
//            //Le message a envoyer
//            var message = NdefMessage(
//                arrayOf(
//                    NdefRecord.createTextRecord("fr", writeText),
//                )
//            )
//
//
//            newTagNdef.connect()
//            //On écrit le 1er message puis ca va devenir un NDEFMessage
//            newTagNdef.format(message)
//            newTagNdef.close()
//
//            //Tout a fonctionné on change le message avec copy pour déclancher l'observable
//
//            tagBean.message = writeText
//        }
//    }
}

//les infos qu'on utilise
data class NFCTagBean(
    var id: String = "",
    var editable: Boolean? = null,
    var blocable: Boolean? = null,
    var techList: String = "",
    var message: String = "",
)
