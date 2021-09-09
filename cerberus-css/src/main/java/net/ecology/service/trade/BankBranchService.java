package net.ecology.service.trade;

import java.util.Optional;

import net.ecology.entity.trade.BankBranch;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.service.IGenericService;

public interface BankBranchService extends IGenericService<BankBranch, Long> {

	/**
	 * Get one Bank with the provided name.
	 * 
	 * @param name
	 *            The Bank Branch name
	 * @return The Bank Branch
	 * @throws ObjectNotFoundException
	 *             If no such Bank Branch exists.
	 */
	Optional<BankBranch> getByName(String name) throws ObjectNotFoundException;

	/**
	 * Get one Bank with the provided code.
	 * 
	 * @param code
	 *            The Bank Branch code
	 * @return The Bank Branch
	 * @throws ObjectNotFoundException
	 *             If no such Bank Branch exists.
	 */
	Optional<BankBranch> getByCode(String code) throws ObjectNotFoundException;
}
