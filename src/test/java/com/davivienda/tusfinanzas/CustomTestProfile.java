// src/test/java/com/davivienda/tusfinanzas/CustomTestProfile.java
package com.davivienda.tusfinanzas;

import io.quarkus.test.junit.QuarkusTestProfile;
import java.util.Map;

public class CustomTestProfile implements QuarkusTestProfile {
    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of(
                // H2 en memoria
                "quarkus.datasource.db-kind", "h2",
                "quarkus.datasource.jdbc.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
                "quarkus.datasource.username", "sa",
                "quarkus.datasource.password", "sa",
                "quarkus.hibernate-orm.database.generation", "drop-and-create",
                // Desactiva JWT real
                "quarkus.smallrye-jwt.enabled", "false",
                // Puerto aleatorio
                "quarkus.http.test-port", "0"
        );
    }
}
