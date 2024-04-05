package com.amonteiro.testnfc.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.amonteiro.testnfc.ui.theme.TestNFCTheme





@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyErrorPreview() {
    //Il faut remplacer NomVotreAppliTheme par le thème de votre application
    //Utilisé par exemple dans MainActivity.kt sous setContent {...}
    TestNFCTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column {
                MyError(errorMessage = "Avec message d'erreur")
                Text("Sans erreur : ")
                MyError(errorMessage = "")
                Text("----------")
                RadioButtonWithLabel(label = "Coucou", selected = true) {}
            }
        }
    }
}

@Composable
fun RadioButtonWithLabel(
    modifier:Modifier = Modifier,
    label: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(modifier = modifier,
        verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selected,
            onClick = onSelect
        )
        Text(
            text = label,
            modifier = Modifier.clickable(onClick = onSelect)
        )
    }
}

@Composable
fun MyError(
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
) {
    AnimatedVisibility(!errorMessage.isNullOrBlank()) {
        Row(
            modifier = modifier
                .background(MaterialTheme.colorScheme.error)
                .padding(8.dp)

        ) {

            Text(
                text = errorMessage ?: "",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onError,
                modifier = Modifier.weight(1f)
            )
        }
    }

}