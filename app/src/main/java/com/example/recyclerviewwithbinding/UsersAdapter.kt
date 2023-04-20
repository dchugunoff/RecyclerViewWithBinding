package com.example.recyclerviewwithbinding

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recyclerviewwithbinding.databinding.ItemUserBinding
import com.example.recyclerviewwithbinding.model.User

interface UserActionListener {

    fun onUserMove(user: User, moveBy: Int)

    fun onUserDelete(user: User)

    fun onUserDetails(user: User)
}

class UsersAdapter(
    private val userActionListener: UserActionListener
) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>(), View.OnClickListener {

    /**
     * По умолчанию список юзеров будет пустым.
     * Сетер нужен для того, чтобы при каждом изменении значения users уведомить recyclerview для обновления
     * field = newValue - назначение нужного списка юзеров
     * Вызываем метод notifyDataSetChanged() для обновления
     */
    var users: List<User> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }


    /**
     * onCreateViewHolder используется, когда RecyclerView хочет создать новый элемент списка.
     * В качестве параметров нам приходит parent, откуда можно вытянуть context,
     * и viewType, когда нужно определить тип view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        /**
         * Инициализация слушателей
         * this потому что наш UserAdapter реализует View.onClickListener
         */
        binding.root.setOnClickListener(this)
        binding.moreImageViewButton.setOnClickListener(this)
        return UserViewHolder(binding)
    }

    /**
     * Адаптер должен знать сколько элементов есть в списке, по этому
     * метод getItemCount() который должен возвращать кол-во этих элементов
     */
    override fun getItemCount(): Int = users.size

    /**
     * Метод onBindViewHolder() используется для того, чтобы обновить элемент списка
     * Нам сюда приходит holder, внутри которого находятся все наши View с элемента itemUser и мы можем их обновить.
     * Так же приходит postition, по которой мы можем вытянуть элемент по позиции
     *
     * Передаем юзера в тэг
     * holder.itemView.tag = user
     * moreImageViewButton.tag = user
     */
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        with(holder.binding) {
            holder.itemView.tag = user
            moreImageViewButton.tag = user

            userNameTextView.text = user.name
            userCompanyTextView.text = user.company
            if (user.photo.isNotBlank()) {
                Glide.with(photoImageView.context)
                    .load(user.photo)
                    .circleCrop()
                    .placeholder(R.drawable.ic_user_avatar)
                    .error(R.drawable.ic_user_avatar)
                    .into(photoImageView)
            } else {
                photoImageView.setImageResource(R.drawable.ic_user_avatar)
            }
        }
    }

    class UserViewHolder(
        val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root)

    /**
     * Вытягиваем юзера из тэга
     */
    override fun onClick(v: View) {
        val user = v.tag as User
        when(v.id) {
            R.id.moreImageViewButton -> {
                showPopupMenu(v)
            } else -> {
                userActionListener.onUserDetails(user)
            }
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val context = view.context
        val user = view.tag as User
        val position = users.indexOfFirst { it.id == user.id }
        /**
         * Параметры:
         * 0 - groupId
         * ID_MOVE_UP - идентификатор из объекта компаньена
         * Menu.NONE - порядок(в нашем случае его нет - NONE)
         * "Move Up" - название
         */
        popupMenu.menu.add(0, ID_MOVE_UP, Menu.NONE, context.getString(R.string.move_up)).apply {
            isEnabled = position > 0
        }
        popupMenu.menu.add(0, ID_MOVE_DOWN, Menu.NONE, context.getString(R.string.move_down)).apply {
            isEnabled = position < users.size - 1
        }
        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, context.getString(R.string.remove))

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                ID_MOVE_UP -> {
                    userActionListener.onUserMove(user, -1)
                }
                ID_MOVE_DOWN -> {
                    userActionListener.onUserMove(user, 1)
                }
                ID_REMOVE -> {
                    userActionListener.onUserDelete(user)
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    /**
     * Идентификаторы для popupMenu
     */
    companion object {
        private const val ID_MOVE_UP = 1
        private const val ID_MOVE_DOWN = 2
        private const val ID_REMOVE = 3
    }

}