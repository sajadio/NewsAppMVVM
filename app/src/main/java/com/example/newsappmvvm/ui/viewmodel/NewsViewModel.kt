package com.example.newsappmvvm.ui.viewmodel

import androidx.lifecycle.*
import androidx.paging.*
import com.example.newsappmvvm.data.mapper.MapperLocalArticleImpl
import com.example.newsappmvvm.data.model.Article
import com.example.newsappmvvm.data.model.LocalArticle
import com.example.newsappmvvm.data.repository.RepositoryImpl
import com.example.newsappmvvm.data.storage.DataStorageImp
import com.example.newsappmvvm.ui.adapter.OnItemClickListener
import com.example.newsappmvvm.utils.UiMode
import com.example.newsappmvvm.utils.event.Event
import com.example.newsappmvvm.utils.listOfCategories
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class NewsViewModel(
    private val repository: RepositoryImpl,
    private val dataStorage: DataStorageImp,
) : ViewModel(), OnItemClickListener {

    val selectedTheme = dataStorage.uiModeFlow.asLiveData()

    private lateinit var _newsBreaking: Flow<PagingData<Article>>
    val newsBreaking: Flow<PagingData<Article>> get() = _newsBreaking

    private lateinit var _newsQuery: Flow<PagingData<Article>>
    val newsQuery: Flow<PagingData<Article>> get() = _newsQuery

    private lateinit var _responseCategories: Flow<PagingData<Article>>
    val responseCategories: Flow<PagingData<Article>> get() = _responseCategories

    val categories = MutableLiveData(listOfCategories)

    private val _article = MutableLiveData<Article>()
    val article: LiveData<Article> get() = _article

    val clickSearchEvent: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val clickBackEvent: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val clickMapperLocalArticleEvent = MutableLiveData<Event<Article>>()
    val clickArticleEvent = MutableLiveData<Event<Article>>()
    val clickWebViewEvent = MutableLiveData<Event<Article>>()
    val toastEvent = MutableLiveData<Event<Boolean>>()

    val fetchLocalArticles: LiveData<List<LocalArticle>> =
        repository.fetchLocalArticles().asLiveData()

    init {
        viewModelScope.launch {
            _newsBreaking = repository.getBreakingNews().cachedIn(viewModelScope)
        }

    }

    fun changeSelectedTheme(uiMode: UiMode) {
        viewModelScope.launch {
            when (uiMode) {
                UiMode.LIGHT -> {
                    dataStorage.setUIMode(uiMode)
                }
                UiMode.DARK -> {
                    dataStorage.setUIMode(uiMode)
                }
            }

        }
    }

    fun getResponseDataByQuery(query: String) {
        viewModelScope.launch {
            _newsQuery = repository.getResponseDataByQuery(query).cachedIn(viewModelScope)
        }
    }

    fun getResponseDataByCategory(category: String) {
        viewModelScope.launch {
            _responseCategories =
                repository.getResponseDataByCategory(category).cachedIn(viewModelScope)
        }
    }

    fun onClickSearch() {
        clickSearchEvent.postValue(Event(true))
    }

    fun onClickBack() {
        clickBackEvent.postValue(Event(true))
    }

    fun clearLocalArticle(localArticle: LocalArticle, reInsertItem: Boolean) {
        viewModelScope.launch {
            repository.clearLocalArticle(localArticle, reInsertItem)
        }
    }

    fun clearAllLocalArticles() {
        viewModelScope.launch {
            repository.clearAllLocalArticles()
        }
    }

    fun checkExistsItem(url: String) = repository.checkExistsItem(url)

    override fun onClickItemLocalMapArticle(localArticle: LocalArticle) {
        val mapperLocalArticle = MapperLocalArticleImpl().map(localArticle)
        clickMapperLocalArticleEvent.postValue(
            Event(mapperLocalArticle)
        )
    }

    override fun onClickItemArticle(article: Article) {
        clickArticleEvent.postValue(Event(article))
    }

    override fun onClickItemWebView(article: Article) {
        clickWebViewEvent.postValue(Event(article))
    }

    override fun onClickItemInsert(article: Article) {
        viewModelScope.launch {
            toastEvent.postValue(Event((repository.insertLocalArticles(article))))
        }
    }

}

