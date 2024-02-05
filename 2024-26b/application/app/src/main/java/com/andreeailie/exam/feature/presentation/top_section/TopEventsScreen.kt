package com.andreeailie.exam.feature.presentation.top_section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andreeailie.exam.R
import com.andreeailie.exam.feature.domain.model.Entity
import com.andreeailie.exam.feature.presentation.entities.EntitiesViewModel


@Composable
fun TopEventsScreen(viewModel: EntitiesViewModel = hiltViewModel()) {
    val topCategories = viewModel.topEvents.value

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            SetTitle()
            topCategories.forEach { categoryCount ->
                CategoryItem(categoryCount)
            }
        }
    }
}

@Composable
fun CategoryItem(entity: Entity) {
    Row(
        modifier = Modifier
            .padding(top = 1.dp, bottom = 8.dp, start = 24.dp, end = 24.dp)
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.light_purple),
                shape = AbsoluteRoundedCornerShape(
                    topLeft = 10.dp,
                    topRight = 10.dp,
                    bottomLeft = 10.dp,
                    bottomRight = 10.dp
                )
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp, start = 10.dp)

        ) {
            Text(
                text = entity.name,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = entity.team,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = entity.details,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = entity.status,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = entity.participants,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = entity.type,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
fun SetTitle(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(start = 25.dp, top = 10.dp)
    ) {
        Text(
            text = "Top Categories",
            textAlign = TextAlign.Left,
            color = colorResource(R.color.purple),
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

