package com.thiagoneves.architecturecomponents.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.thiagoneves.architecturecomponents.R
import com.thiagoneves.architecturecomponents.data.ToDoViewModel
import com.thiagoneves.architecturecomponents.data.model.ToDoData
import com.thiagoneves.architecturecomponents.databinding.FragmentUpdateFramgentBinding
import com.thiagoneves.architecturecomponents.fragments.SharedViewModel


class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateFramgentBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UpdateFragmentArgs>()
    private val mSharedViewModel : SharedViewModel by viewModels()
    private val mToDoViewModel : ToDoViewModel by viewModels()
    private lateinit var currentItem: ToDoData;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateFramgentBinding.inflate(inflater, container, false)


        binding.args = args
        currentItem = args.currentItem;

        binding.spinner.onItemSelectedListener = mSharedViewModel.spinnerListener

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.update_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                return when (menuItem.itemId) {
                    R.id.menu_save -> {
                        updateAndSaveToDoData()
                        true
                    }
                    R.id.menu_delete -> {
                        deleteToDoData()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun updateAndSaveToDoData() {
        val title = binding.editTextTextTitle.text.toString()
        val description = binding.editTextTextDescription.text.toString()
        val priority = binding.spinner.selectedItem.toString()

        if (mSharedViewModel.isValidData(title, description)) {
            val updatedData = ToDoData(currentItem.id, title, mSharedViewModel.parsePriority(priority), description)
            mToDoViewModel.updateData(updatedData)
            Toast.makeText(requireContext(), "Successfully updated!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateFramgent_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Insert the correct data to update", Toast.LENGTH_LONG).show()
        }

    }

    private fun deleteToDoData() {
        val builder= AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mToDoViewModel.deleteData(currentItem)
            Toast.makeText(requireContext(), "Successfully deleted!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateFramgent_to_listFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Are you do you want to delete the ${currentItem.title}?")

        builder.create().show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}