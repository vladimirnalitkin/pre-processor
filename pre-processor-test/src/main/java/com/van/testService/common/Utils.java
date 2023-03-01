package com.van.testService.common;

import com.van.testService.model.UserDetails;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;

public abstract class Utils {

/*
    public static Long getCurrentPrbrId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDetails) authentication.getDetails()).getPrbrId();
    }
*/

	public static Long getCurrentPrbrId() {
		return 1L;
	}

}
