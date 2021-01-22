package m.woong.timerapp.ui.stopwatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import m.woong.timerapp.R

class StopwatchFragment : Fragment() {

    private lateinit var stopwatchViewModel: StopwatchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        stopwatchViewModel =
            ViewModelProvider(this).get(StopwatchViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_stopwatch, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        stopwatchViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}