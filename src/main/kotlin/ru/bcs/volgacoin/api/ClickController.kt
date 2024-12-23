package ru.bcs.volgacoin.api

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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
    @GetMapping
    fun getClicks(): ClickResponseDto {
        return getOrCreateUser().let { ClickResponseDto(it.clicks, it.energy.toLong()) }
    }

    @PostMapping
    @Transactional
    fun addClicks(request: AddClicksRequestDto): ClickResponseDto {
        val user = getOrCreateUser()
        val now = LocalDateTime.now()
        val overflowingEnergy = user.energy + calculateEnergyRegen(user.last_login, now)
        val actualClicks = min(request.clicks, overflowingEnergy.toLong())
        userDataRepository.upsert(
            user.copy(
                clicks = user.clicks + actualClicks,
                energy = min(overflowingEnergy.toLong() - actualClicks, maxEnergy).toDouble(),
                last_login = now
            )
        )
        return getClicks()
    }

    private fun getOrCreateUser(): UserDataEntity {
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
            return userDataRepository.getById(currentUser.id)!!
        }
        return userData
    }

    private fun calculateEnergyRegen(from: LocalDateTime, to: LocalDateTime) =
        from.until(to, ChronoUnit.MILLIS) * energyRegenPerMillis
}

