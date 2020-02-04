package org.shirshov.testtask02.network.repository

import com.fasterxml.jackson.annotation.JsonProperty
import org.shirshov.testtask02.data.Fixture
import org.shirshov.testtask02.data.Result
import org.shirshov.testtask02.network.http.HttpService
import org.shirshov.testtask02.network.utils.Path
import org.threeten.bp.LocalDateTime

class SportEventRepository(private val httpService: HttpService) : BaseHttpRepository() {

    suspend fun fixtures() = request {
        httpService.get<List<FixtureDto>>(Path.Http.FIXTURES)?.map { it.toFixture() }
    }

    suspend fun results() = request {
        httpService.get<List<FixtureDto>>(Path.Http.RESULTS)?.map { it.toResult() }
    }

    data class FixtureDto(
        val id: Long,
        val homeTeam: Team,
        val awayTeam: Team,
        val date: LocalDateTime,
        val competitionStage: CompetitionStage,
        val venue: Venue,
        val state: State?,
        val score: Score?
    ) {
        fun toFixture() = Fixture(competitionStage.competition.id, competitionStage.competition.name, venue.name, date, homeTeam.name, awayTeam.name, state == State.POSTPONED)

        fun toResult() =
            Result(competitionStage.competition.id, competitionStage.competition.name, venue.name, date, homeTeam.name, awayTeam.name, score?.home ?: 0, score?.away ?: 0)
    }

    data class Team(val id: Long, val name: String)

    data class CompetitionStage(val competition: Competition)

    data class Competition(val id: Long, val name: String)

    data class Venue(val id: Long, val name: String)

    data class Score(val home: Int, val away: Int)

    enum class State {
        @JsonProperty("preMatch")
        PREMATCH,
        @JsonProperty("postponed")
        POSTPONED
    }
}