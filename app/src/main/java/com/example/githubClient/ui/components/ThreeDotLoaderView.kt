package com.example.githubClient.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.githubClient.R
import com.example.githubClient.ui.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ConnectingDotsAnimation() {
    val coroutineScope = rememberCoroutineScope()
    var currentDot by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = coroutineScope) {
        coroutineScope.launch {
            while (true) {
                delay(300)
                currentDot = (currentDot + 1) % 4
            }
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        for (i in 1..3) {
            ConnectingDot(visible = i == currentDot)
            if (i < 3) Spacer(modifier = Modifier.width(2.dp))
        }
    }
}

@Composable
fun ConnectingDot(visible: Boolean) {
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0.2f,
        animationSpec = TweenSpec(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    CircularProgressIndicator(
        strokeWidth = 2.dp,
        progress = 1f,
        modifier = Modifier
            .width(4.dp)
            .height(4.dp)
            .padding(top = 4.dp),
        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha),
    )
}

@Composable
fun ConnectingDotsWithText() {
    AppTheme {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(46.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
            ) {
                Text(
                    modifier = Modifier.padding(end = 4.dp),
                    text = "Connecting",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyMedium
                )
                ConnectingDotsAnimation()
            }
        }
    }
}

@Composable
fun MyApp() {
    ConnectingDotsWithText()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp()
}
