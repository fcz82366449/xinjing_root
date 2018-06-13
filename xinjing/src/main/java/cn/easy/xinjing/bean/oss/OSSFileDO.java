package cn.easy.xinjing.bean.oss;

import net.sf.json.JSONObject;

/**
 * Created by caoshengkang on 2016/6/21.
 */
public class OSSFileDO {

    private String location;
    private String bucket;
    private String object;

    public OSSFileDO(){
    }

    public OSSFileDO(String location, String bucket, String object){
        this.location = location;
        this.bucket = bucket;
        this.object = object;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String toJsonString(){
        return toJson().toString();
    }

    public JSONObject toJson(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Bucket", bucket);
        jsonObject.put("Location", location);
        jsonObject.put("Object", object);

        return jsonObject;
    }

    public String getLocation() {
        return location;
    }

    public String getBucket() {
        return bucket;
    }

    public String getObject() {
        return object;
    }
}
