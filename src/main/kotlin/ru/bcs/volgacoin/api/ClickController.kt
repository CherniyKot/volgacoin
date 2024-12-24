package ru.bcs.volgacoin.api

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import ru.bcs.volgacoin.api.dto.AddClicksRequestDto
import ru.bcs.volgacoin.api.dto.ClickResponseDto
import ru.bcs.volgacoin.repository.UserDataEntity
import ru.bcs.volgacoin.repository.UserDataRepository
import ru.bcs.volgacoin.service.TelegramInitData
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.min


@RestController
@RequestMapping("/api/click")
@PreAuthorize("hasRole('USER')")
class ClickControllerImpl(
    private val userDataRepository: UserDataRepository,
    @Value("\${click.energy.max:2500}") private val maxEnergy: Long,
    @Value("\${click.energy.regen:0.001}") private val energyRegenPerMillis: Double,
) {
    private val logger = LoggerFactory.getLogger(ClickControllerImpl::class.java)

//    @GetMapping
//    fun getClicks(): ClickResponseDto {
//        return getOrCreateUser().let { ClickResponseDto(it.clicks, it.energy.toLong()) }
//    }

    @PostMapping
    @Transactional
    fun addClicks(@RequestBody request: AddClicksRequestDto): ClickResponseDto {
        val user = getOrCreateUser(true)
        val now = LocalDateTime.now()
        val regenerated = calculateEnergyRegen(user.last_login, now)
        logger.debug("Restored {} energy between {} and {}", regenerated, user.last_login, now)
        val overflowingEnergy = min(user.energy + regenerated, maxEnergy.toDouble() * 2)
        val actualClicks = min(request.clicks, overflowingEnergy.toLong())
        val newUserData = user.copy(
            clicks = user.clicks + actualClicks,
            energy = min(overflowingEnergy - actualClicks, maxEnergy.toDouble()),
            last_login = now
        )
        userDataRepository.upsert(newUserData)
        return newUserData.let { ClickResponseDto(it.clicks, it.energy.toLong(), maxEnergy) }
    }

    private fun getOrCreateUser(forUpdate: Boolean = false): UserDataEntity {
        val currentUser = (SecurityContextHolder.getContext().authentication.principal as TelegramInitData).userData
        val userData = userDataRepository.getById(currentUser.id)
        if (userData == null) {
            userDataRepository.upsert(
                UserDataEntity(
                    currentUser.id,
                    currentUser.username,
                    0,
                    maxEnergy.toDouble(),
                    LocalDateTime.now()
                )
            )
        }
        if (forUpdate) return userDataRepository.getByIdForUpdate(currentUser.id)!!
        return userDataRepository.getById(currentUser.id)!!
    }

    private fun calculateEnergyRegen(from: LocalDateTime, to: LocalDateTime) =
        from.until(to, ChronoUnit.MILLIS) * energyRegenPerMillis
}

