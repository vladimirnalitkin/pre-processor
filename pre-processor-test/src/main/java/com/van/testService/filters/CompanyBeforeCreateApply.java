package com.van.testService.filters;

import com.van.processor.domain.ApplyOnItem;
import com.van.testService.model.Company;
import org.springframework.stereotype.Component;

@Component("companyBeforeCreateApply")
public class CompanyBeforeCreateApply implements ApplyOnItem<Company> {
	@Override
	public void apply(Company item) {
		item.setName(item.getName() + " - 1");
	}
}
