package net.ecology.repository.trade;

import org.springframework.stereotype.Repository;

import net.ecology.entity.trade.OrderNote;
import net.ecology.framework.persistence.CodeNamePersistence;

@Repository
public interface OrderNotePersistence extends CodeNamePersistence<OrderNote, Long>{
}
