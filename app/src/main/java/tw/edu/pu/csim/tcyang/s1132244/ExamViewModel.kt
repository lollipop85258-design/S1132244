package tw.edu.pu.csim.tcyang.s1132244

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class ExamViewModel : ViewModel() {
    var screenWidthPx by mutableFloatStateOf(0f)
        private set

    var screenHeightPx by mutableFloatStateOf(0f)
        private set

    var score by mutableStateOf(0)
        private set

    var statusMessage by mutableStateOf("")
        private set

    var serviceIconY by mutableFloatStateOf(0f)
    var serviceIconX by mutableFloatStateOf(0f)
    var serviceIconResId by mutableStateOf(R.drawable.service0)

    var isGamePaused by mutableStateOf(false)
        private set

    private val _toastMessageChannel = Channel<String>(Channel.BUFFERED)
    val toastMessageFlow = _toastMessageChannel.receiveAsFlow()

    private var currentCorrectRoleId by mutableStateOf(0)

    private var gameJob: Job? = null
    private val iconSizePx = 300f
    private val fallSpeed = 20f
    private val updateIntervalMs = 100L
    private val pauseDurationMs = 3000L

    private val services = listOf(
        R.drawable.service0 to "極早期療育，屬於嬰幼兒方面的服務",
        R.drawable.service1 to "離島服務，屬於兒童方面的服務",
        R.drawable.service2 to "極重多障，屬於成人方面的服務",
        R.drawable.service3 to "輔具服務，屬於一般民眾方面的服務"
    )

    private val roleNames = listOf("嬰兒", "兒童", "成人", "一般民眾")

    private val topYOffsetPx get() = (screenHeightPx / 2f - iconSizePx).coerceAtLeast(0f)
    private val bottomYOffsetPx get() = (screenHeightPx - iconSizePx).coerceAtLeast(0f)

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

    fun updateIconX(newX: Float) {
        val maxBound = screenWidthPx - iconSizePx
        serviceIconX = newX.coerceIn(0f, maxBound)
    }

    private fun resetIconPosition() {
        val nextServiceIndex = Random.nextInt(services.size)
        serviceIconResId = services[nextServiceIndex].first
        currentCorrectRoleId = nextServiceIndex

        serviceIconY = 0f
        serviceIconX = screenWidthPx / 2 - iconSizePx / 2
        statusMessage = ""
        isGamePaused = false
    }

    private fun pauseAndResumeGame(displayMessage: String, toastText: String, isCorrect: Boolean) {
        isGamePaused = true
        statusMessage = displayMessage

        viewModelScope.launch {
            _toastMessageChannel.send(toastText)

            if (isCorrect) {
                score++
            } else {
                score--
            }

            delay(pauseDurationMs)
            resetIconPosition()
        }
    }

    private fun checkCollision() {
        if (isGamePaused) return

        val roleAreas = listOf(
            CollisionArea(0f, topYOffsetPx, iconSizePx, topYOffsetPx + iconSizePx, 0),
            CollisionArea(screenWidthPx - iconSizePx, topYOffsetPx, screenWidthPx, topYOffsetPx + iconSizePx, 1),
            CollisionArea(0f, bottomYOffsetPx, iconSizePx, bottomYOffsetPx + iconSizePx, 2),
            CollisionArea(screenWidthPx - iconSizePx, bottomYOffsetPx, screenWidthPx, bottomYOffsetPx + iconSizePx, 3)
        )

        val serviceIconArea = CollisionArea(
            serviceIconX,
            serviceIconY,
            serviceIconX + iconSizePx,
            serviceIconY + iconSizePx,
            -1
        )

        for (role in roleAreas) {
            if (serviceIconArea.isCollidingWith(role)) {

                val isCorrect = role.roleId == currentCorrectRoleId
                val answerText = services[currentCorrectRoleId].second
                val scoreChangeText = if (isCorrect) "加1分" else "減1分"
                val collisionTargetName = roleNames[role.roleId]

                val displayMessage = "(碰撞${collisionTargetName}圖示) ($scoreChangeText)"
                val toastText = answerText

                pauseAndResumeGame(displayMessage, toastText, isCorrect)
                return
            }
        }
    }

    private fun startGameLoop() {
        gameJob?.cancel()
        gameJob = viewModelScope.launch {
            while (true) {
                delay(updateIntervalMs)

                if (!isGamePaused) {
                    serviceIconY += fallSpeed
                }

                if (serviceIconY + iconSizePx >= screenHeightPx) {
                    if (!isGamePaused) {
                        val answerText = services[currentCorrectRoleId].second
                        val displayMessage = "(掉到最下方) (減1分)"
                        val toastText = answerText

                        pauseAndResumeGame(displayMessage, toastText, false)
                    }
                } else {
                    checkCollision()
                }
            }
        }
    }
}

data class CollisionArea(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
    val roleId: Int
) {
    fun isCollidingWith(other: CollisionArea): Boolean {
        return left < other.right &&
                right > other.left &&
                top < other.bottom &&
                bottom > other.top
    }
}