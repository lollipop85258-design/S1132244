package tw.edu.pu.csim.tcyang.s1132244

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class ExamViewModel : ViewModel() {
    var screenWidthPx by mutableFloatStateOf(0f)
        private set

    var screenHeightPx by mutableFloatStateOf(0f)
        private set

    var score by mutableStateOf(0)
        private set

    var serviceIconY by mutableFloatStateOf(0f)
    var serviceIconX by mutableFloatStateOf(0f)
    var serviceIconResId by mutableStateOf(R.drawable.service0)

    private var gameJob: Job? = null
    private val iconSizePx = 300f
    private val fallSpeed = 20f
    private val updateIntervalMs = 100L

    init {
        resetIconPosition()
        startGameLoop()
    }

    override fun onCleared() {
        super.onCleared()
        gameJob?.cancel()
    }

    fun setScreenSize(width: Float, height: Float) {
        screenWidthPx = width
        screenHeightPx = height
    }

    fun updateScore(newScore: Int) {
        score = newScore
    }

    fun updateIconX(newX: Float) {
        val maxBound = screenWidthPx - iconSizePx
        serviceIconX = newX.coerceIn(0f, maxBound)
    }

    private fun resetIconPosition() {
        val services = listOf(R.drawable.service0, R.drawable.service1, R.drawable.service2, R.drawable.service3)
        serviceIconResId = services[Random.nextInt(services.size)]

        serviceIconY = 0f
        serviceIconX = screenWidthPx / 2 - iconSizePx / 2
    }

    private fun startGameLoop() {
        gameJob?.cancel()
        gameJob = viewModelScope.launch {
            while (true) {
                delay(updateIntervalMs)
                serviceIconY += fallSpeed

                if (serviceIconY + iconSizePx >= screenHeightPx) {
                    resetIconPosition()
                    updateScore(score + 1)
                }
            }
        }
    }
}