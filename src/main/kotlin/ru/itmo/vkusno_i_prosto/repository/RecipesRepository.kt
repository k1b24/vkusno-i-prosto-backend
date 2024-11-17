package ru.itmo.vkusno_i_prosto.repository

import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import ru.itmo.vkusno_i_prosto.model.recipes.Recipe

@Repository
interface RecipesRepository : MongoRepository<Recipe, String> {
    @Aggregation(pipeline = [
        "{ \$match : { 'ingredients' : { \$in : ?0}} }",
        "{'\$skip' : ?1}",
        "{'\$limit' : ?2}"
    ])
    fun findAll(ingredients: List<String>, offset: Long, limit: Long): List<Recipe>

    @Aggregation(pipeline = [
        "{'\$skip' : ?0}",
        "{'\$limit' : ?1}"
    ])
    fun findAll(offset: Long, limit: Long): List<Recipe>

    @Aggregation(pipeline = [
        "{ \$match : { 'ingredients' : { \$in : ?0 }, 'ownerUsername' : ?1} }",
        "{'\$skip' : ?2}",
        "{'\$limit' : ?3}"
    ])
    fun findAllByUsername(ingredients: List<String>, ownerUsername: String, offset: Long, limit: Long): List<Recipe>

    @Aggregation(pipeline = [
        "{ \$match : { 'ownerUsername' : ?0} }",
        "{'\$skip' : ?1}",
        "{'\$limit' : ?2}"
    ])
    fun findAllByUsername(ownerUsername: String, offset: Long, limit: Long): List<Recipe>

    @Aggregation(pipeline = [
        "{ \$match : { 'favorites' : { \$in : ?0}} }",
        "{'\$skip' : ?1}",
        "{'\$limit' : ?2}"
    ])
    fun findByFavoritesContains(login: List<String>, offset: Long, limit: Long): List<Recipe>

    fun countByFavoritesContains(login: String): Long
}