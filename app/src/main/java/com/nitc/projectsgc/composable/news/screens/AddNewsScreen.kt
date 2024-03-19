package com.nitc.projectsgc.composable.news.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.nitc.projectsgc.R
import com.nitc.projectsgc.composable.components.BasicButton
import com.nitc.projectsgc.composable.components.BasicButtonWithEnabled
import com.nitc.projectsgc.composable.components.BasicButtonWithState
import com.nitc.projectsgc.composable.components.CardInputFieldWithValue
import com.nitc.projectsgc.composable.news.NewsViewModel
import com.nitc.projectsgc.composable.util.UserRole
import com.nitc.projectsgc.models.News
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Preview
@Composable
fun AddNewsScreenPreview() {

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewsScreen(
    newsViewModel: NewsViewModel,
    mentorID: String,
    onDismiss: () -> Unit
) {
    val newsState = remember {
        mutableStateOf(
            News(
                mentorID = mentorID,
                publishDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            )
        )
    }

    val submitState = remember {
        mutableStateOf(false)
    }

    val newsContext = LocalContext.current

    LaunchedEffect(key1 = submitState.value) {
        if(submitState.value){
            val added = newsViewModel.addNews(UserRole.Mentor,newsState.value)
            if(added){
                showToast("Added",newsContext)
            }else{
                showToast("Could not add news",newsContext)
            }
            submitState.value = false
        }
    }

    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(),
        containerColor = Color.White,
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = {
            onDismiss()
        }) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            CardInputFieldWithValue(
                hint = "News for students",
                text = newsState.value.news,
                isPassword = false,
                modifier = Modifier
            ) { newsInput ->
                newsState.value = newsState.value.copy(news = newsInput)
            }

            BasicButtonWithEnabled(
                enabled = newsState.value.news.isNotEmpty(),
                text = "Submit", colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.navy_blue)
            ), tc = Color.White, modifier = Modifier) {
                    submitState.value = true
            }
        }
    }
}


fun showToast(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}