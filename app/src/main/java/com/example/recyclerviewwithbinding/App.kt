package com.example.recyclerviewwithbinding

import android.app.Application
import com.example.recyclerviewwithbinding.model.UsersService

/**
 * Класс для UsersService чтобы сделать его singleton
 */

class App: Application() {
    val usersService = UsersService()
    /**
     * Реализация синглтона
     * Экземпляр класса UsersService()
     */
}