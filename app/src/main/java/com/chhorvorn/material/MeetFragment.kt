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
import androidx.room.Room
import com.chhorvorn.material.databinding.MeetFragmentBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MeetFragment : Fragment() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingInflatedId")
    private lateinit var buttonSubmit: Button
    private lateinit var editTextPhone: com.google.android.material.textfield.TextInputEditText
    private lateinit var textInputLayoutPhone: com.google.android.material.textfield.TextInputLayout
    private lateinit var firstName: com.google.android.material.textfield.TextInputEditText
    private lateinit var lastName: com.google.android.material.textfield.TextInputEditText
    private lateinit var city: com.google.android.material.textfield.TextInputEditText
    private lateinit var zipCode: com.google.android.material.textfield.TextInputEditText
    private lateinit var address: com.google.android.material.textfield.TextInputEditText
    private lateinit var textInputSelectDate: com.google.android.material.textfield.TextInputEditText
    private lateinit var snackBar: Snackbar
    private var _binding: MeetFragmentBinding? = null
    private val binding get() = _binding!!
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

        editTextPhone = binding.phone
        textInputLayoutPhone = binding.textInputLayoutPhone
        firstName = binding.firstName
        lastName = binding.lastName
        city = binding.city
        zipCode = binding.zipCode
        address = binding.address
        buttonSubmit = binding.submitButton
        textInputSelectDate = binding.selectDate

//        snackBar = Snackbar.make(view, "Meet", Snackbar.LENGTH_SHORT)
//        snackBar.setAction("Undo") { println("Undo") }
//        snackBar.show()

        val db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "Users"
        )
            .fallbackToDestructiveMigration()
            .build()

        val itemDao = db.itemDao()

        buttonSubmit.setOnClickListener {
            val phone = editTextPhone.text.toString()
            val first = firstName.text.toString()
            val lastName = lastName.text.toString()
            val city = city.text.toString()
            val address = address.text.toString()
            val zipCode = zipCode.text.toString()
            var isValid = true

            if (phone.isEmpty()) {
                textInputLayoutPhone.error = "Phone cannot be empty!"
                isValid = false
            } else if (phone.length < 10) {
                textInputLayoutPhone.error = "Phone must be at least 10 characters"
                isValid = false
            } else if (phone.length > 12) {
                textInputLayoutPhone.error = "Phone must be at most 12 characters"
                isValid = false
            } else {
                textInputLayoutPhone.error = null
            }

            if (isValid) {
                Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
            }
        }

        textInputSelectDate.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (textInputSelectDate.right - textInputSelectDate.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    val datePicker = onCreateDatePicker()
                    datePicker.addOnPositiveButtonClickListener { selection ->
                        val date = Date(selection)
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val formattedDate = dateFormat.format((date))
                        textInputSelectDate.setText(formattedDate)
                    }
                    datePicker.show(childFragmentManager, "tag")
                }
                return@setOnTouchListener true
            }
            return@setOnTouchListener true
        }
    }

    private fun onCreateDatePicker(): MaterialDatePicker<Long> {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        return datePicker.build()
    }
}