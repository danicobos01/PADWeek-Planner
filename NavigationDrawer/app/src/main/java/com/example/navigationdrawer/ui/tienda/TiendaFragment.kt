package com.example.navigationdrawer.ui.tienda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.navigationdrawer.R
import com.example.navigationdrawer.databinding.FragmentTiendaBinding
import com.example.navigationdrawer.ui.Temporizador.TempViewModel

class TiendaFragment : Fragment() {
    private var binding: FragmentTiendaBinding? = null
    private val rv: RecyclerView? = null
    private val recArr: ArrayList<RecompensaInfo>? = null
    private val adapter: RecompensasAdapter? = null
    private val dbAdapter: DatabaseOrg? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_tienda, container, false)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }



}