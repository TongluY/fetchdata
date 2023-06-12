package com.example.fetchdata

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONArray


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
    }

    private fun fetchData() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://fetch-hiring.s3.amazonaws.com/hiring.json"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                parseData(response)
            },
            { error ->
                error.printStackTrace()
            }
        )
        queue.add(jsonArrayRequest)
    }

    private fun parseData(response: JSONArray) {
        val gson = Gson()
        val items = gson.fromJson(response.toString(), Array<Item>::class.java)
        val filteredItems = items.filter { !it.name.isNullOrBlank() }

        val sortedItems = filteredItems.sortedWith(compareBy({ it.listId }, { extractNumber(it.name!!) }))

        val groupedItems = sortedItems.groupBy { it.listId }

        val itemList: MutableList<Item> = mutableListOf()

        groupedItems.forEach { (_, items) ->
            itemList.addAll(items)
        }

        val adapter = ItemAdapter(itemList)
        recyclerView.adapter = adapter

        val listIds = groupedItems.keys.toList()
        val listIdDropdown = findViewById<Spinner>(R.id.listIdDropdown)
        val dropdownItems = listIds.map { "ListId $it" }
        val dropdownAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dropdownItems)
        listIdDropdown.adapter = dropdownAdapter

        listIdDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedListId = listIds[position]
                val filteredItems = groupedItems[selectedListId] ?: emptyList()
                adapter.updateItems(filteredItems)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                adapter.updateItems(emptyList())
            }
        }
    }

    private fun extractNumber(name: String): Int {
        val regex = "\\d+".toRegex()
        val matchResult = regex.find(name)
        return matchResult?.value?.toIntOrNull() ?: 0
    }

}