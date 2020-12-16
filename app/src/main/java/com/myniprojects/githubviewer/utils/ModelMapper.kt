package com.myniprojects.githubviewer.utils

interface ModelMapper<OldModel, NewModel>
{
    fun mapToNewModel(entity: OldModel): NewModel
}