package cn.easy.xinjing.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.easy.base.service.BaseService;
import cn.easy.xinjing.utils.Constants;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.easy.base.bean.PageBean;
import cn.easy.xinjing.domain.VrRoomAppTask;
import cn.easy.xinjing.repository.VrRoomAppTaskDao;

@Component
public class VrRoomAppTaskService extends BaseService<VrRoomAppTask> {
    @Autowired
    private VrRoomAppTaskDao	vrRoomAppTaskDao;

    public Page<VrRoomAppTask> search(Map<String, Object> searchParams, PageBean pageBean) {
        return vrRoomAppTaskDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<VrRoomAppTask> search(Map<String, Object> searchParams) {
        return vrRoomAppTaskDao.findAll(spec(searchParams), new Sort(Direction.ASC, "createdAt"));
    }

    public List<VrRoomAppTask> findNotEnd(String userId) {
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("EQ_userId", userId);
        searchParams.put("IN_status", Constants.VR_ROOM_APP_TASK_STATUS_INIT + "," + Constants.VR_ROOM_APP_TASK_STATUS_PLAY);
        return search(searchParams);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        vrRoomAppTaskDao.delete(id);
    }

    public VrRoomAppTask getOne(String id) {
        return vrRoomAppTaskDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public VrRoomAppTask save(VrRoomAppTask vrRoomAppTask) {
        List<VrRoomAppTask> vrRoomAppTaskList = findNotEnd(vrRoomAppTask.getUserId());

        if(!vrRoomAppTaskList.isEmpty()) {
            VrRoomAppTask lastVrRoomAppTask = vrRoomAppTaskList.get(vrRoomAppTaskList.size() - 1);
            if(lastVrRoomAppTask.isPlayType()) {
                if(lastVrRoomAppTask.getStatus() == Constants.VR_ROOM_APP_TASK_STATUS_PLAY) {
                    VrRoomAppTask endVrRoomAppTask = new VrRoomAppTask();
                    endVrRoomAppTask.setType(Constants.VR_ROOM_APP_TASK_TYPE_END);
                    endVrRoomAppTask.setStatus(Constants.VR_ROOM_APP_TASK_STATUS_INIT);
                    endVrRoomAppTask.setUserId(vrRoomAppTask.getUserId());
                    endVrRoomAppTask.setContent(lastVrRoomAppTask.getContent());
                    vrRoomAppTaskDao.save(endVrRoomAppTask);
                }
            }
            //把播放中的更新为结束，把未播放的删除；
            for(VrRoomAppTask _task : vrRoomAppTaskList) {
                if(_task.getStatus() == Constants.VR_ROOM_APP_TASK_STATUS_PLAY) {
                    _task.setStatus( Constants.VR_ROOM_APP_TASK_STATUS_END);
                    vrRoomAppTaskDao.save(_task);
                }
                else {
                    vrRoomAppTaskDao.delete(_task);
                }
            }
        }
        return vrRoomAppTaskDao.save(vrRoomAppTask);
    }

    public void save(List<VrRoomAppTask> vrRoomAppTaskList) {
        vrRoomAppTaskDao.save(vrRoomAppTaskList);
    }
}


