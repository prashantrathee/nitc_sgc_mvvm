package com.nitc.projectsgc.composable.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.nitc.projectsgc.models.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepo:NewsRepo
) : ViewModel(){

    private val _news = MutableStateFlow<Either<String,List<News>>>(Either.Right(emptyList()))
    val news = _news.asStateFlow()

    fun getNews(){
        viewModelScope.launch {
            _news.value = newsRepo.getNews()
        }
    }

    suspend fun deleteNews(userType:Int,newsID:String):Boolean{
        val deleted = withContext(Dispatchers.Main){
            newsRepo.deleteNews(userType,newsID)
        }
        return deleted
    }
    suspend fun addNews(userType: Int,news: News):Boolean{
        val added = withContext(Dispatchers.Main){
            newsRepo.addNews(userType,news)
        }
        return added
    }

}