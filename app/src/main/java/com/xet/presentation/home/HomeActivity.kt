package com.xet.presentation.home

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.color.MaterialColors
import com.xet.R
import com.xet.databinding.ActivityHomeBinding
import com.xet.presentation.ServiceLocator
import com.xet.presentation.friends.ContactsFragment
import com.xet.presentation.profile.ProfileFragment
import com.xet.presentation.search.SearchFragment

class HomeActivity(
    private var viewModel: HomeViewModel = ServiceLocator.getHomeViewModel()
) : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navigationBar = binding.homeNavigationBar
        val userId = viewModel.loadUser()

        if (savedInstanceState == null) {
            setCurrentFragment(ContactsFragment.newInstance(userId))
        }

        navigationBar.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.menu_chat -> setCurrentFragment(ContactsFragment.newInstance(userId))
                R.id.menu_search -> setCurrentFragment(SearchFragment.newInstance(userId))
                R.id.menu_profile -> setCurrentFragment(ProfileFragment.newInstance(userId))
            }
            true
        }

        val color = MaterialColors.getColor(this, com.google.android.material.R.attr.colorSecondaryVariant, Color.BLACK)
        window.navigationBarColor = color
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.home_frame_layout, fragment)
            commit()
        }
}