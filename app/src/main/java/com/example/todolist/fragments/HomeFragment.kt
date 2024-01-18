package com.example.todolist.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.FragmentHomeBinding
import com.example.todolist.utils.adapter.ToDoAdapter
import com.example.todolist.utils.model.ToDoData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment(), ToDoDialogFragment.DialogNextBtnClickListener,
    ToDoAdapter.ToDoAdapterClickInterface {
    private val TAG = "HomeFragment"
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding
    private var popUpFragment:ToDoDialogFragment? =null
    private lateinit var adapter:ToDoAdapter
    private lateinit var mList:MutableList<ToDoData>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    } override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        getDataFromDatabase()
        registerEvent()
    }
        private fun registerEvent(){
            binding.addTaskBtn.setOnClickListener{
                if (popUpFragment != null)
                    childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
                popUpFragment = ToDoDialogFragment()
                popUpFragment!!.setListener(this)
                popUpFragment!!.show(
                    childFragmentManager,ToDoDialogFragment.TAG
                )
            }
        }

    private fun init(view: View){
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference.child("Tasks").child(auth.currentUser?.uid.toString())

        binding.mainRecyclerView.setHasFixedSize(true)
        binding.mainRecyclerView.layoutManager= LinearLayoutManager(context)
        mList = mutableListOf()
        adapter = ToDoAdapter(mList)
        adapter.setListener(this)
        binding.mainRecyclerView.adapter= adapter
    }
    private fun getDataFromDatabase(){
        databaseRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for (taskSnapshot in snapshot.children){
                    val todoTask = taskSnapshot.key?.let {
                        ToDoData(it,taskSnapshot.value.toString())
                    }
                    if (todoTask!=null){
                        mList.add(todoTask)
                    }

                }
                Log.d(TAG, "onDataChange: " + mList)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,error.message,Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onSaveTask(todoTask: String, todoEdit: TextInputEditText) {
        databaseRef.push().setValue(todoTask).addOnCompleteListener{
            if (it.isSuccessful){
                    Toast.makeText(context,"To Do Task Saved Successfully ",Toast.LENGTH_LONG).show()
                todoEdit.text=null
            }else{
                Toast.makeText(context,it.exception?.message,Toast.LENGTH_LONG).show()
            }
            popUpFragment!!.dismiss()
        }
    }

        override fun onUpdateTask(toDoData: ToDoData, todoEdit: TextInputEditText) {
           val map = HashMap<String, Any>()
            map[toDoData.taskId] = toDoData.task
        databaseRef.updateChildren(map).addOnCompleteListener{
            if (it.isSuccessful) {
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
            todoEdit.text=null
            popUpFragment!!.dismiss()
        }
    }

    override fun onDeleteItemClicked(toDoData: ToDoData, position: Int) {
            databaseRef.child(toDoData.taskId).removeValue().addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(context,"Deleted Successful",Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(context,it.exception?.message,Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onEditItemClicked(toDoData: ToDoData, position: Int) {
        if (popUpFragment != null)
            childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()

        popUpFragment = ToDoDialogFragment.newInstance(toDoData.taskId, toDoData.task)
        popUpFragment!!.setListener(this)
    popUpFragment!!.show(childFragmentManager,ToDoDialogFragment.TAG)}


}