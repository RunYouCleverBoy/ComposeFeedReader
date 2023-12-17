package com.playgrounds.daggerkspplayground.di

import com.playgrounds.daggerkspplayground.api.RssApi
import com.playgrounds.daggerkspplayground.api.RssApiImpl
import com.playgrounds.daggerkspplayground.repos.FeedRepo
import com.playgrounds.daggerkspplayground.repos.FeedRepoImpl
import com.playgrounds.daggerkspplayground.repos.HistoryRepo
import com.playgrounds.daggerkspplayground.repos.HistoryRepoImpl
import com.playgrounds.daggerkspplayground.repos.TabRepo
import com.playgrounds.daggerkspplayground.repos.TabRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MainModule {
    @Binds
    abstract fun provideRssApi(rssApiImpl: RssApiImpl): RssApi

    @Binds
    abstract fun provideFeedRepo(feedRepoImpl: FeedRepoImpl): FeedRepo

    @Binds
    abstract fun provideTabRepo(tabRepoImpl: TabRepoImpl): TabRepo

    @Binds
    abstract fun provideHistoryRepo(historyRepoImpl: HistoryRepoImpl): HistoryRepo
}

