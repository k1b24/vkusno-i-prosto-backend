package ru.itmo.vkusno_i_prosto.repository

import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import ru.itmo.vkusno_i_prosto.model.recipes.Recipe

@Repository
interface RecipesRepository : MongoRepository<Recipe, String> {
    @Aggregation(pipeline = [
        "{ \$match : { 'ingredients' : { \$in : ?0}, 'ingredients' : { \$nin : ?1} } }",
        "{'\$skip' : ?2}",
        "{'\$limit' : ?3}"
    ])
    fun findAll(ingredients: List<String>, excludeIngredients: List<String>, offset: Long, limit: Long): List<Recipe>

    @Aggregation(pipeline = [
        "{ \$match : { 'ingredients' : { \$nin : ?0}, 'name' : { \$regex : ?1 } } }",
        "{'\$skip' : ?2}",
        "{'\$limit' : ?3}"
    ])
    fun findAll(excludeIngredients: List<String>, name: String, offset: Long, limit: Long): List<Recipe>

    @Aggregation(pipeline = [
        "{ \$match : { 'ingredients' : { \$in : ?0 }, 'ingredients' : { \$nin : ?1}, 'ownerUsername' : ?2, 'name' : { \$regex : ?3 }} }",
        "{'\$skip' : ?4}",
        "{'\$limit' : ?5}"
    ])
    fun findAllByUsername(ingredients: List<String>, excludeIngredients: List<String>, ownerUsername: String, name: String, offset: Long, limit: Long): List<Recipe>

    @Aggregation(pipeline = [
        "{ \$match : { 'ownerUsername' : ?0, 'ingredients' : { \$nin : ?1}, 'name' : { \$regex : ?2 }} }",
        "{'\$skip' : ?3}",
        "{'\$limit' : ?4}"
    ])
    fun findAllByUsername(ownerUsername: String, excludeIngredients: List<String>, name: String, offset: Long, limit: Long): List<Recipe>

    @Aggregation(pipeline = [
        "{ \$match : { 'favorites' : { \$in : ?0}, 'name' : { \$regex : ?1 }} }",
        "{'\$skip' : ?2}",
        "{'\$limit' : ?3}"
    ])
    fun findByFavoritesContains(login: List<String>, name: String, offset: Long, limit: Long): List<Recipe>

    fun countByFavoritesContains(login: String): Long
}