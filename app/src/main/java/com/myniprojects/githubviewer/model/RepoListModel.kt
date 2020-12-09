package com.myniprojects.githubviewer.model

sealed class RepoListModel
{
    data class RepoItem(val githubRepo: GithubRepo) : RepoListModel()
    data class RepoSeparatorItem(val after: Int) : RepoListModel()
}