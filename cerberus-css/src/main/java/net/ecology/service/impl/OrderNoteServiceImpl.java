package net.ecology.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.ecology.entity.trade.OrderNote;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;
import net.ecology.repository.trade.OrderNotePersistence;
import net.ecology.service.trade.OrderNoteService;

@Service
public class OrderNoteServiceImpl extends GenericService<OrderNote, Long> implements OrderNoteService{
	private static final long serialVersionUID = -644103846803081016L;
	
	@Inject 
	private OrderNotePersistence repository;
	
	protected IPersistence<OrderNote, Long> getPersistence() {
		return this.repository;
	}
}
