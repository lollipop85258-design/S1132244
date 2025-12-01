package tw.edu.pu.csim.tcyang.s1132244

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import tw.edu.pu.csim.tcyang.s1132244.ui.theme.S1132244Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        setContent {
            S1132244Theme {
                val vm: ExamViewModel = viewModel()

                val displayMetrics = resources.displayMetrics
                val widthPx = displayMetrics.widthPixels.toFloat()
                val heightPx = displayMetrics.heightPixels.toFloat() // 使用 dimensionPixels 更準確

                vm.setScreenSize(widthPx, heightPx)

                ExamScreen(vm = vm)
            }
        }
    }
}