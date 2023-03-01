package com.van.testService.repository;

import com.van.testService.model.UserDetails;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

public abstract class AbstractRepositoryTest {
	protected final Log log = LogFactory.getLog(getClass());

	protected static long prbrId = 1L;

	static void init() {
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(new Authentication() {
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return null;
			}

			@Override
			public Object getCredentials() {
				return null;
			}

			@Override
			public Object getDetails() {
				return new UserDetails(prbrId);
			}

			@Override
			public Object getPrincipal() {
				return null;
			}

			@Override
			public boolean isAuthenticated() {
				return false;
			}

			@Override
			public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
			}

			@Override
			public String getName() {
				return null;
			}
		});
		SecurityContextHolder.setContext(securityContext);
	}

	public static void setPrbrId(long prbrId) {
		AbstractRepositoryTest.prbrId = prbrId;
	}
}
