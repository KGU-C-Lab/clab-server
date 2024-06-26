package page.clab.api.domain.blacklistIp.dao;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.blacklistIp.domain.BlacklistIp;

@Repository
public interface BlacklistIpRepository extends JpaRepository<BlacklistIp, Long> {

    Page<BlacklistIp> findAll(Pageable pageable);

    boolean existsByIpAddress(String ipAddress);

    Optional<BlacklistIp> findByIpAddress(String ipAddress);

}