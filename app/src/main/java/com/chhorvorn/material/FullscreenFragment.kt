package com.chhorvorn.material

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.chhorvorn.material.databinding.FragmentFullscreenBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface OnFragmentListener {
    fun onFragmentInteraction()
}
class FullscreenFragment : DialogFragment() {

    private var _binding: FragmentFullscreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskDao: itemDao
    private var listener: OnFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFullscreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CommitTransaction")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val database = AppDatabase.getDatabase(requireContext())
        val closeDialogButton = binding.closeDialog
        val saveButton = binding.saveButton
        val taskTitle = binding.taskTitle
        val taskDescription = binding.taskDescription
        val taskTitleLayout = binding.taskTitleLayout
        val taskDescriptionLayout = binding.taskDescriptionLayout

        taskDao = database.itemDao()

        //onSave
        saveButton.setOnClickListener {
            var isValid: Boolean = true
            if (taskTitle.text.toString().isEmpty()) {
                isValid = false
                taskTitleLayout.error = "Title is empty!"
            } else {
                isValid = true
                taskTitleLayout.error = null
            }

            if (isValid) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val newTaskItem =
                        TASK_ITEM(
                            uid = 0,
                            title = taskTitle.text.toString(),
                            desc = taskDescription.text.toString(),
                            status = false
                        )
                    val insertSuccess: Unit = taskDao.insert(newTaskItem)
                    listener?.onFragmentInteraction()
                    dismiss()
                }

            }
        }

        //onCloseDialog
        closeDialogButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}