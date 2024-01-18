package com.example.todolist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.todolist.databinding.FragmentToDoDialogBinding
import com.example.todolist.utils.model.ToDoData
import com.google.android.material.textfield.TextInputEditText


class ToDoDialogFragment : DialogFragment() {

private lateinit var binding: FragmentToDoDialogBinding
    private var listener : DialogNextBtnClickListener? = null
    private var toDoData: ToDoData? = null
    fun setListener(listener: DialogNextBtnClickListener){
        this.listener=listener
    }
    companion object{
        const val TAG = "ToDoDialogFragment"
        @JvmStatic
        fun newInstance(taskId: String,task:String)= ToDoDialogFragment().apply{
            arguments = Bundle().apply {
                putString("taskId",taskId)
                putString("task",task)
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentToDoDialogBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments!=null){
            toDoData = ToDoData(arguments?.getString("taskId").toString(),arguments?.getString("task").toString())
            binding.todoEt.setText(toDoData?.task)
        }

        registerEvents()
    }
private fun registerEvents(){
    binding.todoNextBtn.setOnClickListener{
        val todoTask= binding.todoEt.text.toString()
        if (todoTask.isNotEmpty()){
            if (toDoData == null){
                listener?.onSaveTask(todoTask,binding.todoEt)

            }else{
                toDoData?.task = todoTask
                listener?.onUpdateTask(toDoData!!, binding.todoEt)
            }
        }else{
            Toast.makeText(context,"Please type some Tasks",Toast.LENGTH_LONG).show()
        }
    }
    binding.todoClose.setOnClickListener{
        dismiss()
    }
}
    interface DialogNextBtnClickListener{
        fun onSaveTask(todoTask:String , todoEdit:TextInputEditText)
        fun onUpdateTask(toDoData: ToDoData, todoEdit:TextInputEditText)
    }}
