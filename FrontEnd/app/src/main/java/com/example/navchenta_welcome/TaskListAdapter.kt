package com.example.navchenta_welcome

import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.navchenta_welcome.databinding.ItemTaskBinding
//import com.example.todolist.databinding.ItemTaskBinding

class TaskListAdapter : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(DiffCallback()) {

    var listenerEdit: (Task) -> Unit = {}
    var listenerDelete: (Task) -> Unit = {}
    var listenerJoin: (Task) -> Unit = {}

    var sharedPreferences: SharedPreferences? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }
//    private fun showToast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(
        private val binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Task) {
            sharedPreferences = itemView.context.getSharedPreferences("MyAppPreferences", AppCompatActivity.MODE_PRIVATE)
            var flag = sharedPreferences?.getString("flag_login", "")
            binding.email1.text = item.firstemail
            binding.month.text = "${item.date} ${item.hour}"
            binding.email2.text = item.secondemail
            binding.StateshowerRight.text = item.state
            binding.districtShow.text = item.district
            binding.address.text = item.address
            binding.batchnumber.text = item.batch.toString()
            binding.title.text = item.batchn
            binding.stateshow.text = item.staten
            binding.distric.text = item.districtn
            val id_joined = sharedPreferences?.getString("session_id", "-1")?.toInt()

//            val id_joined = sharedPreferences.getString("session_id_joined", "-1")?.toInt()
            if(flag=="user"){
                binding.joinbutton.visibility = android.view.View.VISIBLE
                if(id_joined == -1){
                    binding.joinbutton.text = "Join"
                    binding.ivMore.visibility = android.view.View.GONE
                }
                else{
                    binding.joinbutton.isEnabled = false
                    binding.joinbutton.isClickable = false
                    if(id_joined==item.id) {
                        binding.joinbutton.text = "Joined"
                        binding.joinbutton.setBackgroundColor(Color.parseColor("#00FF00"))
                    }
                }
            }
            else{
                binding.joinbutton.visibility = android.view.View.GONE
                binding.ivMore.visibility = android.view.View.VISIBLE
            }
            binding.joinbutton.setOnClickListener{
                showId(item)
            }
            binding.ivMore.setOnClickListener {
                showPopup(item)
            }
        }
        private fun showId(item:Task){
            val joinbutton = binding.joinbutton
            listenerJoin(item)
        }
        private fun showPopup(item: Task) {
            val ivMore = binding.ivMore
            val popupMenu = PopupMenu(ivMore.context, ivMore)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
//                    R.id.action_edit -> listenerEdit(item)
                    R.id.action_delete -> listenerDelete(item)

//                    R.id.action_delete ->
//                    Log.i("lakshya", "This is an info message")
//                            println("lakshya")
                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }
    }
}


class DiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
    override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
}
