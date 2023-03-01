package com.van.testService.filters;

import com.van.processor.domain.FilterFunction;
import org.springframework.stereotype.Component;

import static com.van.testService.common.Utils.getCurrentPrbrId;

@Component("prbrFilter")
public class PrbrAllOperationFilter implements FilterFunction {
	@Override
	public String getFilter() {
		return "PRBR_ID = " + getCurrentPrbrId();
	}
}
