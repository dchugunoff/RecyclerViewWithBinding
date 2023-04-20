package com.example.recyclerviewwithbinding.model

import com.github.javafaker.Faker
import java.util.*

/**
 * Слушатель UsersListener - который будет отдавать список юзеров, после применения методов(delete и move)
 */
typealias UsersListener = (users: List<User>) -> Unit

class UsersService {

    private var users = mutableListOf<User>()

    /**
     * Локальная переменная listeners типа mutableSetList, в которую будем складывать все слушатели, которые
     * будут прослушивать все слушатели в классе UserService
     */
    private val listeners = mutableSetOf<UsersListener>()

    /**
     * Инициализация рандомного списка с помощью библиотеки faker
     */
    init {
        val faker = Faker.instance()
        IMAGES.shuffle()
        users = (1..100).map { User(
            id = it.toLong(),
            name = faker.name().name(),
            company = faker.company().name(),
            photo = IMAGES[it % IMAGES.size]
        ) }.toMutableList()
    }

    /**
     * Метод getUsers() для получения списка пользователей
     */
    fun getUsers(): List<User> {
        return users
    }

    /**
     * Метод moveUser() для перемещения пользователя
     * val oldIndex - переменная со старым(текущим) значением пользователя
     * Проверка если не равен -1(списки начинаются с индекса 0) возвращает индекс
     * val newIndex - переменная для нового индекса(позиции в списке) - moveBy - аргумент для перемещения(Например 1 - вверх, -1 - вниз
     * Проверка если новый индекс не меньше 0 и новый индекс больше или равен чем users.size - возвращает
     * Collections.swap - метод библиотеки Collections, принимает значения списка, старого и нового индексов.
     */
    fun moveUser(user: User, moveBy: Int) {
        val oldIndex = users.indexOfFirst { it.id == user.id }
        if (oldIndex == -1) return
        val newIndex = oldIndex + moveBy
        if (newIndex < 0 || newIndex >= users.size) return
        Collections.swap(users, oldIndex, newIndex)
        notifyChanges()
    }

    /**
     * Метод deleteUser() для удаления выбранного юзера
     * Переменная indexToDelete - переменная для выбора пользователя, сравнивает текущий id с id юзера
     * Если инеккс не равен(списки начинаются с индекса 0), то удаляет юзера
     */
    fun deleteUser(user: User) {
        val indexToDelete = users.indexOfFirst { it.id == user.id }
        if (indexToDelete != -1) {
            users.removeAt(indexToDelete)
            notifyChanges()
        }
    }

    /**
     * addListener - метод, который принимает один аргумент - слушатель который нужно добавить(в случае remove - удалить)
     * Добавляем слушатель в наш набор listeners(в случае remove - удаляем из набора)
     */
    fun addListener(listener: UsersListener) {
        listeners.add(listener)
        listener.invoke(users)
    }

    fun removeListener(listener: UsersListener) {
        listeners.add(listener)
    }

    /**
     * notifyChanges() - метод, который позволяет не добавлять одну и ту же логику в методы addListener и removeListener
     * Проходим по всем слушателям, которые у нас добавлены, для каждого вызываем метод invoke() и
     * передаем текущий список пользователей
     */
    private fun notifyChanges() {
        listeners.forEach { it.invoke(users) }
    }



    /**
     * Компаньен обжект со списком картинок
     */
    companion object {
        private val IMAGES = mutableListOf(
            "https://images.unsplash.com/photo-1600267185393-e158a98703de?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NjQ0&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1579710039144-85d6bdffddc9?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0Njk1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1488426862026-3ee34a7d66df?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0ODE0&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1620252655460-080dbec533ca?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzQ1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1613679074971-91fc27180061?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzUz&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1485795959911-ea5ebf41b6ae?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzU4&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1545996124-0501ebae84d0?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzY1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/flagged/photo-1568225061049-70fb3006b5be?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0Nzcy&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1567186937675-a5131c8a89ea?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0ODYx&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1546456073-92b9f0a8d413?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0ODY1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800"
        )
    }
}