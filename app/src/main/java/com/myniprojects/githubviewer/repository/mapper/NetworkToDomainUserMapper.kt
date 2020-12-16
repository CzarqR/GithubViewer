package com.myniprojects.githubviewer.repository.mapper

import com.myniprojects.githubviewer.model.GithubUser
import com.myniprojects.githubviewer.network.UserResponse
import com.myniprojects.githubviewer.utils.ModelMapper
import javax.inject.Inject

class NetworkToDomainUserMapper @Inject constructor() : ModelMapper<UserResponse, GithubUser>
{
    override fun mapToNewModel(entity: UserResponse): GithubUser
    {
        return GithubUser(
            login = entity.login,
            avatarUrl = entity.avatarUrl,
            url = entity.url,
            name = entity.name,
            company = entity.company,
            blog = entity.blog,
            location = entity.location,
            email = entity.email,
            bio = entity.bio,
            twitter = entity.twitter,
            publicRepos = entity.publicRepos,
            followers = entity.followers,
            following = entity.following,
            createdAt = entity.createdAt,
        )
    }
}