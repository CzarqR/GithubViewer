package com.myniprojects.githubviewer.model

sealed class RepoListModel
{
    data class RepoItem(val githubRepo: GithubRepo) : RepoListModel()
    data class RepoSeparatorItem(val after: Int, val before: Int = -1) : RepoListModel()
}