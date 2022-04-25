package com.example.navigationdrawer.ui.CalendarioSemanal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.navigationdrawer.databinding.FragmentSemanalBinding

public class SemanalFragment : Fragment() {
    private var _binding: FragmentSemanalBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val semanalViewModel =
            ViewModelProvider(this).get(SemanalViewModel::class.java)

        _binding = FragmentSemanalBinding.inflate(inflater, container, false)
        val root: View = binding.root

       /* val textView: TextView = binding.textSemanal
        semanalViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        */

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
