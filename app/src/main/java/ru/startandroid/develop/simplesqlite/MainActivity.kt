package ru.startandroid.develop.simplesqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText

const val LOG_TAG = "myLogs"

class MainActivity : AppCompatActivity(), OnClickListener {

    private var btnAdd:Button? = null
    private var btnRead:Button? = null
    private var btnClear:Button? = null
    private var btnUpd:Button? = null
    private var btnDel:Button? = null

    private var etName:EditText? = null
    private var etEmail:EditText? = null
    private var etID:EditText? = null

    private var bdHelper:SQLiteOpenHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAdd = findViewById<View>(R.id.btnAdd) as Button
        btnAdd?.setOnClickListener(this)

        btnRead = findViewById<View>(R.id.btnRead) as Button
        btnAdd?.setOnClickListener(this)

        btnClear = findViewById<View>(R.id.btnClear) as Button
        btnClear?.setOnClickListener(this)

        btnUpd = findViewById<View>(R.id.btnUpd) as Button
        btnUpd?.setOnClickListener(this)

        btnDel = findViewById<View>(R.id.btnDel) as Button
        btnDel?.setOnClickListener(this)

        etName = findViewById<View>(R.id.etName) as EditText
        etEmail = findViewById<View>(R.id.etEmail) as EditText
        etID = findViewById<View>(R.id.etID) as EditText

        bdHelper = DBHelper(this)
    }

    override fun onClick(v: View?) {
        val cv = ContentValues()

        val name = etName?.text.toString()
        val email = etEmail?.text.toString()
        val id = etID?.text.toString()

        val db:SQLiteDatabase = bdHelper!!.writableDatabase

        when(v?.id) {
            R.id.btnAdd -> {
                Log.d(LOG_TAG, "---Insert in mytable: ---")

                cv.put("name", name)
                cv.put("email", email)

                val rowID = db.insert("mytable", null, cv)
                Log.d(LOG_TAG, "row inserted, ID = $rowID")
            }
            R.id.btnRead -> {
                Log.d(LOG_TAG, "---Row in mytable: ---")

                val c = db.query("mytable", null, null, null, null, null, null)

                if (c.moveToFirst()) {
                    val idColIndex: Int = c.getColumnIndex("id")
                    val nameColIndex: Int = c.getColumnIndex("name")
                    val emailColIndex: Int = c.getColumnIndex("email")

                    do {
                        Log.d(LOG_TAG,
                            "Id = ${c.getInt(idColIndex)}" +
                                    ", name = ${c.getString(nameColIndex)}" +
                                    ", email = ${c.getString(emailColIndex)}")
                    } while (c.moveToNext())
                } else {
                    Log.d(LOG_TAG, "0 rows")
                    c.close()
                }
            }
            R.id.btnClear -> {
                Log.d(LOG_TAG, "---Clear mytable: ---")

                val clearCount: Int = db.delete("mytable", null, null)

                Log.d(LOG_TAG, "deleted row count = $clearCount")
            }

            R.id.btnUpd -> {
                if (id.equals("", ignoreCase = true)) { } //break
                    Log.d(LOG_TAG, "---Update mytable: ---")
                    cv.put("name", name)
                    cv.put("name", email)

                val updCount = db.update("mytable", cv, "id = ?", arrayOf(id))
                Log.d(LOG_TAG, "update rows count = $updCount")
            }

            R.id.btnDel -> {
                if (id.equals("", ignoreCase = true)) {}//break

                    Log.d(LOG_TAG, "---Delete from mytable: ---")
                    val delCount = db.delete("mytable", "id = $id", null)
                    Log.d(LOG_TAG, "deleted rows count = $delCount")
                }
            }
        bdHelper?.close()
    }


    internal inner class DBHelper(context: Context?) :
        SQLiteOpenHelper (context, "myDB", null, 1) {
        override fun onCreate(db: SQLiteDatabase?) {
            Log.d(LOG_TAG, "---onCreate databaase ---")
            db?.execSQL("create table mytable ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "email text" + ");")
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersin: Int, newVersion: Int) {
        }
    }
}
