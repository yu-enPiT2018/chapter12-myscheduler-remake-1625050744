package com.example.enpit_p12.myscheduler

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.text.format.DateFormat.*
import android.view.View
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

        val scheduleId = intent?.getLongExtra("schedule_id", -1L)
        if(scheduleId != -1L) {
            val schedule = realm.where<Shcedule>()
                    .equalTo("id", scheduleId).findFirst()
            dateEdit.setText(
            format("yyyy/MM/dd", schedule?.date))
            titleEdit.setText(schedule?.title)
            detailEdit.setText(schedule?.detail)
            delete.visibility = View.VISIBLE
        } else {
            delete.visibility = View.INVISIBLE
        }

        save.setOnClickListener {
            when(scheduleId) {
                -1L -> {
                    realm.executeTransaction {
                        val maxId = realm.where<Shcedule>().max("id")
                        val nextId = (maxId?.toLong() ?: 0L) + 1
                        val schedule = realm.createObject<Shcedule>(nextId)
                        dateEdit.text.toString().toDate("yyyy/MM/dd")?.let {
                            schedule.date = it
                        }
                        schedule.title = titleEdit.text.toString()
                        schedule.detail = detailEdit.text.toString()
                    }
                    alert("追加しました") {
                        yesButton { finish() }
                    }.show()
                }
                else ->{
                    realm.executeTransaction{
                        val schdule = realm.where<Shcedule>()
                                .equalTo("id", scheduleId).findFirst()
                        dateEdit.text.toString().toDate("yyyy/MM/dd")?.let {
                            schdule?.date = it
                        }
                        schdule?.title = titleEdit.text.toString()
                        schdule?.detail = detailEdit.text.toString()
                    }
                    alert ("修正しました"){
                        yesButton { finish() }
                    }.show()
                }
            }
        }

        delete.setOnClickListener{
            realm.executeTransaction{
                realm.where<Shcedule>().equalTo("id", scheduleId)
                        ?.findFirst()?.deleteFromRealm()
            }
            alert("削除しました") {
                yesButton { finish() }
            }.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun String.toDate(pattern: String = "yyyy/MM/dd HH:mm"): Date?{
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
