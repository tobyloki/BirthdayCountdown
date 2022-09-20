package com.example.birthdaycountdown.ViewsAndViewModels.BirthdayCountdown

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.birthdaycountdown.R
import com.example.birthdaycountdown.databinding.FragmentBirthdayCountdownBinding
import java.util.*

class BirthdayCountdownFragment : Fragment() {

    private var _binding: FragmentBirthdayCountdownBinding? = null
    private val binding get() = _binding!!
    private lateinit var root: View
    private lateinit var viewModel: BirthdayCountdownViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBirthdayCountdownBinding.inflate(inflater, container, false)
        root = binding.root
        setHasOptionsMenu(true)

        val monthsTxt = binding.monthsTxt
        val daysTxt = binding.daysTxt
        val hoursTxt = binding.hoursTxt
        val minutesTxt = binding.minutesTxt
        val secondsTxt = binding.secondsTxt
        val nameTxt = binding.nameTxt
        val birthDateTxt = binding.birthDateTxt

        viewModel = ViewModelProvider(this).get(BirthdayCountdownViewModel::class.java)
        lifecycle.addObserver(viewModel)
        viewModel.months.observe(viewLifecycleOwner) { value ->
            monthsTxt.text = String.format("%02d", value)
            setTimeTxtColor(monthsTxt, value)
        }
        viewModel.days.observe(viewLifecycleOwner) { value ->
            daysTxt.text = String.format("%02d", value)
            setTimeTxtColor(daysTxt, value)
        }
        viewModel.hours.observe(viewLifecycleOwner) { value ->
            hoursTxt.text = String.format("%02d", value)
            setTimeTxtColor(hoursTxt, value)
        }
        viewModel.minutes.observe(viewLifecycleOwner) { value ->
            minutesTxt.text = String.format("%02d", value)
            setTimeTxtColor(minutesTxt, value)
        }
        viewModel.seconds.observe(viewLifecycleOwner) { value ->
            secondsTxt.text = String.format("%02d", value)
            setTimeTxtColor(secondsTxt, value)
        }
        viewModel.nameTxt.observe(viewLifecycleOwner) {
            nameTxt.text = "Until $it's birthday"
        }
        viewModel.birthDateTxt.observe(viewLifecycleOwner) { value ->
            birthDateTxt.text = value
        }
        viewModel.navigateToNextPage.observe(viewLifecycleOwner) { value ->
            if (value) {
                root.findNavController().navigate(R.id.action_nav_gallery_to_nav_slideshow)
            }
        }

        return root
    }

    private fun setTimeTxtColor(txt: TextView, value: Int) {
        // set color to black
        txt.setTextColor(Color.BLACK)

        // set color to red
        if(value == 0) {
            if(txt.id == R.id.monthsTxt) {
                txt.setTextColor(Color.RED)
            }
            if(viewModel.months.value == 0) {
                if(txt.id == R.id.daysTxt) {
                    txt.setTextColor(Color.RED)
                }
                if(viewModel.days.value == 0) {
                    if(txt.id == R.id.hoursTxt) {
                        txt.setTextColor(Color.RED)
                    }
                    if(viewModel.hours.value == 0) {
                        if(txt.id == R.id.minutesTxt) {
                            txt.setTextColor(Color.RED)
                        }
                        if(viewModel.minutes.value == 0) {
                            if(txt.id == R.id.secondsTxt) {
                                txt.setTextColor(Color.RED)
                            }
                        }
                    }
                }
            }
        }
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