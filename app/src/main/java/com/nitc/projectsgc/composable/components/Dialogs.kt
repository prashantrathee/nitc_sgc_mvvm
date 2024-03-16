package com.nitc.projectsgc.composable.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.nitc.projectsgc.R
import com.nitc.projectsgc.composable.util.DateUtils
import java.time.format.DateTimeFormatter


@Composable
fun RemarkDialog(value: String, setShowDialog: (Boolean) -> Unit, setValue: (String) -> Unit) {

    val txtFieldError = remember { mutableStateOf("") }
    val txtField = remember { mutableStateOf(value) }

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 0.dp
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Complete Appointment",
                            style = TextStyle(
                                fontSize = 21.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "",
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable { setShowDialog(false) }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    CardInputFieldWithValue(
                        modifier = Modifier
                            .fillMaxWidth(),
                        hint = "Remarks",
                        isPassword = false,
                        text = "",
                        onValueChanged = {

                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

//                    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                        BasicButton(
                            clickCallback = {
                                if (txtField.value.isEmpty()) {
                                    txtFieldError.value = "Field can not be empty"
                                }else{
                                    setValue(txtField.value)
                                    setShowDialog(false)
                                }
                            },
                            tc = Color.White,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.navy_blue)
                            ),
                            modifier = Modifier,
                            text = "Done"
                        )
//                    }
                }
            }
        }
    }
}
//
//@Preview
//@Composable
//fun RemarkDialogPreview(){
//    RemarkDialog(value = "Remarks", setShowDialog = {}) {
//
//    }
//}


@Preview
@Composable
fun DateDialogPreview(){
    DateDialog("Reschedule",true) {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateDialog(
    heading:String,
    isVisible:Boolean,
    dateSelected:(String)->Unit
){
    val openDialog = remember {
        mutableStateOf(isVisible)
    }
    val dateState = rememberDatePickerState()
    val dateString = DateUtils().dateToString(dateState.selectedDateMillis!!)
    if(openDialog.value){
        DatePickerDialog(
            modifier = Modifier.fillMaxSize(),
            onDismissRequest = {
            openDialog.value = false
        }, confirmButton = {
            if(dateState.selectedDateMillis != null) {
                dateSelected(dateString)
                openDialog.value = false
            }else{
//                Toast.makeText(LocalContext.current,"Select date first",Toast.LENGTH_LONG).show()
            }
        }) {
            DatePicker(
                state = dateState,
                title = {
                    HeadingText(text = heading, fontColor = Color.Black, modifier = Modifier)
                },
                headline = {
                           Text(dateString)
                },
                showModeToggle = true
            )
        }
    }
}