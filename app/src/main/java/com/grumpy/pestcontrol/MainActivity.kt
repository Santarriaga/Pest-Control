package com.grumpy.pestcontrol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseUser
import com.grumpy.pestcontrol.auth.AuthViewModel
import com.grumpy.pestcontrol.auth.AuthViewModelFactory
import com.grumpy.pestcontrol.databinding.ActivityMainBinding
import com.grumpy.pestcontrol.views.AuthActivity

class MainActivity : AppCompatActivity() {

    //binding
    private lateinit var binding: ActivityMainBinding

    private lateinit var navController : NavController

    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel = ViewModelProvider(this, AuthViewModelFactory()).get(AuthViewModel::class.java)

        //setup binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get current user UID and pass it to fragments
//        val currentUser = viewModel.getUserData().value
//        if(currentUser != null){
//            //Log.d("currentUser", currentUser.uid)
//        }

        viewModel.getLoggedStatus().observe(this, Observer<Boolean>{ loggedOff ->
            if(loggedOff){
                val intent = Intent(this,AuthActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        //setup top bar
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.logoutBtn ->{
                    viewModel.signOut()
                    true
                }
                else ->{
                    false
                }
            }
        }


        //setup navigation drawer
        binding.topAppBar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            binding.drawerLayout.closeDrawer(GravityCompat.END)
            true
        }

        //setup up Nav component with drawer
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.navigationView.setupWithNavController(navController)

    }


}