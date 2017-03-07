package com.weather.data.validation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ValidationUtil {

	public boolean checkString(String str, int validLegnth) {
		if (StringUtils.isEmpty(str))
			return false;

		if (StringUtils.length(str) < validLegnth)
			return false;

		return true;
	}
	
}
