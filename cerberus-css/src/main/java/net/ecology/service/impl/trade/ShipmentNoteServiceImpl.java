package net.ecology.service.impl.trade;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.ecology.entity.trade.ShipmentNote;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;
import net.ecology.repository.trade.ShipmentNotePersistence;
import net.ecology.service.trade.ShipmentNoteService;

@Service
public class ShipmentNoteServiceImpl extends GenericService<ShipmentNote, Long> implements ShipmentNoteService{
	private static final long serialVersionUID = -4524060954777123091L;

	@Inject 
	private ShipmentNotePersistence repository;
	
	protected IPersistence<ShipmentNote, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public Optional<ShipmentNote> getBySerial(String serial) throws ObjectNotFoundException {
		return repository.findBySerial(serial);
	}

	@Override
	public Optional<ShipmentNote> getByCode(String code) throws ObjectNotFoundException {
		return repository.findByCode(code);
	}
}
