package com.sultonbek1547.restapiwithmvvm

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
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

class MainActivity : AppCompatActivity(), RvAdapter.RvClick {

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

    override fun menuClick(imageView: ImageView, myToDo: MyToDo, position: Int) {
        val popupMenu = PopupMenu(this, imageView)
        popupMenu.inflate(R.menu.pop_ip_menu)

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.btn_edit -> {
                    editToDo(myToDo, position)
                }
                R.id.btn_delete -> {
                    deleteToDo(myToDo, position)
                }

            }

            true
        }

        popupMenu.show()
    }

    private fun editToDo(myToDo: MyToDo, position: Int) {
        val dialog = AlertDialog.Builder(this).create()
        val itemDialogBinding = ItemDialogBinding.inflate(layoutInflater)

        itemDialogBinding.apply {
            edtName.setText(myToDo.sarlavha)
            edtAbout.setText(myToDo.matn)
            edtDeadline.setText(myToDo.oxirgi_muddat)
            when (myToDo.holat) {
                "Yangi" -> spinnerState.setSelection(0)
                "Bajarilmoqda" -> spinnerState.setSelection(1)
                "Tugatildi" -> spinnerState.setSelection(2)
            }

            btnSave.setOnClickListener {
                val myToDoRequest = MyToDoRequest(
                    sarlavha = edtName.text.toString().trim(),
                    matn = edtAbout.text.toString().trim(),
                    oxirgi_muddat = edtDeadline.text.toString().trim(),
                    holat = spinnerState.selectedItem.toString()
                )

                toDoViewModel.updateMyToDo(myToDo.id, myToDoRequest).observe(this@MainActivity) {
                    when (it.status) {
                        Status.LOADING -> {
                            progressBar.visibility = View.VISIBLE
                            btnSave.isEnabled = false
                        }
                        Status.SUCCESS -> {
                            progressBar.visibility = View.GONE
                            rvAdapter.list[position] = it.data!!
                            rvAdapter.notifyItemChanged(position)
                            Toast.makeText(this@MainActivity, "Saqlandi", Toast.LENGTH_SHORT).show()
                            dialog.cancel()
                        }
                        Status.ERROR -> {
                            btnSave.isEnabled = true
                            progressBar.visibility = View.GONE
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

        dialog.setView(itemDialogBinding.root)
        dialog.show()
    }

    private fun deleteToDo(myToDo: MyToDo, position: Int) {
        toDoViewModel.deleteToDo(myToDo.id).observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    Log.e("TAG", "deleteToDo: loading delete")
                }
                Status.SUCCESS -> {
                    Toast.makeText(this, "Deleted: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                    rvAdapter.list.removeAt(position)
                    rvAdapter.notifyItemRemoved(position)
                }
                Status.ERROR -> {
                    Toast.makeText(this, "Xatolik: ${it.message}", Toast.LENGTH_LONG).show()
                 }

            }
        }
    }

}