package com.thiagoneves.architecturecomponents.fragments

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.thiagoneves.architecturecomponents.R
import com.thiagoneves.architecturecomponents.data.model.Priority
import com.thiagoneves.architecturecomponents.data.model.ToDoData

//TODO it's can become a util class
class SharedViewModel (application: Application) : AndroidViewModel(application) {

    val emptyDatabase = MutableLiveData<Boolean>(false);

    fun checkIfTheDatabaseIsEmpty(toDoData : List<ToDoData>) {
        emptyDatabase.value = toDoData.isEmpty();
    }
    val spinnerListener: AdapterView.OnItemSelectedListener = object :  AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long) {
            when (position) {
                 0 -> {
                     (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application,
                     R.color.red))
                 }
                1 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application,
                        R.color.yellow))
                }
                2 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application,
                        R.color.green))
                }
            }

        }

        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

    }

    fun isValidData(title: String, description: String) : Boolean {
        return if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            false
        } else title.isNotEmpty() || description.isNotEmpty()
    }

    fun parsePriority(priority: String): Priority {
        return when(priority){
            "High Priority" -> { Priority.HIGH }
            "Medium Priority" -> { Priority.MEDIUM }
            "Low Priority" -> { Priority.LOW }
            else -> Priority.LOW
        }
    }
}