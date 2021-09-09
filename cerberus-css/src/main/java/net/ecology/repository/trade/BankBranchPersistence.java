package net.ecology.repository.trade;

import org.springframework.stereotype.Repository;

import net.ecology.entity.trade.BankBranch;
import net.ecology.framework.persistence.CodeNamePersistence;

@Repository
public interface BankBranchPersistence extends CodeNamePersistence<BankBranch, Long>{
}
