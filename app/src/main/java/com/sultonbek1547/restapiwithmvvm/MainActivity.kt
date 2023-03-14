package com.sultonbek1547.restapiwithmvvm

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sultonbek1547.restapiwithmvvm.adapters.RvAdapter
import com.sultonbek1547.restapiwithmvvm.databinding.ActivityMainBinding
import com.sultonbek1547.restapiwithmvvm.databinding.ItemDialogBinding
import com.sultonbek1547.restapiwithmvvm.model.MyToDo
import com.sultonbek1547.restapiwithmvvm.model.MyToDoRequest
import com.sultonbek1547.restapiwithmvvm.repository.ToDoRepository
import com.sultonbek1547.restapiwithmvvm.retrofit.ApiClient
import com.sultonbek1547.restapiwithmvvm.utils.Status
import com.sultonbek1547.restapiwithmvvm.viewmodel.MyViewModelFactory
import com.sultonbek1547.restapiwithmvvm.viewmodel.ToDoViewModel

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var toDoViewModel: ToDoViewModel
    private lateinit var rvAdapter: RvAdapter
    private lateinit var toDoRepository: ToDoRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        toDoRepository = ToDoRepository(ApiClient.getApiService())
        toDoViewModel = ViewModelProvider(
            this,
            MyViewModelFactory(toDoRepository)
        ).get(ToDoViewModel::class.java)
        rvAdapter = RvAdapter(this)
        binding.myRv.adapter = rvAdapter

        toDoViewModel.getAllToDo().observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    Log.e("TAG", "onCreate: loading ")
                }
                Status.ERROR -> {
                    Log.e("TAG", "onCreate: error")
                }
                Status.SUCCESS -> {
                    Log.e("TAG", "onCreate: ${it.data}")
                    rvAdapter.list = it.data!! as ArrayList<MyToDo>
                    rvAdapter.notifyDataSetChanged()
                }

            }
        }
        binding.btnAdd.setOnClickListener {
            val dialog = AlertDialog.Builder(this).create()
            val itemDialogBinding = ItemDialogBinding.inflate(layoutInflater)
            dialog.setView(itemDialogBinding.root)

            itemDialogBinding.apply {

                btnSave.setOnClickListener {
                    val myToDoRequest = MyToDoRequest(
                        spinnerState.selectedItem.toString(),
                        edtAbout.text.toString().trim(),
                        edtDeadline.text.toString().trim(),
                        edtName.text.toString().trim(),
                    )

                    toDoViewModel.addMyToDo(myToDoRequest).observe(this@MainActivity) {
                        when (it.status) {
                            Status.LOADING -> {
                                Log.e("TAGEE", "onCreate: ${rvAdapter.list.size}")
                                progressBar.visibility = View.VISIBLE
                                btnSave.isEnabled = false
                            }
                            Status.SUCCESS -> {
                                progressBar.visibility = View.GONE
                                it.data?.let { it1 ->
                                    rvAdapter.list.add(it1)
                                    rvAdapter.notifyItemInserted(rvAdapter.list.lastIndex)

                                    Toast.makeText(
                                        this@MainActivity,
                                        "${it.data.id} id bilan saqlandi",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                dialog.cancel()
                                Log.e("TAGEE", "onCreate: ${rvAdapter.list.size}")


                            }
                            Status.ERROR -> {
                                progressBar.visibility = View.GONE
                                btnSave.isEnabled = true
                                Toast.makeText(
                                    this@MainActivity,
                                    "Xatolik: ${it.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                    }
                }

            }

            dialog.show()
        }

    }
}