package net.ecology.service.trade;

import java.util.Optional;

import net.ecology.entity.trade.Portfolio;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.service.IGenericService;

public interface PortfolioService extends IGenericService<Portfolio, Long> {

	/**
	 * Get one Portfolio with the provided name.
	 * 
	 * @param name
	 *            The Portfolio name
	 * @return The Portfolio
	 * @throws ObjectNotFoundException
	 *             If no such Portfolio exists.
	 */
	Optional<Portfolio> getByName(String name) throws ObjectNotFoundException;

	/**
	 * Get one Portfolio with the provided code.
	 * 
	 * @param code
	 *            The Portfolio code
	 * @return The Portfolio
	 * @throws ObjectNotFoundException
	 *             If no such Portfolio exists.
	 */
	Optional<Portfolio> getByCode(String code) throws ObjectNotFoundException;
}
