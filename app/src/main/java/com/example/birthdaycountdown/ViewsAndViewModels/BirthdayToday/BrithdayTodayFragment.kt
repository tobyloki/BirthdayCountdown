package com.example.birthdaycountdown.ViewsAndViewModels.BirthdayToday

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.birthdaycountdown.R
import com.example.birthdaycountdown.Shared.Utility.KonfettiPresets
import com.example.birthdaycountdown.databinding.FragmentBirthdayTodayBinding
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit

class BrithdayTodayFragment : Fragment() {

    private var _binding: FragmentBirthdayTodayBinding? = null
    private val binding get() = _binding!!
    private lateinit var root: View

    private lateinit var viewKonfetti: KonfettiView

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBirthdayTodayBinding.inflate(inflater, container, false)
        root = binding.root
        setHasOptionsMenu(true)

        val nameTxt = binding.nameTxt
        val ageTxt = binding.ageTxt
        val birthDateTxt = binding.birthDateTxt
        viewKonfetti = binding.konfettiView

        // start confetti
        viewKonfetti.start(KonfettiPresets.explode())

        // create continuous confetti
        Thread {
            Thread.sleep(2000)
            while(true) {
                viewKonfetti.stopGracefully()
                viewKonfetti.start(KonfettiPresets.rain())
                Thread.sleep(5000)
            }
        }.start()

        val viewModel = ViewModelProvider(this).get(BirthdayTodayViewModel::class.java)
        lifecycle.addObserver(viewModel)
        viewModel.nameTxt.observe(viewLifecycleOwner) {
            nameTxt.text = "It's $it's birthday today!!!"
        }
        viewModel.ageTxt.observe(viewLifecycleOwner) {
            ageTxt.text = it.toString()
        }
        viewModel.birthDateTxt.observe(viewLifecycleOwner) {
            birthDateTxt.text = it
        }

        return root
    }

    override fun onPause() {
        super.onPause()
        viewKonfetti.stopGracefully()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_birthday_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, root.findNavController()) || super.onOptionsItemSelected(item)
    }
}