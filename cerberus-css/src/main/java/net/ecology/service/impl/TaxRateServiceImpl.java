package net.ecology.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.ecology.entity.trade.TaxRate;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;
import net.ecology.repository.trade.TaxRatePersistence;
import net.ecology.service.trade.TaxRateService;

@Service
public class TaxRateServiceImpl extends GenericService<TaxRate, Long> implements TaxRateService {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5003151117371926438L;

	@Inject 
	private TaxRatePersistence repository;
	
	protected IPersistence<TaxRate, Long> getPersistence() {
		return this.repository;
	}
}
