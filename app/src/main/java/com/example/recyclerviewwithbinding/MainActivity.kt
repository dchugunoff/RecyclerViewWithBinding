package com.example.recyclerviewwithbinding

import android.net.wifi.p2p.WifiP2pManager.ActionListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerviewwithbinding.databinding.ActivityMainBinding
import com.example.recyclerviewwithbinding.model.User
import com.example.recyclerviewwithbinding.model.UsersListener
import com.example.recyclerviewwithbinding.model.UsersService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapter

    private val usersService: UsersService
        get() = (applicationContext as App).usersService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Передаем в конструктор адаптера объект реализованный от UserActionListener
         */
        adapter = UsersAdapter(object : UserActionListener {
            override fun onUserMove(user: User, moveBy: Int) {
                usersService.moveUser(user, moveBy)
            }

            override fun onUserDelete(user: User) {
                usersService.deleteUser(user)
            }

            override fun onUserDetails(user: User) {
                Toast.makeText(this@MainActivity, "User: ${user.name}", Toast.LENGTH_SHORT).show()
            }

        })

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        usersService.addListener(usersListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        usersService.removeListener(usersListener)
    }

    private val usersListener: UsersListener = {
        adapter.users = it
    }
}