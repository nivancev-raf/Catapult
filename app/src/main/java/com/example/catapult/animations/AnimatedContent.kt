package com.example.catapult.animations

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.runtime.Composable

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@ExperimentalAnimationApi
@Composable
fun AnimatedQuestionContent(
    targetState: Int,
    content: @Composable () -> Unit
) {
    AnimatedContent(
        targetState = targetState,
        transitionSpec = {
            val enterTransition = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn() + scaleIn(initialScale = 0.8f)
            val exitTransition = slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }) + fadeOut() + scaleOut(targetScale = 1.2f)
            enterTransition with exitTransition using SizeTransform(clip = false)
        },
        content = { content() }, label = ""
    )
}