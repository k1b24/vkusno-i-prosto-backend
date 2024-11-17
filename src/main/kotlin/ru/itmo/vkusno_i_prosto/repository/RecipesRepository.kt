package ru.itmo.vkusno_i_prosto.repository

import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import ru.itmo.vkusno_i_prosto.model.recipes.Recipe
import java.util.UUID

@Repository
interface RecipesRepository : MongoRepository<Recipe, String> {
    @Aggregation(pipeline = [
        "{'\$skip' : ?0}",
        "{'\$limit' : ?1}"
    ])
    fun findAll(offset: Long, limit: Long): List<Recipe>
}