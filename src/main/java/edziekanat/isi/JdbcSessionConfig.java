package edziekanat.isi;

import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 10080) // NOTE: tydzien narazie
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 60) // NOTE: 7 sekund narazie
public class JdbcSessionConfig {
}