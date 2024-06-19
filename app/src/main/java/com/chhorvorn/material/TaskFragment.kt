package com.chhorvorn.material

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.chhorvorn.material.databinding.TaskFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskFragment : Fragment() {
    @SuppressLint("SetTextI18n", "MissingInflatedId")
    private var _binding: TaskFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TaskFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newTaskButton = binding.extendedFab

        //onClickButtonAdd
        newTaskButton.setOnClickListener {
            showDialog()
        }

        //get data from database
        loadData()
    }

    private fun showDialog() {
        val fragmentManager = childFragmentManager
        val fullscreenDialogFragment = FullscreenFragment()
        fullscreenDialogFragment.show(fragmentManager, "fullscreen_dialog")
    }

    private fun loadData() {
        val database = AppDatabase.getDatabase(requireContext()).itemDao()
        var item: List<TASK_ITEM>
        lifecycleScope.launch(Dispatchers.IO) {
            item = database.getAll()
            val recyclerView = binding.recyclerViewMail
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = TaskAdapter(item)
        }
    }
}
