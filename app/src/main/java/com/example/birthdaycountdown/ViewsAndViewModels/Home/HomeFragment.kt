package com.example.birthdaycountdown.ViewsAndViewModels.Home

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import com.example.birthdaycountdown.R
import com.example.birthdaycountdown.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Response.error

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    private lateinit var root: View

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        root = binding.root
        setHasOptionsMenu(true)

        val recyclerView = binding.list
        val loading = binding.loading

        recyclerView.focusable = View.NOT_FOCUSABLE
        recyclerView.layoutManager = GridLayoutManager(context, 1)
        recyclerView.adapter = BirthdayListAdapter(listOf())

        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        lifecycle.addObserver(homeViewModel)
        homeViewModel.birthdays.observe(viewLifecycleOwner) { birthdays ->
            recyclerView.adapter = BirthdayListAdapter(birthdays)
        }
        homeViewModel.loading.observe(viewLifecycleOwner) {
            loading.visibility = if (it) View.VISIBLE else View.GONE
        }
        homeViewModel.showError.observe(viewLifecycleOwner) {
            if (it) {
                Snackbar.make(root, "Unable to get birthday list", Snackbar.LENGTH_LONG).show()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, root.findNavController()) || super.onOptionsItemSelected(item)
    }
}