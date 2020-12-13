package com.myniprojects.githubviewer.repository.mapper

import com.myniprojects.githubviewer.model.GithubRepo
import com.myniprojects.githubviewer.network.ResponseItem
import com.myniprojects.githubviewer.utils.ModelMapper
import javax.inject.Inject

class NetworkToDomainReposMapper @Inject constructor() : ModelMapper<ResponseItem, GithubRepo>
{
    override fun mapToNewModel(entity: ResponseItem): GithubRepo
    {
        return GithubRepo(
            id = entity.id,
            name = entity.name,
            fullName = entity.fullName,
            description = entity.description,
            url = entity.url,
            stars = entity.stars,
            forks = entity.forks,
            language = entity.language
        )
    }
}