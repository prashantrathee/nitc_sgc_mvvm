package com.nitc.projectsgc.composable.news

import android.util.Log
import arrow.core.Either
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nitc.projectsgc.models.News
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class NewsRepo @Inject constructor(){


    suspend fun addNews(news: News):Boolean{
        return suspendCoroutine {continuation ->

            var database = FirebaseDatabase.getInstance()
            var reference = database.reference.child("news")
            var newsID = reference.push().key.toString()
            news.newsID = newsID
            reference.child(newsID).setValue(news).addOnCompleteListener { task->
                if(task.isSuccessful){
                    continuation.resume(true)
                }else{
                    continuation.resume(false)
                }
            }
        }
    }
    suspend fun deleteNews(newsID:String):Boolean{
        return suspendCoroutine {continuation ->

            var database = FirebaseDatabase.getInstance()
            var reference = database.reference.child("news")
            reference.child(newsID).removeValue().addOnCompleteListener { task->
                if(task.isSuccessful){
                    continuation.resume(true)
                }else{
                    continuation.resume(false)
                }
            }
        }
    }


    suspend fun getNews(): Either<String,ArrayList<News>>?{
        return suspendCoroutine { continuation ->
            var database = FirebaseDatabase.getInstance()
            var reference = database.reference.child("news")
            var isResumed = false
            var newsArray = arrayListOf<News>()
            reference.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for ( ds in snapshot.children){
                        try{
                            val news = ds.getValue(News::class.java)
                            newsArray.add(news!!)

                        }catch(excCasting:Exception){
                            Log.d("getNews","Error in casting news : $excCasting")
                            continue
                        }
                    }
                    if(!isResumed){
                        isResumed = true
                        continuation.resume(Either.Right(newsArray))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if(!isResumed){
                        isResumed = true
                        continuation.resume(Either.Left("Error in database : $error"))
                    }
                }
            })
        }
    }


}