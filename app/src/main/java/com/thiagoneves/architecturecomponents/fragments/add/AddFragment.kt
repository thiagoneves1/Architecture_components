package com.thiagoneves.architecturecomponents.fragments.add

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.thiagoneves.architecturecomponents.R
import com.thiagoneves.architecturecomponents.data.ToDoViewModel
import com.thiagoneves.architecturecomponents.data.model.ToDoData
import com.thiagoneves.architecturecomponents.databinding.FragmentAddBinding
import com.thiagoneves.architecturecomponents.fragments.SharedViewModel


class AddFragment : Fragment() {
    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val view =  binding.root

        binding.spinner.onItemSelectedListener = mSharedViewModel.spinnerListener

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.add_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                return when (menuItem.itemId) {
                    R.id.menu_add -> {
                        Log.d("list menu " , "menu_add")
                        saveData()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun saveData() {
        val title = binding.editTextTextTitle.text.toString()
        val priority = binding.spinner.selectedItem.toString()
        val description = binding.editTextTextDescription.text.toString()

        if (mSharedViewModel.isValidData(title, description)) {
            val data = ToDoData(0, title, mSharedViewModel.parsePriority(priority), description)
            mToDoViewModel.insertData(data)
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Insert the correct data to insert", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}