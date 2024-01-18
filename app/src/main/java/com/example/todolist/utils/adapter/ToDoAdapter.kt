package com.example.todolist.utils.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.EachTodoItemBinding
import com.example.todolist.fragments.ToDoDialogFragment.Companion.TAG
import com.example.todolist.utils.model.ToDoData

class ToDoAdapter(private val list: MutableList<ToDoData>):RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    inner class ToDoViewHolder(val binding: EachTodoItemBinding):RecyclerView.ViewHolder(binding.root)
    private var listener : ToDoAdapterClickInterface? = null
    fun setListener(listener: ToDoAdapterClickInterface){
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = EachTodoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ToDoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
with(holder){
    with(list[position]){
        binding.todoTask.text= this.task
        Log.d(TAG, "onBindViewHolder: "+this)
        binding.deleteTask.setOnClickListener{
        listener?.onDeleteItemClicked(this,position)
        }
        binding.editTask.setOnClickListener{
        listener?.onEditItemClicked(this,position)
        }


    }
}



}
    interface ToDoAdapterClickInterface{
        fun onDeleteItemClicked(toDoData: ToDoData, position : Int)
        fun onEditItemClicked(toDoData: ToDoData, position: Int)}

}