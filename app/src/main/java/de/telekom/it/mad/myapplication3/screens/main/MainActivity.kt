package de.telekom.it.mad.myapplication3.screens.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.telekom.it.mad.myapplication3.App
import de.telekom.it.mad.myapplication3.R
import de.telekom.it.mad.myapplication3.data.AppDataRepository
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var appDataRepo: AppDataRepository

    private lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel

    private lateinit var recyclerView: RecyclerView
    private val adapter = MainListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDependencies()
        initViewModel()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel.dataList.observe(this) {
            adapter.dataList = it
            adapter.notifyDataSetChanged()
        }
    }

    private fun injectDependencies(){
        App.appComponent.inject(this)
    }

    private fun initViewModel() {
        viewModelFactory = MainViewModelFactory(application, appDataRepo)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(MainViewModel::class.java)
    }
}