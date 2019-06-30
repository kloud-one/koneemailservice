package com.kone.emailservice.repository;

import com.kone.emailservice.domain.OrgUserEmailInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;


/**
 * Spring Data  repository for the OrgUserEmailInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrgUserEmailInfoRepository extends JpaRepository<OrgUserEmailInfo, Long> {

    @Transactional
    @Modifying
    @Query(value = "INSERT IGNORE INTO ORG_USER_EMAIL_INFO(ORGANIZATION_NAME, ORG_MEMBER_EMAIL_ID) VALUES(?1, ?2)", nativeQuery = true)
    Integer saveDistinct(@Param("organizationName") String organizationName, @Param("orgMemberEmailId") String orgMemberEmailId);


    //public int find(@Param("organizationName") String organizationName, @Param("orgMemberEmailId") String orgMemberEmailId);

    Optional<OrgUserEmailInfo> findOneByOrganizationNameAndOrgMemberEmailId(String organizationName, String orgMemberEmailId);

}
