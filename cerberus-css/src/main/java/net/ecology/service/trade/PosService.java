package net.ecology.service.trade;

import java.util.Optional;

import net.ecology.entity.trade.Pos;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.service.IGenericService;

public interface PosService extends IGenericService<Pos, Long> {

	/**
	 * Get one Pos with the provided name.
	 * 
	 * @param name
	 *            The Pos name
	 * @return The Pos
	 * @throws ObjectNotFoundException
	 *             If no such Pos exists.
	 */
	Optional<Pos> getByName(String name) throws ObjectNotFoundException;

	/**
	 * Get one Pos with the provided code.
	 * 
	 * @param code
	 *            The Pos code
	 * @return The Pos
	 * @throws ObjectNotFoundException
	 *             If no such Pos exists.
	 */
	Optional<Pos> getByCode(String code) throws ObjectNotFoundException;
}
