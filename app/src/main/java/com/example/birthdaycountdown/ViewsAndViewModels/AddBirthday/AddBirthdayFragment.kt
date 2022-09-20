package com.example.birthdaycountdown.ViewsAndViewModels.AddBirthday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.birthdaycountdown.databinding.FragmentAddBirthdayBinding
import com.google.android.material.snackbar.Snackbar

class AddBirthdayFragment : Fragment() {

    private var _binding: FragmentAddBirthdayBinding? = null
    private val binding get() = _binding!!
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBirthdayBinding.inflate(inflater, container, false)
        root = binding.root

        val nameTxt = binding.nameFieldLayout
        val numberPickerMonth = binding.numberPickerMonth
        val numberPickerDay = binding.numberPickerDay
        val numberPickerYear = binding.numberPickerYear
        val createBtn = binding.createBtn

        // Set the min and max values for the month
        numberPickerMonth.displayedValues = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

        val viewModel = ViewModelProvider(this).get(AddBirthdayViewModel::class.java)
        lifecycle.addObserver(viewModel)
        viewModel.maxMonth.observe(viewLifecycleOwner) { value ->
            numberPickerMonth.maxValue = value
        }
        viewModel.minMonth.observe(viewLifecycleOwner) { value ->
            numberPickerMonth.minValue = value
        }
        viewModel.month.observe(viewLifecycleOwner) { value ->
            numberPickerMonth.value = value
        }
        viewModel.maxDay.observe(viewLifecycleOwner) { value ->
            numberPickerDay.maxValue = value
        }
        viewModel.minDay.observe(viewLifecycleOwner) { value ->
            numberPickerDay.minValue = value
        }
        viewModel.day.observe(viewLifecycleOwner) { value ->
            numberPickerDay.value = value
        }
        viewModel.maxYear.observe(viewLifecycleOwner) { value ->
            numberPickerYear.maxValue = value
        }
        viewModel.minYear.observe(viewLifecycleOwner) { value ->
            numberPickerYear.minValue = value
        }
        viewModel.year.observe(viewLifecycleOwner) { value ->
            numberPickerYear.value = value
        }

        // set the min and max value for the day number picker depending on the month, and the year
        numberPickerMonth.setOnValueChangedListener { _, _, _ ->
            viewModel.onMonthChanged(numberPickerMonth.value)
        }
        // set the min and max value for the day number picker depending on the month, and the year
        numberPickerYear.setOnValueChangedListener { _, _, _ ->
            viewModel.onYearChanged(numberPickerYear.value)
        }

        createBtn.setOnClickListener() {
            val name = nameTxt.editText?.text.toString()

            // check if the name is empty
            if (name.trim().isEmpty()) {
                nameTxt.error = "Please enter a name"
                return@setOnClickListener
            } else {
                nameTxt.error = null
            }

            val month = numberPickerMonth.value
            val day = numberPickerDay.value
            val year = numberPickerYear.value
            viewModel.createBirthday(name, month, day, year) { error ->
                if (error == null) {
                    // trigger onBackPressed() to go back to the previous fragment
                    requireActivity().onBackPressed()
                } else {
                    Snackbar.make(root, error, Snackbar.LENGTH_LONG).show()
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}