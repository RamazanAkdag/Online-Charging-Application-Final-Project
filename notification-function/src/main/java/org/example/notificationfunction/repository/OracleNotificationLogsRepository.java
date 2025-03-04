package org.example.notificationfunction.repository;

import com.ramobeko.oracle.model.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OracleNotificationLogsRepository extends JpaRepository<NotificationLog, Long> {
}
