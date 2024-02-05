package com.andreeailie.exam.feature.presentation.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.andreeailie.exam.R
import com.andreeailie.exam.feature.presentation.util.Screen
import kotlinx.coroutines.CancellationException


@Composable
fun AddButton(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val touchedDown = remember { mutableStateOf(false) }

    var backgroundColor = colorResource(id = R.color.purple)

    if (touchedDown.value) {
        backgroundColor = colorResource(id = R.color.purple_700)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd
    ) {
        Card(
            modifier = modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            // log
                            touchedDown.value = true

                            val released = try {
                                tryAwaitRelease()
                            } catch (c: CancellationException) {
                                false
                            }

                            if (released) {
                                touchedDown.value = false
                                navController.navigate(Screen.AddEditScreen.route)
                            }
                        }
                    )
                }
                .size(50.dp)
                .align(Alignment.BottomEnd),
            shape = AbsoluteRoundedCornerShape(
                topLeft = 15.dp,
                topRight = 15.dp,
                bottomLeft = 15.dp,
                bottomRight = 15.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            )
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 13.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(id = R.string.add_button_label),
                    color = colorResource(id = R.color.white),
                    style = TextStyle(
                        fontSize = 35.sp,
                    ),
                )
            }
        }
    }
}