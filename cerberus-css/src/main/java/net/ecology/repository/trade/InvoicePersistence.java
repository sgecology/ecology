package net.ecology.repository.trade;

import org.springframework.stereotype.Repository;

import net.ecology.entity.trade.Invoice;
import net.ecology.framework.persistence.CodeSerialPersistence;

@Repository
public interface InvoicePersistence extends CodeSerialPersistence<Invoice, Long>{
}
