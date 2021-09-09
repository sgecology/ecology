package net.ecology.repository.trade;

import org.springframework.stereotype.Repository;

import net.ecology.entity.trade.Bank;
import net.ecology.framework.persistence.CodeNamePersistence;

@Repository
public interface BankPersistence extends CodeNamePersistence<Bank, Long>{
}
