package com.example.galgeleg3

import MyRecyclerViewAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class WordListActivity : AppCompatActivity(), MyRecyclerViewAdapter.ItemClickListener {
    var adapter: MyRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        //data to populate each row with
        val wordNames: ArrayList<String> = ArrayList()
        wordNames.add("mand")
        wordNames.add("test")
        wordNames.add("fri")
        wordNames.add("øv")
        wordNames.add("luskebuks")
        wordNames.add("tester")
        wordNames.add("flot")
        wordNames.add("android")
        wordNames.add("prøveperiode")
        wordNames.add("videospil")
        wordNames.add("fængsel")
        wordNames.add("forstærker")
        wordNames.add("lol")
        wordNames.add("ballerup")
        wordNames.add("implicit")
        wordNames.add("konfetti")
        wordNames.add("ad")
        wordNames.add("imponerende")

        val difficulty: ArrayList<String> = ArrayList()
        for (i in wordNames) {
            difficulty.add("Bogstaver: " + i.length)
        }

        // set up the RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rvWords)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        adapter = MyRecyclerViewAdapter(this, wordNames, difficulty)
        adapter!!.setClickListener(this)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(view: View?, position: Int) {
        val listWord: ArrayList<String> = ArrayList()
        listWord.add(adapter!!.getItem(position))

        val i = Intent(this, PlayActivity::class.java)
        i.putExtra("listWord", listWord)
        startActivity(i)
    }

    //sends us to main menu when game is lost/won
    override fun onPause() {
        super.onPause()
        finish()
    }
}