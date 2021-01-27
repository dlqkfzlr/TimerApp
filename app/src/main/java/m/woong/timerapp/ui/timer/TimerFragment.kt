package m.woong.timerapp.ui.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import m.woong.timerapp.R
import m.woong.timerapp.SharedMainViewModel
import m.woong.timerapp.databinding.FragmentTimerBinding
import m.woong.timerapp.util.TimerStatus
import m.woong.timerapp.util.TimerStatus.*

class TimerFragment : Fragment() {

    private lateinit var binding: FragmentTimerBinding
    private val viewModel: SharedMainViewModel by activityViewModels()
    private var timerJob: Job? = null
    private var mStatus: TimerStatus = DEFAULT

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimerBinding.inflate(inflater, container, false)
        binding.tvType.text = if (USING_FLOW) "FLOW" else "LIVEDATA"
        binding.tvTime.text = "60"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnStart.setOnClickListener {
            startTimer()
        }

        binding.btnCancel.setOnClickListener {
            stopTimer()
        }

        viewModel.timeLiveData.observe(viewLifecycleOwner, Observer {
            // UI에 표시
            binding.tvTime.text = it.toString()
        })
    }

    private fun startTimer() {
        if (USING_FLOW) {
            when(mStatus){
                DEFAULT -> {
                    mStatus = STARTED
                    timerJob = viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.startFlow().collect {
                            // UI에 표시
                            binding.tvTime.text = it.toString()
                        }
                    }
                }
                STARTED -> { // 일시정지
                    mStatus = PAUSED


                }
                PAUSED -> {  // 재개
                    mStatus = STARTED

                }
            }
        } else {
            when(mStatus){
                DEFAULT -> {
                    mStatus = STARTED
                    viewModel.startLiveData()
                }
                STARTED -> { // 일시정지
                    mStatus = PAUSED


                }
                PAUSED -> {  // 재개
                    mStatus = STARTED

                }
            }
        }
        changeBtnText(mStatus)
    }

    private fun stopTimer() {
        mStatus = DEFAULT
        changeBtnText(mStatus)
        binding.tvTime.text = getString(R.string.timer_60)
        if (USING_FLOW) {
            timerJob?.cancel()
        } else{
            viewModel.stopLiveData()
        }
    }

    private fun changeBtnText(status: TimerStatus) {
        binding.btnStart.text = when (status) {
            DEFAULT -> "START"
            STARTED -> "PAUSE"
            PAUSED -> "RESUME"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        const val USING_FLOW = false
    }
}


