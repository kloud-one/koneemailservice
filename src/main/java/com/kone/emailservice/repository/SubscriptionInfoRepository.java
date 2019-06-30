package com.kone.emailservice.repository;

import com.kone.emailservice.domain.OrgUserEmailInfo;
import com.kone.emailservice.domain.SubscriptionInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the SubscriptionInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionInfoRepository extends JpaRepository<SubscriptionInfo, Long> {
    Optional<SubscriptionInfo> findOneByorguseremailinfoId(Long id);
}
