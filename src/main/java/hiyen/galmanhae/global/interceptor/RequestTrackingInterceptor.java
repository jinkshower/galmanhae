package hiyen.galmanhae.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class RequestTrackingInterceptor implements HandlerInterceptor {

	public static final String LOG_ID = "logId";

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
		final String requestURI = request.getRequestURI();

		final String uuid = UUID.randomUUID().toString().substring(0, 8);
		request.setAttribute(LOG_ID, uuid);

		log.info("Request [{}] [{}] [{}]", uuid, request.getMethod(), requestURI);
		return true;
	}

	@Override
	public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
		final Exception ex) throws Exception {
		final String requestURI = request.getRequestURI();

		final String logId = (String) request.getAttribute(LOG_ID);

		log.info("Response [{}] [{}] [{}]", logId, response.getStatus(), requestURI);
	}
}
