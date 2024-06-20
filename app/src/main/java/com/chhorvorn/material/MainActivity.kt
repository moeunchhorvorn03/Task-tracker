package com.chhorvorn.material

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.chhorvorn.material.databinding.ActivityMainBinding
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), OnFragmentListener, OnCompletedTaskFragmentListener {
    lateinit var binding: ActivityMainBinding
    lateinit var bottomNavigation: BottomNavigationView
    lateinit var badge: BadgeDrawable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        bottomNavigation = binding.bottomNavigation
        badge = bottomNavigation.getOrCreateBadge(R.id.navigation_mail)

        badge.isVisible = true

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_mail -> {
                    loadFragment(TaskFragment())
                    true
                }
                R.id.navigation_meet -> {
                    loadFragment(MeetFragment())
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            bottomNavigation.selectedItemId = R.id.navigation_mail
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val database = AppDatabase.getDatabase(this).itemDao()
        var item: List<TASK_ITEM>
        lifecycleScope.launch(Dispatchers.IO) {
            item = database.getAll()
            badge.number = item.size
            badge.isVisible = badge.number != 0
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.host_navigation, fragment)
            .commit()
    }

    override fun onFragmentInteraction() {
        loadFragment(TaskFragment())
    }

    override fun onCompletedTaskFragmentInteraction() {
        loadFragment(MeetFragment())
    }
}