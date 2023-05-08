package com.thiagoneves.architecturecomponents.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.thiagoneves.architecturecomponents.R
import com.thiagoneves.architecturecomponents.data.ToDoViewModel
import com.thiagoneves.architecturecomponents.data.model.ToDoData
import com.thiagoneves.architecturecomponents.databinding.FragmentListBinding
import com.thiagoneves.architecturecomponents.fragments.SharedViewModel


class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private val mToDoViewModel : ToDoViewModel by viewModels()
    private val mSharedViewModel : SharedViewModel by viewModels()

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val listAdapter: ListAdapter by lazy { ListAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val view =  binding.root
        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel

        setupRecyclerView()

        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            mSharedViewModel.checkIfTheDatabaseIsEmpty(data)
            listAdapter.setData(data)
        })

        return view;
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = listAdapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        // Swipe to Delete
        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = listAdapter.dataList[viewHolder.adapterPosition]
                // Delete Item
                mToDoViewModel.deleteData(deletedItem)
                listAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                // Restore Deleted Item
                restoreDeletedData(viewHolder.itemView, deletedItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deletedItem: ToDoData) {
        val snackBar = Snackbar.make(
            view, "Deleted '${deletedItem.title}'",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            mToDoViewModel.insertData(deletedItem)
        }
        snackBar.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()
    }

    private fun setupMenu() {
        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.list_fragment_menu, menu)

                val search = menu.findItem(R.id.menu_search)
                val searchView = search.actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(this@ListFragment)

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                return when (menuItem.itemId) {
                    R.id.menu_priority_high -> {
                        searchHigh()
                        true
                    }
                    R.id.menu_priority_low -> {
                        Log.d("list menu ", "menu_priority_low");
                        searchLow()
                        true
                    }
                    R.id.menu_delete_all -> {
                        deleteAllItems()
                        true
                    }
                    else -> false
                }
            }
        }
            , viewLifecycleOwner, Lifecycle.State.RESUMED);
    }

    private fun searchHigh() {
        mToDoViewModel.sortByHighPriority.observe(this,
            Observer {
                listAdapter.setData(it)

            })
    }

    private fun searchLow() {
        mToDoViewModel.sortByLowPriority.observe(this,
            Observer {
                listAdapter.setData(it)
            })
    }

    private fun deleteAllItems() {
        val builder= AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(requireContext(), "Successfully all items deleted!", Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Are you do you want to delete the all items?")

        builder.create().show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%${query}%"
        mToDoViewModel.searchDatabase(searchQuery).observe(this, Observer {list ->
            list?.let {
                listAdapter.setData(list)
            }
        })
    }

}