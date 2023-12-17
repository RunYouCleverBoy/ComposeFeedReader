package com.playgrounds.daggerkspplayground.screens.primaryscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryScreen(lastSurfData: String, onSurfDataClicked: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Last surf data:\n$lastSurfData",
            style = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center,
                textDirection = TextDirection.Content))
        Spacer(modifier = Modifier.height(30.dp))
        ElevatedButton(onClick = { onSurfDataClicked() }) {
            Text(text = "Go")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrimaryScreenPreview() {
    PrimaryScreen(lastSurfData = "None", onSurfDataClicked = {})
}