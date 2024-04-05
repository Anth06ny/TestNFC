package com.amonteiro.testnfc.ui.screen

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amonteiro.testnfc.MainViewModel
import com.amonteiro.testnfc.ui.MyError
import com.amonteiro.testnfc.ui.RadioButtonWithLabel
import com.amonteiro.testnfc.ui.theme.TestNFCTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@Preview(showBackground = true, showSystemUi = true, locale = "fr")
@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun TestNFCScreenPreview() {

    //Il faut remplacer NomVotreAppliTheme par le thème de votre application
    //Utilisé par exemple dans MainActivity.kt sous setContent {...}
    TestNFCTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            TestNFCScreen(MainViewModel().apply {
                bundleKey = listOf("Clé1", "Clé2")
                action = "Blabla.action"
                writeText = "Coucou"
                readAction = false
                errorMessage = "Un message d'erreur"
            })
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TestNFCScreen(mainViewModel: MainViewModel = viewModel()) {

    //tag NFC format NDEF
    //Intent de detection de TAG NFC
    //ACTION_NDEF_DISCOVERED > ACTION_TECH_DISCOVERED > ACTION_TAG_DISCOVERED
    //On utilisera plutot le premier


    Column(modifier = Modifier.padding(10.dp)) {

        Text(
            text = "Tag",
            fontSize = 40.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        )

        MyError(
            errorMessage = mainViewModel.errorMessage,
        )

        Row(modifier = Modifier.fillMaxWidth()) {

            RadioButtonWithLabel(label = "Read", selected = mainViewModel.readAction, modifier = Modifier.weight(1f)) {
                mainViewModel.readAction = true
            }

            RadioButtonWithLabel(label = "Write", selected = !mainViewModel.readAction, modifier = Modifier.weight(1f)) {
                mainViewModel.readAction = false
            }
        }

        //Zone d'edition pour le prochain tag
        AnimatedVisibility(visible = !mainViewModel.readAction) {
            TextField(
                value = mainViewModel.writeText, //Valeur affichée
                onValueChange = { newValue: String -> mainViewModel.writeText = newValue }, //Nouveau texte entrée
                leadingIcon = { //Image d'icone
                    Icon(
                        imageVector = Icons.Default.Edit,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )
                },
                singleLine = true,
                label = { Text("Texte à écrire sur le Tag NFC") }, //Texte d'aide qui se déplace
                //Comment le composant doit se placer
                modifier = Modifier
                    .fillMaxWidth() // Prend toute la largeur
                    .heightIn(min = 56.dp) //Hauteur minimum
            )
        }
        Spacer(Modifier.size(8.dp))



        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(8.dp)) {
                Row {
                    Text(text = "action : ", color = MaterialTheme.colorScheme.primary)
                    Text(text = mainViewModel.action ?: "")
                }
                Spacer(Modifier.size(8.dp))

                Text(text = "BundleKey : ", color = MaterialTheme.colorScheme.primary)
                Text(text = mainViewModel.bundleKey?.joinToString("\n") { "-$it" } ?: "")
            }
        }

        Spacer(Modifier.size(8.dp))

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Row {
                    Text(text = "id : ", color = MaterialTheme.colorScheme.primary)
                    Text(text = mainViewModel.tagObservable?.id ?: "?")
                }
                Spacer(Modifier.size(8.dp))

                Row {
                    Text(text = "Editable : ", color = MaterialTheme.colorScheme.primary)
                    Text(text = mainViewModel.tagObservable?.editable?.toString()  ?: "?")
                }
                Spacer(Modifier.size(8.dp))
                Row {
                    Text(text = "Blocable : ", color = MaterialTheme.colorScheme.primary)
                    Text(text = mainViewModel.tagObservable?.blocable?.toString() ?: "?")
                }
                Spacer(Modifier.size(8.dp))

                Text(text = "TechList : ", color = MaterialTheme.colorScheme.primary)
                Text(text = mainViewModel.tagObservable?.techList ?: "?")
            }
        }

        Spacer(Modifier.size(8.dp))

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = "Message : ", color = MaterialTheme.colorScheme.primary)
                //Un NdefMessage peut avoir plusieurs records.
                Text(text = mainViewModel.tagObservable?.message ?: "")
            }
        }
    }
}

