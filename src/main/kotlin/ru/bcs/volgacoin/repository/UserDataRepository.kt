package ru.bcs.volgacoin.repository

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.time.LocalDateTime

@Repository
@Transactional
class UserDataRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    val mapper:RowMapper<UserDataEntity> = RowMapper { rs, rowNum ->
        UserDataEntity(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getLong("clicks"),
            rs.getDouble("energy"),
            rs.getObject("lasT_login", Timestamp::class.java).toLocalDateTime()
        ) }

    fun getById(id: Long) = jdbcTemplate.query(
        "SELECT * from user_data WHERE id=:id",
        mapOf("id" to id),
        mapper
    ).firstOrNull()

    fun getByIdForUpdate(id: Long) = jdbcTemplate.query(
        "SELECT * from user_data WHERE id=:id FOR UPDATE",
        mapOf("id" to id),
        mapper
    ).firstOrNull()

    fun upsert(data: UserDataEntity) = jdbcTemplate.update(
        "INSERT INTO user_data (id, username, clicks, energy, last_login) " +
                "values (:id, :username, :clicks, :energy, :last_login) " +
                "ON CONFLICT (id) " +
                "DO UPDATE SET id=:id, username=:username, clicks=:clicks, energy=:energy, last_login=:last_login",
        mapOf(
            "id" to data.id,
            "username" to data.username,
            "clicks" to data.clicks,
            "energy" to data.energy,
            "last_login" to data.last_login
        )
    )

    fun getRating(limit: Int = 100): List<UserDataEntity> = jdbcTemplate.query(
        "SELECT * from user_data ORDER BY clicks LIMIT :limit",
        mapOf("limit" to limit),
        mapper
    )
}