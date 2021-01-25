package m.woong.timerapp.ui.timer

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TimerViewModel : ViewModel() {

    private val _timeLiveData: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val timeLiveData: LiveData<Int> = _timeLiveData

    var countDownTimer = object : CountDownTimer(60000, 1000) {
        override fun onTick(p0: Long) {
            _timeLiveData.value = (p0 / 1000).toInt()
        }

        override fun onFinish() {
            _timeLiveData.value = 60
        }
    }

    // START
    fun startLiveData(){
        countDownTimer.start()
    }

    fun startFlow(time: Int): Flow<Int> = flow {
        for (i in time downTo 0) {
            emit(i)
            Log.d("Flow", "emit:$i")
            delay(1000)
        }
    }

    // PAUSE
    fun stopLiveData(){
        countDownTimer.cancel()
    }


}