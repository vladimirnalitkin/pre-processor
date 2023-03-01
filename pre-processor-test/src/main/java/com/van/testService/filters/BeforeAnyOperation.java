package com.van.testService.filters;

import com.van.processor.domain.ApplyOnItem;
import com.van.testService.model.GenericEntity;
import org.springframework.stereotype.Component;

import static com.van.testService.common.Utils.getCurrentPrbrId;

@Component("beforeAnyApply")
public class BeforeAnyOperation<K extends GenericEntity> implements ApplyOnItem<K> {
	@Override
	public void apply(K item) {
		item.setPrbrId(getCurrentPrbrId());
	}
}