package net.ecology.repository.trade;

import org.springframework.stereotype.Repository;

import net.ecology.entity.trade.TradeAccount;
import net.ecology.framework.persistence.CodeNamePersistence;

@Repository
public interface TradeAccountPersistence extends CodeNamePersistence<TradeAccount, Long>{
}
