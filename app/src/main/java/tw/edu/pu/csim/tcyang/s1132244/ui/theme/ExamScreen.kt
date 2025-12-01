package tw.edu.pu.csim.tcyang.s1132244

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import tw.edu.pu.csim.tcyang.s1132244.ui.theme.S1132244Theme

const val AUTHOR_INFO = "資管二A郭崇承"

@Composable
fun ExamScreen(vm: ExamViewModel = viewModel()) {

    val width = vm.screenWidthPx
    val height = vm.screenHeightPx
    val score = vm.score

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.happy),
            contentDescription = "App Logo",
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "瑪利亞基金會服務大考驗", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "作者: $AUTHOR_INFO", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "螢幕大小: ${String.format("%.1f", width)} * ${String.format("%.1f", height)}", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "成績: $score 分", fontSize = 16.sp)
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