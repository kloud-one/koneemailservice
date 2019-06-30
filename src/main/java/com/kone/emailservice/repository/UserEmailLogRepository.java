package com.kone.emailservice.repository;

import com.kone.emailservice.domain.UserEmailLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserEmailLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserEmailLogRepository extends JpaRepository<UserEmailLog, Long> {

}
