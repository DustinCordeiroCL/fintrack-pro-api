package com.dustin.fintrack;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.sql.init.mode=never" // Evita que ele tente rodar scripts do Postgres no H2
})
class FintrackProApiApplicationTests {

    @Test
    void contextLoads() {
        // Este teste serve apenas para garantir que o Spring consegue subir o contexto da aplicação sem quebrar.
    }
}