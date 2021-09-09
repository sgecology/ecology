package net.ecology.service.impl.trade;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.ecology.entity.trade.Invoice;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;
import net.ecology.repository.trade.InvoicePersistence;
import net.ecology.service.trade.InvoiceService;

@Service
public class InvoiceServiceImpl extends GenericService<Invoice, Long> implements InvoiceService{
	private static final long serialVersionUID = -3091073932588099354L;
	
	@Inject 
	private InvoicePersistence repository;
	
	protected IPersistence<Invoice, Long> getPersistence() {
		return this.repository;
	}
}
