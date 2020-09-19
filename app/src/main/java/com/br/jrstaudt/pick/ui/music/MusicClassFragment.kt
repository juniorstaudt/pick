package com.br.jrstaudt.pick.ui.music

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.br.jrstaudt.pick.R
import com.br.jrstaudt.pick.models.DBHelper
import com.br.jrstaudt.pick.models.MusicClass
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

class MusicClassFragment : AppCompatActivity() {

    private var coordinatorLayout: CoordinatorLayout? = null
    private var recyclerView: RecyclerView? = null
    private var db: DBHelper? = null
    private var ItemsList = ArrayList<MusicClass>()
    private var mAdapter: MusicClassAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_music_class_main)

        setUpComponents()

        val firebaseAnalytics = Firebase.analytics

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
            param("acessou_home", 10)
        }
    }

    private fun setUpComponents() {
        coordinatorLayout = findViewById(R.id.clListClass)
        recyclerView = findViewById(R.id.recycler_music_class)
        db = DBHelper(this)
        val btAction = findViewById<View>(R.id.btAction) as FloatingActionButton

        btAction.setOnClickListener {
            showDialog(false, null, -1)
        }

        //exibir os resultados em uma lista
        ItemsList.addAll(db!!.classItems)
        mAdapter = MusicClassAdapter(this, ItemsList)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter

        //Função de clique nos itens da lista
        recyclerView!!.addOnItemTouchListener(LongPressListener(this,
            recyclerView!!,
            object : LongPressListener.ClickListener{
                override fun onClick(view: View, position: Int) {
                }

                override fun onLongClick(view: View, position: Int) {
                    showOptionsDialog(position)
                }
            }
            ))
    }

    private fun showOptionsDialog(position: Int) {
        val options = arrayOf<CharSequence>(
            getString(R.string.editClass),
            getString(R.string.deleteClass),
            getString(R.string.deleteAllClasses))
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.options))
        builder.setItems(options) { dialog, itemIndex ->
            when(itemIndex) {
                0 -> showDialog(true, ItemsList[position], position)
                1 -> deleteClass(position)
                2 -> deleteAllClasses()
                else -> Toast.makeText(applicationContext, getString(R.string.errorToast), Toast.LENGTH_SHORT).show()
            }
        }

        builder.show()
    }

    private fun deleteClass(position: Int) {
        db!!.deleteClass(ItemsList[position])
        ItemsList.removeAt(position)
        mAdapter!!.notifyItemRemoved(position)
    }

    private fun deleteAllClasses() {
        db!!.deleteAllClasses()
        ItemsList.removeAll(ItemsList)
        mAdapter!!.notifyDataSetChanged()
    }

    private fun showDialog(isUpdate: Boolean, musicClass: MusicClass?, position: Int) {

        val layoutInflaterAndroid = LayoutInflater.from(applicationContext)
        val view = layoutInflaterAndroid.inflate(R.layout.fragment_music_class_dialog, null)

        val userInput = AlertDialog.Builder (this@MusicClassFragment)
        userInput.setView(view)

        val input = view.findViewById<EditText>(R.id.etTextClass)

        if(isUpdate && musicClass != null) {
            input.setText(musicClass!!.description)
        }

        userInput
            .setCancelable(false)
            .setPositiveButton(
                if(isUpdate) getString(R.string.updateClass)
                else getString(R.string.saveClass))
            {dialogBox, id->}
            .setNegativeButton(getString(R.string.cancelClass)) {dialogBox, id->dialogBox.cancel()}

        val alertDialog = userInput.create()
        alertDialog.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(View.OnClickListener {
            if(TextUtils.isEmpty(input.text.toString())) {
                Toast.makeText(this@MusicClassFragment,
                    getString(R.string.toast),
                    Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else {
                alertDialog.dismiss()
            }

            if(isUpdate && musicClass != null) {
                updateClass(input.text.toString(), position)
            } else {
                createMusicClass(input.text.toString())
            }
        })
    }

    private fun updateClass(classDesc: String, position: Int) {
        val mClass = ItemsList[position]
        mClass.description = (classDesc)
        db!!.updateClass(mClass)
        ItemsList[position] = mClass
        mAdapter!!.notifyItemChanged(position)
    }

    private fun createMusicClass(classDescription: String) {
        val mClass = db!!.insertMusicClass(classDescription)
        val newClass = db!!.getItem(mClass)

        if (newClass != null) {
            ItemsList.add(0, newClass)
            mAdapter!!.notifyDataSetChanged()
        }
    }
}