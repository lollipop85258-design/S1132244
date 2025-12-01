package tw.edu.pu.csim.tcyang.s1132244

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import tw.edu.pu.csim.tcyang.s1132244.ui.theme.S1132244Theme
import java.util.Locale

const val AUTHOR_INFO = "資管二A 郭崇承"

@Composable
fun ExamScreen(vm: ExamViewModel = viewModel()) {

    val width = vm.screenWidthPx
    val height = vm.screenHeightPx
    val score = vm.score

    val density = LocalDensity.current

    val iconSizePx = 300f
    val iconSizeDp: Dp = with(density) { iconSizePx.toDp() }

    val leftXOffsetPx = 0f
    val rightXOffsetPx = width - iconSizePx

    val topYOffsetPx = (height / 2f - iconSizePx).coerceAtLeast(0f)
    val bottomYOffsetPx = (height - iconSizePx).coerceAtLeast(0f)

    val serviceIconXDp = with(density) { vm.serviceIconX.toDp() }
    val serviceIconYDp = with(density) { vm.serviceIconY.toDp() }

    val draggableState = rememberDraggableState { delta ->
        val newX = (vm.serviceIconX + delta).coerceIn(0f, width - iconSizePx)
        vm.updateIconX(newX)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.happy),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "瑪利亞基金會服務大考驗", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "作者: $AUTHOR_INFO", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = String.format(Locale.getDefault(), "螢幕大小: %.1f * %.1f", width, height),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "成績: $score 分 ${vm.statusMessage}", fontSize = 16.sp)
        }

        Image(
            painter = painterResource(id = vm.serviceIconResId),
            contentDescription = null,
            modifier = Modifier
                .size(iconSizeDp)
                .offset(x = serviceIconXDp, y = serviceIconYDp)
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal
                )
        )

        Image(
            painter = painterResource(id = R.drawable.role0),
            contentDescription = null,
            modifier = Modifier
                .size(iconSizeDp)
                .offset(
                    x = with(density) { leftXOffsetPx.toDp() },
                    y = with(density) { topYOffsetPx.toDp() }
                )
        )

        Image(
            painter = painterResource(id = R.drawable.role1),
            contentDescription = null,
            modifier = Modifier
                .size(iconSizeDp)
                .offset(
                    x = with(density) { rightXOffsetPx.toDp() },
                    y = with(density) { topYOffsetPx.toDp() }
                )
        )

        Image(
            painter = painterResource(id = R.drawable.role2),
            contentDescription = null,
            modifier = Modifier
                .size(iconSizeDp)
                .offset(
                    x = with(density) { leftXOffsetPx.toDp() },
                    y = with(density) { bottomYOffsetPx.toDp() }
                )
        )

        Image(
            painter = painterResource(id = R.drawable.role3),
            contentDescription = null,
            modifier = Modifier
                .size(iconSizeDp)
                .offset(
                    x = with(density) { rightXOffsetPx.toDp() },
                    y = with(density) { bottomYOffsetPx.toDp() }
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExamScreenPreview() {
    S1132244Theme {
        val previewVm = ExamViewModel().apply { setScreenSize(1080f, 1920f) }
        ExamScreen(previewVm)
    }
}