package cn.easy.xinjing.repository;

import cn.easy.xinjing.domain.UserVrRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;

import java.math.BigDecimal;
import java.util.List;

public interface UserVrRoomDao extends JpaRepository<UserVrRoom, String>, JpaSpecificationExecutor<UserVrRoom> {

    UserVrRoom findByUserIdAndVrRoomId(String userId, String vrRoomId);

    UserVrRoom findByUserId(String userId);

    Page<UserVrRoom> findAll(Pageable pageable);
    List<UserVrRoom> findAll();
    List<UserVrRoom> findByVrRoomIdAndType(String vrRoomId, int type);

    @Query(value = "select * from xj_user_vr_room group by vr_room_id",nativeQuery = true)
    List<UserVrRoom> getAll();

    @Query(value = "select uvr.* from xj_user_vr_room uvr,pb_user u where uvr.user_id=u.id and u.realname like :realName% and uvr.vr_room_id = :vrRoomId",nativeQuery = true)
    List<UserVrRoom> findByRealNameAndVrRoomId(@Param("realName") String realName,@Param("vrRoomId") String vrRoomId);

    @Query(value = "select uvr.* from xj_user_vr_room uvr,pb_user u where uvr.user_id=u.id and u.realname like :realName%",nativeQuery = true)
    List<UserVrRoom> findByRealName(@Param("realName") String realName);

}
