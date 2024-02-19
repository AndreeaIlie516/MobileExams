package com.andreeailie.reexam.feature.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.andreeailie.reexam.R
import com.andreeailie.reexam.feature.domain.model.Entity

@Composable
fun EntityCellSimple(
    modifier: Modifier = Modifier,
    entity: Entity,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(bottom = 8.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.light_purple),
                shape = AbsoluteRoundedCornerShape(
                    topLeft = 10.dp,
                    topRight = 10.dp,
                    bottomLeft = 10.dp,
                    bottomRight = 10.dp
                )
            )
            .clickable {
                onClick()
            },
        verticalArrangement = Arrangement.Top

        ) {
        Column(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp, start = 10.dp)

        )
        {
            Row {
                Column {
                    Text(
                        text = "Title:  " + entity.title,
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Text(
                        text = "Author:  " + entity.author,
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Text(
                        text = "Genre:  " + entity.genre,
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Text(
                        text = "Year:  " + entity.year,
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Text(
                        text = "ISBN:  " + entity.ISBN,
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Text(
                        text = "Availability:  " + entity.availability,
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
            }
        }
    }
}