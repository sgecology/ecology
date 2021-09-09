package net.ecology.repository.trade;

import org.springframework.stereotype.Repository;

import net.ecology.entity.emx.CustomerInvoice;
import net.ecology.framework.persistence.BasePersistence;

@Repository
public interface CustomerInvoicePersistence extends BasePersistence<CustomerInvoice, Long>{
}
