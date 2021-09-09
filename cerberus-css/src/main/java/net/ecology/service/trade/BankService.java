package net.ecology.service.trade;

import java.util.Optional;

import net.ecology.entity.trade.Bank;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.service.IGenericService;

public interface BankService extends IGenericService<Bank, Long> {

	/**
	 * Get one Bank with the provided name.
	 * 
	 * @param name
	 *            The Bank name
	 * @return The Bank
	 * @throws ObjectNotFoundException
	 *             If no such Bank exists.
	 */
	Optional<Bank> getByName(String name) throws ObjectNotFoundException;

	/**
	 * Get one Bank with the provided code.
	 * 
	 * @param code
	 *            The Bank code
	 * @return The Bank
	 * @throws ObjectNotFoundException
	 *             If no such Bank exists.
	 */
	Optional<Bank> getByCode(String code) throws ObjectNotFoundException;
}
