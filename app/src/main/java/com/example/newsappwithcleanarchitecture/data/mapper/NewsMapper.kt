package com.example.newsappwithcleanarchitecture.data.mapper

import com.example.newsappwithcleanarchitecture.data.local.NewsEntity
import com.example.newsappwithcleanarchitecture.data.remote.NewsDTO
import com.example.newsappwithcleanarchitecture.domain.model.News

object NewsMapper {
    fun mapDtoToDomain(dto: NewsDTO): News {
        return News(
            id = dto.id,
            title = dto.title,
            description = dto.description,
            image = dto.image
        )
    }

    fun mapEntityToDomain(entity: NewsEntity): News {
        return News(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            image = entity.image
        )
    }

    fun mapDomainToEntity(domain: News) = NewsEntity(
        id = domain.id,
        title = domain.title,
        description = domain.description,
        image = domain.image
    )

    fun mapDtoListToDomainList(dtos: List<NewsDTO>) = dtos.map { mapDtoToDomain(it) }
    fun mapEntityListToDomainList(entities: List<NewsEntity>) = entities.map { mapEntityToDomain(it) }
    fun mapDomainListToEntityList(domains: List<News>) = domains.map { mapDomainToEntity(it) }
}