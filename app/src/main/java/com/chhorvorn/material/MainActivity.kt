package com.chhorvorn.material

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.chhorvorn.material.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val bottomNavigation = binding.bottomNavigation
        val badge = bottomNavigation.getOrCreateBadge(R.id.navigation_mail)
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "Users")
            .fallbackToDestructiveMigration()
            .build()
        val itemDao = db.itemDao()

        badge.isVisible = true
        badge.number = 7


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
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.host_navigation, fragment)
            .commit()
    }
}