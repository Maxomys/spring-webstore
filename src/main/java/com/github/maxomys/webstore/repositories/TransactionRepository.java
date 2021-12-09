package com.github.maxomys.webstore.repositories;

import com.github.maxomys.webstore.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {



}
