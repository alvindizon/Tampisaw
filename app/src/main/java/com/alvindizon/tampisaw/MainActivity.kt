package com.alvindizon.tampisaw

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.alvindizon.tampisaw.core.ui.BaseActivity
import com.alvindizon.tampisaw.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
    }

    private fun setupNavigation() {
        // Since we're using FragmentContainerView, use findFragmentById instead of findNavController(R.id.nav_host_fragment_txn)
        // See https://stackoverflow.com/a/59275182/4612653
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_txn) as NavHostFragment? ?: return

        val navController = host.navController
        binding.bottomNavView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, dest, _ ->
            binding.bottomNavView.isVisible = dest.id != R.id.details_dest
        }
    }
}
