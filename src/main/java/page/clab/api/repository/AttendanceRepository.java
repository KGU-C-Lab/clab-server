package page.clab.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.type.entity.ActivityGroup;
import page.clab.api.type.entity.Attendance;
import page.clab.api.type.entity.AttendanceId;
import page.clab.api.type.entity.Member;

public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceId> {

        Page<Attendance> findAllByAttendanceIdMemberAndActivityGroupOrderByCreatedAt(Member member, ActivityGroup activityGroup, Pageable pageable);

        Page<Attendance> findAllByActivityGroupOrderByActivityDateAscAttendanceIdMemberAsc(ActivityGroup activityGroup, Pageable pageable);

}