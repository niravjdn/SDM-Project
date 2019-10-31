package com.sdmproject.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.context.request.RequestContextHolder;

import com.sdmproject.repository.UserRepository;

public class SuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private SessionRegistry sessionRegistry;

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	Logger logger = LoggerFactory.getLogger(UserRepository.class);

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {

		handle(request, response, authentication);
		clearAuthenticationAttributes(request);
	}

	protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {

		String targetUrl = determineTargetUrl(authentication);

		if (response.isCommitted()) {
			logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
			return;
		}

		redirectStrategy.sendRedirect(request, response, targetUrl);
	}

	protected String determineTargetUrl(Authentication authentication) {
		boolean isUser = false;
		boolean isAdmin = false;
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		System.out.println(authorities.toString());
		for (GrantedAuthority grantedAuthority : authorities) {
			if (grantedAuthority.getAuthority().equals("CLERK")) {
				isUser = true;
				break;
			} else if (grantedAuthority.getAuthority().equals("ADMIN")) {
				isAdmin = true;
				// if new authenticated user is admin remove all other admin
				List<SessionInformation> sessions = new ArrayList<SessionInformation>();
				for (Object principal : sessionRegistry.getAllPrincipals()) {

					if (userHasAuthority("ADMIN", ((User) principal).getAuthorities())) {
						List<SessionInformation> activeUserSessions = sessionRegistry.getAllSessions(principal, false);
						sessions.addAll(activeUserSessions);
					}
				}
				SessionInformation newAdminAuthId = sessionRegistry
						.getSessionInformation(RequestContextHolder.currentRequestAttributes().getSessionId());

				for (SessionInformation session : sessions) {
					System.out.println("newAdminAuthID " + newAdminAuthId);
					System.out.println("here " + session);
					if (newAdminAuthId != session) {
						// if sessions are not same as new admin then delete
						session.expireNow();
					}
				}

				break;
			}
		}

		if (isUser) {
			return "/clerk/home";
		} else if (isAdmin) {
			return "/admin/dashboard";
		} else {
			throw new IllegalStateException();
		}
	}

	public static boolean userHasAuthority(String authority, Collection<GrantedAuthority> collection) {
		for (GrantedAuthority grantedAuthority : collection) {
			if (authority.equals(grantedAuthority.getAuthority())) {
				return true;
			}
		}
		return false;
	}

	protected void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	protected RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}
}