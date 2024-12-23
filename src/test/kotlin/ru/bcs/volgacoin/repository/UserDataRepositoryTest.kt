package ru.bcs.volgacoin.repository

import com.opentable.db.postgres.embedded.EmbeddedPostgres
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.sql.DataSource
import kotlin.test.assertContains


class UserDataRepositoryTest {

    var dataSource: DataSource = EmbeddedPostgres.builder().start().getPostgresDatabase()

    init {
        dataSource.connection.createStatement().execute("CREATE USER volgacoin_user WITH PASSWORD '123456';\n" +
                "\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS public.user_data\n" +
                "(\n" +
                "    id bigint NOT NULL,\n" +
                "    username text COLLATE pg_catalog.\"default\" NOT NULL,\n" +
                "    clicks bigint NOT NULL,\n" +
                "    energy double precision NOT NULL,\n" +
                "    last_login timestamp with time zone NOT NULL,\n" +
                "    CONSTRAINT user_data_pkey PRIMARY KEY (id)\n" +
                ");\n" +
                "GRANT ALL ON TABLE public.user_data TO volgacoin_user;")
    }


    val jdbcTemplate=NamedParameterJdbcTemplate(dataSource)

    var userDataRepository= UserDataRepository(jdbcTemplate)

    @Test
    fun testBasic() {
        assertNull(userDataRepository.getById(1))

        val user = UserDataEntity(1, "test", 1, 2.0, LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS))
        userDataRepository.upsert(user)
        assertEquals(user, userDataRepository.getById(1))

        val user2 = user.copy(clicks = 333)
        userDataRepository.upsert(user2)
        assertEquals(user2, userDataRepository.getById(1))

        val user3 = user2.copy(id = 2, clicks = 4)
        userDataRepository.upsert(user3)
        userDataRepository.getRating().let {
            assertTrue(it.size == 2)
            assertContains(it, user2)
            assertContains(it, user3)
        }
    }
}