package cn.easy.xinjing.bean.api;

import cn.easy.xinjing.domain.SingleselRecord;

import java.util.List;

/**
 * Created by Lenovo on 2017/8/15.
 */
public class SingleselRecordSaveBean extends ApiBaseBean {
    private List<SingleselRecord> singleselRecords;

    public List<SingleselRecord> getSingleselRecords() {
        return singleselRecords;
    }

    public void setSingleselRecords(List<SingleselRecord> singleselRecords) {
        this.singleselRecords = singleselRecords;
    }
    public SingleselRecordSaveBean(List<SingleselRecord> singleselRecords){
        super();
        this.singleselRecords = singleselRecords;
    }
    public SingleselRecordSaveBean(){
        super();
    }
}
