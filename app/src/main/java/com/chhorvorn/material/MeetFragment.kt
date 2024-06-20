package com.chhorvorn.material

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.camera.video.Recording
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.chhorvorn.material.databinding.MeetFragmentBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

interface OnCompletedTaskFragmentListener {
    fun onCompletedTaskFragmentInteraction()
}

class MeetFragment : Fragment() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingInflatedId")

    private lateinit var snackBar: Snackbar
    private var _binding: MeetFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: itemDao
    private lateinit var item: List<TASK_ITEM>
    private var listener: OnCompletedTaskFragmentListener? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCompletedTaskFragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = MeetFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = AppDatabase.getDatabase(requireContext()).itemDao()
        loadData()
    }

    private fun onCreateDatePicker(): MaterialDatePicker<Long> {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        return datePicker.build()
    }

    private fun loadData() {
        lifecycleScope.launch(Dispatchers.IO) {
            item = db.getDataWithStatusTrue()
            val recyclerView = binding.recyclerView
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = CompletedTaskAdapter(item, object : OnClickListener {
                override fun onItemDelete(position: Int) {
                    val taskToDelete = TASK_ITEM(uid = item[position].uid, title = item[position].title, desc = item[position].desc, status = item[position].status)
                    deleteTask(taskToDelete)
                }
            })
        }
    }

    private fun deleteTask(task: TASK_ITEM) {
        lifecycleScope.launch(Dispatchers.IO) {
            val updateSuccess = db.delete(task)
            listener?.onCompletedTaskFragmentInteraction()
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
//        snackBar = Snackbar.make(view, "Meet", Snackbar.LENGTH_SHORT)
//        snackBar.setAction("Undo") { println("Undo") }
//        snackBar.show()


//        textInputSelectDate.setOnTouchListener { v, event ->
//            val DRAWABLE_RIGHT = 2
//            if (event.action == MotionEvent.ACTION_UP) {
//                if (event.rawX >= (textInputSelectDate.right - textInputSelectDate.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
//                    val datePicker = onCreateDatePicker()
//                    datePicker.addOnPositiveButtonClickListener { selection ->
//                        val date = Date(selection)
//                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//                        val formattedDate = dateFormat.format((date))
//                        textInputSelectDate.setText(formattedDate)
//                    }
//                    datePicker.show(childFragmentManager, "tag")
//                }
//                return@setOnTouchListener true
//            }
//            return@setOnTouchListener true
//        }
