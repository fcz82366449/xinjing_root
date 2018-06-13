package cn.easy.xinjing.service;

import cn.easy.base.bean.PageBean;
import cn.easy.base.domain.User;
import cn.easy.base.service.BaseService;
import cn.easy.base.service.UserService;
import cn.easy.base.utils.Constants;
import cn.easy.xinjing.domain.UserVrRoom;
import cn.easy.xinjing.repository.UserVrRoomDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
public class UserVrRoomService extends BaseService<UserVrRoom> {
    @Autowired
    private UserVrRoomDao	userVrRoomDao;
    @Autowired
    private UserService userService;

    public List<UserVrRoom> findByRealNameAndVrRoomId(String realName,String vrRoomId){
        if(realName!=null) {
            if (vrRoomId.trim().equals("") || vrRoomId == null) {
                 return userVrRoomDao.findByRealName(realName);
            } else {
                return userVrRoomDao.findByRealNameAndVrRoomId(realName, vrRoomId);
            }
        }else{
            return userVrRoomDao.findAll();
        }
    }

    public List<UserVrRoom> getAll(){
        return userVrRoomDao.getAll();
    }
    public Page<UserVrRoom> search(Map<String, Object> searchParams, PageBean pageBean) {
        return userVrRoomDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "id")));
    }
    public Page<UserVrRoom> findAll(PageBean pageBean){
        return userVrRoomDao.findAll(pageBean.toPageRequest(new Sort(Direction.DESC, "id")));
    }

    /**
     * 删除vr管理员的同时，将关联的user信息软删除
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUserVrRoom(String id) {
        UserVrRoom userVrRoom = getOne(id);
        User user = userVrRoom.getUser();
        user.setHidden(Constants.TRUE);
        userService.update(user);
        userVrRoomDao.delete(id);
    }
    public Page<UserVrRoom> findAll(Map<String,Object> searchParams,PageBean pageBean){
        return userVrRoomDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "id")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        userVrRoomDao.delete(id);
    }

    public UserVrRoom getOne(String id) {
        return userVrRoomDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserVrRoom save(UserVrRoom userVrRoom) {
        return userVrRoomDao.save(userVrRoom);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserVrRoom getByUserIdAndRoomId(String userId, String vrRoomId) {
        return userVrRoomDao.findByUserIdAndVrRoomId(userId, vrRoomId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserVrRoom getByUserId(String userId) {
        return userVrRoomDao.findByUserId(userId);
    }

    public List<UserVrRoom> findByVrRoomIdAndType(String vrRoomId, int type) {
        return userVrRoomDao.findByVrRoomIdAndType(vrRoomId, type);
    }
}


