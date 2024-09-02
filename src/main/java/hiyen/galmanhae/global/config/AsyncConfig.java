package hiyen.galmanhae.global.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Configuration
public class AsyncConfig {

	public static final String WEATHER_CONGESTION_API_THREAD_POOL = "weatherCongestionApiThreadPool";

	@Bean(name = WEATHER_CONGESTION_API_THREAD_POOL)
	public Executor weatherCongestionAPIThreadPool() {
		return Executors.newCachedThreadPool();
	}
}
