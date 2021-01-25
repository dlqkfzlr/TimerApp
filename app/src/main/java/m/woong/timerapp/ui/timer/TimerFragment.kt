package m.woong.timerapp.ui.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import m.woong.timerapp.R
import m.woong.timerapp.databinding.FragmentTimerBinding

class TimerFragment : Fragment() {

    private lateinit var binding: FragmentTimerBinding
    private val timerViewModel: TimerViewModel by viewModels()
    private var timerJob: Job? = null
    private var isStarted = false

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
            isStarted = if (!isStarted) {
                binding.btnStart.text = "PAUSE"
                startTimer1(60)
                true
            } else {

                false
            }
        }

        binding.btnCancel.setOnClickListener {
            stopTimer1()
        }
    }


    private fun startTimer1(time: Int) {
        if (USING_FLOW) {
            timerJob = lifecycleScope.launch {
                timerViewModel.startFlow(time).collect {
                    // UI에 표시
                    binding.tvTime.text = it.toString()
                }
            }
        } else {
            timerViewModel.startLiveData()
            timerViewModel.timeLiveData.observe(viewLifecycleOwner, Observer {
                // UI에 표시
                binding.tvTime.text = it.toString()
            })
        }
    }

    private fun stopTimer1() {
        isStarted = false
        changeBtnText(isStarted)
        binding.tvTime.text = getString(R.string.timer_60)
        if (USING_FLOW) {
            timerJob?.cancel()
        } else{
            timerViewModel.stopLiveData()
        }
    }

    private fun changeBtnText(isStarted: Boolean) {
        binding.btnStart.text = if (isStarted) "PAUSE" else "START"
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        const val USING_FLOW = true
    }
}
