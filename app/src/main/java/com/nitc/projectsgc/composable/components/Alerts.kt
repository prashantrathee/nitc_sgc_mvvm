package com.nitc.projectsgc.composable.components

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp



@Composable
fun SimpleToast(message: String) {
    val context = LocalContext.current
    LaunchedEffect(true) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun SimpleSnackBar(
    snackbarHostState: SnackbarHostState = remember {
        SnackbarHostState()
    },
    text: String
) {
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    SnackbarHost(hostState = snackBarHostState, modifier = Modifier.padding(5.dp)){
        Snackbar(
            content = {
                Text(text = text)
            }
        )
    }

}