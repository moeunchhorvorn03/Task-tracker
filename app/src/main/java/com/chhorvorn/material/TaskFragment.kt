package com.chhorvorn.material

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chhorvorn.material.databinding.TaskFragmentBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskFragment : Fragment() {
    @SuppressLint("SetTextI18n", "MissingInflatedId")
    private var _binding: TaskFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : TaskAdapter
    private lateinit var database: itemDao
    private lateinit var item: List<TASK_ITEM>
    private lateinit var toolbar: Toolbar
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
        _binding = TaskFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newTaskButton = binding.extendedFab
        val scrollview = binding.scrollView
        toolbar = binding.toolbar
        var lastScrollY = 0

        database = AppDatabase.getDatabase(requireContext()).itemDao()

        loadData()

        scrollview.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            println("scrollY $scrollY, lastScrollY $lastScrollY")
            val scrollDelta = scrollY - lastScrollY
            lastScrollY = scrollY

            if (scrollDelta > 0 && newTaskButton.isExtended) {
                newTaskButton.shrink()
            } else if (scrollDelta < 0 && !newTaskButton.isExtended) {
                newTaskButton.extend()
            }
        }

        //onClickButtonAdd
        newTaskButton.setOnClickListener {
            showDialog()
        }

    }

    private fun showDialog() {
        val fragmentManager = childFragmentManager
        val fullscreenDialogFragment = FullscreenFragment()
        fullscreenDialogFragment.show(fragmentManager, "fullscreen_dialog")
    }

    private fun loadData() {
        lifecycleScope.launch(Dispatchers.IO) {
            item = database.getAll()
            val recyclerView = binding.recyclerViewMail
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = TaskAdapter(item, object : TaskAdapter.TaskInterface {
                override fun onItemClick(position: Int) {
                    println(position)
                }

                override fun onItemDelete(position: Int) {
                    val taskToDelete = TASK_ITEM(uid = item[position].uid,title = item[position].title,desc = item[position].desc,status = item[position].status)
                    deleteTask(taskToDelete)
                }

                override fun onItemDone(position: Int) {
                    val taskToBeCompleted = TASK_ITEM(uid = item[position].uid,title = item[position].title,desc = item[position].desc, status = true)
                    completeTask(taskToBeCompleted)
                }
            })
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun deleteTask(task: TASK_ITEM) {
        lifecycleScope.launch(Dispatchers.IO) {
            val updateSuccess = database.delete(task)
            listener?.onFragmentInteraction()
        }
    }

    private fun completeTask(task: TASK_ITEM) {
        lifecycleScope.launch(Dispatchers.IO) {
            val update = database.update(task)
            listener?.onFragmentInteraction()
        }
    }
}
