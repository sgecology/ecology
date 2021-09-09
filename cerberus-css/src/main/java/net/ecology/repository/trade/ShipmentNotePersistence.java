package net.ecology.repository.trade;

import org.springframework.stereotype.Repository;

import net.ecology.entity.trade.ShipmentNote;
import net.ecology.framework.persistence.CodeSerialPersistence;

@Repository
public interface ShipmentNotePersistence extends CodeSerialPersistence<ShipmentNote, Long>{
}
