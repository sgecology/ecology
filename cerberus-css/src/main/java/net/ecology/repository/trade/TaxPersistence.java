package net.ecology.repository.trade;

import org.springframework.stereotype.Repository;

import net.ecology.entity.trade.Tax;
import net.ecology.framework.persistence.CodeNamePersistence;

@Repository
public interface TaxPersistence extends CodeNamePersistence<Tax, Long>{
}
