package com.example.newsappmvvm.data.mapper

import com.example.newsappmvvm.data.model.Article
import com.example.newsappmvvm.data.model.LocalArticle

class MapperLocalArticleImpl : MapperNews<LocalArticle, Article> {
    override fun map(input: LocalArticle): Article {
        return Article(
            articleId = input.articleId,
            author = input.author,
            content = input.content,
            description = input.description,
            publishedAt = input.publishedAt,
            url = input.url,
            source = input.source,
            urlToImage = input.urlToImage,
        )
    }
}