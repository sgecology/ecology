package net.ecology.framework.service;

import java.io.Serializable;
import java.util.Optional;

import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.entity.Repo;

public interface ICodeSerialService<T extends Repo, K extends Serializable> extends IGenericService<T, K> {

	/**
	 * Get one business object with the provided serial.
	 * 
	 * @param serial
	 *            The business object's serial
	 * @return The business object
	 * @throws ObjectNotFoundException
	 *             If no such business object exists.
	 */
	Optional<T> getBySerial(String serial) throws ObjectNotFoundException;

	/**
	 * Get one business object with the provided code.
	 * 
	 * @param code
	 *            The business object's code
	 * @return The business object
	 * @throws ObjectNotFoundException
	 *             If no such business object exists.
	 */
	Optional<T> getByCode(String code) throws ObjectNotFoundException;
}
