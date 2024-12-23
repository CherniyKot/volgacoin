package ru.bcs.volgacoin.api

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.bcs.volgacoin.api.dto.RatingResponseDto
import ru.bcs.volgacoin.repository.UserDataRepository

@RestController
@RequestMapping("/api/rating")
@PreAuthorize("hasRole('USER')")
class RatingControllerImpl(
    private val userDataRepository: UserDataRepository,
) {
    @GetMapping
    fun getRating(): List<RatingResponseDto> {
        return userDataRepository.getRating().map { RatingResponseDto(it.username, it.clicks) }
    }
}

