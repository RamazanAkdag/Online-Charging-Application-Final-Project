package org.example.notificationfunction.repository;

import com.ramobeko.oracle.model.OracleCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OracleCustomerRepository extends JpaRepository<OracleCustomer, Long> {
    // Eğer ek sorgulara ihtiyacınız varsa buraya ekleyebilirsiniz.
    // Örneğin: Optional<OracleCustomer> findByEmail(String email);
}

