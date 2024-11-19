package org.joksin.multitenancy.database.migration;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

@Slf4j
@Singleton
@AllArgsConstructor
public class TenantMigrationExecutor implements ApplicationEventListener<StartupEvent> {

  private static String[] tenantSchemas = new String[] {"tenant_a", "tenant_b"};

  private final DataSource dataSource;

  @Override
  @Transactional
  public void onApplicationEvent(StartupEvent event) {
    log.info("Executing tenant migration");

    for (String schema : tenantSchemas) {
      var flyway =
          Flyway.configure()
              .dataSource(dataSource)
              .schemas(schema) // Set schema dynamically
              .locations("classpath:db/migration2")
              .load();
      flyway.migrate();
    }

    log.info("All tenants are migrated");
  }
}
