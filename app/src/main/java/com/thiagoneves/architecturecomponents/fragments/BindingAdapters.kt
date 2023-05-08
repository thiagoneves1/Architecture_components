package com.thiagoneves.architecturecomponents.fragments

import android.view.View
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.thiagoneves.architecturecomponents.R
import com.thiagoneves.architecturecomponents.data.model.Priority
import com.thiagoneves.architecturecomponents.data.model.ToDoData
import com.thiagoneves.architecturecomponents.fragments.list.ListFragmentDirections

class BindingAdapters {


    companion object {

        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view: FloatingActionButton, navigate: Boolean) {
            view.setOnClickListener {
                if (navigate) {
                    view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                }
            }
        }

        @BindingAdapter("android:emptyDataBase")
        @JvmStatic
        fun emptyDataBase(view: View, emptyDataBase: MutableLiveData<Boolean>) {
            when(emptyDataBase.value) {
                true -> view.visibility = View.VISIBLE
                else -> view.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("android:parsePriorityToInt")
        @JvmStatic
        fun parsePriorityToInt(view: Spinner, priority: Priority) {
            view.setSelection(priority.ordinal)
        }

        @BindingAdapter("android:parsePriorityColor")
        @JvmStatic
        fun parsePriorityColor(cardView: CardView, priority: Priority) {
            when(priority) {
                Priority.HIGH -> (cardView.setCardBackgroundColor(cardView.context.getColor(R.color.red)))
                Priority.MEDIUM -> (cardView.setCardBackgroundColor(cardView.context.getColor(R.color.yellow)))
                else -> (cardView.setCardBackgroundColor(cardView.context.getColor(R.color.green)))
            }
        }

        @BindingAdapter("android:sendDataToUpdateFragment")
        @JvmStatic
        fun sendDataToUpdateFragment(constraintLayout: ConstraintLayout, toDoData: ToDoData) {
            constraintLayout.setOnClickListener {
                    val action = ListFragmentDirections.actionListFragmentToUpdateFramgent(toDoData)
                    constraintLayout.findNavController().navigate(action)
                }
            }
    }
}