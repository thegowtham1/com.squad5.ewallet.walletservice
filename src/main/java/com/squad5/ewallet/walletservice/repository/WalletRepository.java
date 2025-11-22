package com.squad5.ewallet.walletservice.repository;


import com.squad5.ewallet.walletservice.entity.Wallet;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUserIdAndIsDeleted(Long userId, Byte isDeleted);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.userId = :userId AND w.isDeleted = 0")
    Optional<Wallet> findByUserIdForUpdate(@Param("userId") Long userId);

    boolean existsByAccountNumber(String accountNumber);
}

