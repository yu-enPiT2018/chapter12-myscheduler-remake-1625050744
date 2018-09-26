package com.example.enpit_p12.myscheduler

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activitiy_schedule_edit.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class SchduleEditActivity : AppCompatActivity() {
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitiy_schedule_edit)
        realm = Realm.getDefaultInstance()

        save.setOnClickListener {
            realm.executeTransaction {
                val maxId = realm.where<Shcedule>().max("id")
                val nextId = (maxId?.toLong() ?: 0L) + 1
                val schedule = realm.createObject<Shcedule>(nextId)
                dateEdit.text.toString().toDate("yyyy/MM/ddd")?.let {
                    schedule.date = it
                }
                schedule.title = titleEdit.text.toString()
                schedule.detail = detailEdit.text.toString()
            }
                alert("追加しました"){
                    yesButton { finish() }
                }.show()

            }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun String.toDate(pattern: String = "yyyy/MM/ddd HH:mm"): Date?{
        val sdFormat = try{
            SimpleDateFormat(pattern)
        }catch (e: IllegalAccessException){
            null
        }
        val date = sdFormat?.let {
            try {
                it.parse(this)
            }catch (e: ParseException){
                null
            }
        }
        return date
    }
}
