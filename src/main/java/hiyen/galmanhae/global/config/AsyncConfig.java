package hiyen.galmanhae.global.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

	public static final String WEATHER_CONGESTION_API_THREAD_POOL = "weatherCongestionApiThreadPool";

	private static final int POOL_SIZE = 20;

	@Bean(name = WEATHER_CONGESTION_API_THREAD_POOL)
	public Executor weatherCongestionAPIThreadPool() {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(POOL_SIZE);
		executor.setMaxPoolSize(POOL_SIZE);
		return executor;
	}
}
