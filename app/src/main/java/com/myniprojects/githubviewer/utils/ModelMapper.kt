package com.myniprojects.githubviewer.utils

interface ModelMapper<OldModel, NewModel>
{
    fun mapToNewModel(entity: OldModel): NewModel

    fun mapToNewModelList(entities: List<OldModel>): List<NewModel>
    {
        return entities.map {
            mapToNewModel(it)
        }
    }
}