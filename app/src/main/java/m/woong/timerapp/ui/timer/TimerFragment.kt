package m.woong.timerapp.ui.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import m.woong.timerapp.databinding.FragmentTimerBinding

class TimerFragment : Fragment() {

    private var fragmentTimerBinding: FragmentTimerBinding? = null
    private val timerViewModel: TimerViewModel by viewModels()
    private var timerJob: Job? = null
    private var isStarted = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTimerBinding.inflate(inflater, container, false)
        fragmentTimerBinding = binding
        binding.tvTimeLivedata.text = "60"
        binding.tvTimeFlow.text = "60"
        binding.btnStart.setOnClickListener {
            isStarted = if (!isStarted){
                fragmentTimerBinding?.btnStart?.text = "PAUSE"
                startTimer()
                true
            } else {
                fragmentTimerBinding?.btnStart?.text = "START"
                stopTimer()
                false
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun startTimer(){
        timerViewModel.startLiveData()
        timerViewModel.timeLiveData.observe(viewLifecycleOwner, Observer {
            // UI에 표시
            fragmentTimerBinding?.tvTimeLivedata?.text = it.toString()
        })

        timerJob = lifecycleScope.launch {
            timerViewModel.startFlow().collect {
                // UI에 표시
                fragmentTimerBinding?.tvTimeFlow?.text = it.toString()
            }
        }
    }

    private fun stopTimer(){
        timerViewModel.stopLiveData()
        timerJob?.cancel()
    }


    override fun onDestroyView() {
        fragmentTimerBinding = null
        super.onDestroyView()
    }
}
