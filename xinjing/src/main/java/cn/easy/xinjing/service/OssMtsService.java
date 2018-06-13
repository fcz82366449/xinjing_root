package cn.easy.xinjing.service;

import cn.easy.xinjing.bean.oss.OSSFileDO;
import com.aliyun.api.AliyunClient;
import com.aliyun.api.DefaultAliyunClient;
import com.aliyun.api.domain.*;
import com.aliyun.api.mts.mts20140618.request.*;
import com.aliyun.api.mts.mts20140618.response.*;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
import com.taobao.api.ApiException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;


/**
 * Created by lihe.lh on 2015/4/13.
 */
@Service
public class OssMtsService {
    private static String MTS_SERVER_URL = "http://mts.aliyuncs.com/";
    private static final String location = "oss-cn-hangzhou";

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.accessId}")
    private String accessKeyId;
    @Value("${aliyun.oss.accessKey}")
    private String accessKeySecret;
    @Value("${aliyun.oss.staticBucketName}")
    private String staticBucketName;
    @Value("${aliyun.oss.imgBucketName}")
    private String imgBucketName;
    @Value("${aliyun.oss.vodBucketName}")
    private String vodBucketName;
    @Value("${aliyun.oss.staticReturnServer}")
    private String staticReturnServer;
    @Value("${aliyun.oss.imgReturnServer}")
    private String imgReturnServer;
    @Value("${aliyun.oss.vodReturnServer}")
    private String vodReturnServer;
    @Value("${aliyun.oss.basicDir}")
    private String basicDir;
    @Value("${aliyun.mts.pipelineId}")
    private String pipelineId;
    @Value("${aliyun.mts.mp3TranscodeTemplateId}")
    private String mp3TranscodeTemplateId;
    @Value("${aliyun.mts.mp4TranscodeTemplateId}")
    private String mp4TranscodeTemplateId;
    @Value("${aliyun.mts.waterMarkTemplateId}")
    private String waterMarkTemplateId;

    private AliyunClient aliyunClient;

    private OSSClient ossClient;

    @PostConstruct
    private void initOssClient() {
        if(ossClient == null) {
            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        }
    }

    @PostConstruct
    private void initAliyunClient() {
        if(aliyunClient == null) {
            aliyunClient = new DefaultAliyunClient(MTS_SERVER_URL, accessKeyId, accessKeySecret);
        }
    }

    public OSSFileDO getVodOSSFileDO(String object){
        try {
            String encodedObjectName = URLEncoder.encode(object, "utf-8");
            return new OSSFileDO(location, vodBucketName, encodedObjectName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String uploadStatic(MultipartFile file) {
        if(file == null){
            return  "";
        }
        String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        String inOssKey = basicDir + System.currentTimeMillis();
        OSSFileDO ossFileDO = uploadLocalFile(staticBucketName, inOssKey + "." + extName, file);
        return staticReturnServer + ossFileDO.getObject();
    }

    public String uploadVod(MultipartFile file) {
        String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        String inOssKey = basicDir + System.currentTimeMillis();
        uploadLocalFile(vodBucketName, inOssKey + "." + extName, file);
        return inOssKey + "." + extName;
    }

    public String uploadImg(MultipartFile file) {
        String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        String inOssKey = basicDir + System.currentTimeMillis();
        uploadLocalFile(imgBucketName, inOssKey + "." + extName, file);
        return inOssKey + "." + extName;
    }

    /**
     * 获取音视频授权地址
     * @param key
     * @return
     */
    public String getVodAccessUrl(String key){
        // 设置URL过期时间为2小时
        Date expiration = new Date(new Date().getTime() + 7200 * 1000);

        // 生成URL
        URL url = ossClient.generatePresignedUrl(vodBucketName, key, expiration);
        String urlStr = url.toString();
        urlStr = vodReturnServer + urlStr.substring(urlStr.indexOf("com/")+4);
        return urlStr;
    }

    /**
     * 获取音频文件大小
     * @param key
     * @return
     */
    public Long getVodAccessSize(String key){
        long size = 0;
        if(StringUtils.isNotBlank(key)){
            OSSObject object = ossClient.getObject(vodBucketName, key);
            if(object!=null){
                size = object.getObjectMetadata()==null?0:object.getObjectMetadata().getContentLength();
            }
        }
        return  size;
    }

    /**
     * 获取图片授权地址
     * @param key
     * @return
     */
    public String getImgAccessUrl(String key){
        // 设置URL过期时间为2小时
        Date expiration = new Date(new Date().getTime() + 7200 * 1000);

        // 生成URL
        URL url = ossClient.generatePresignedUrl(imgBucketName, key, expiration);
        String urlStr = url.toString();
        urlStr = imgReturnServer + urlStr.substring(urlStr.indexOf("com/")+4);
        return urlStr;
    }

    public OSSFileDO uploadLocalFile(String bucket, String object, MultipartFile file){
        try {
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(file.getSize());
            ossClient.putObject(bucket, object, file.getInputStream(), meta);

            String encodedObjectName = URLEncoder.encode(object, "utf-8");
            return new OSSFileDO(location, bucket, encodedObjectName);
        } catch (Exception e) {
            throw new RuntimeException("fail@uploadLocalFile", e);
        }
    }

    public List<String> deleteObjects(List<String> keys, String bucketName) {
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
        deleteObjectsRequest.setKeys(keys);
        return ossClient.deleteObjects(deleteObjectsRequest).getDeletedObjects();
    }

    public void deleteObject(String key, String bucketName)  {
        ossClient.deleteObject(bucketName, key);
    }

    public List<String> deleteObjectsByURL(List<String> urls, String bucketName) {
        if (urls != null && urls.size() > 0) {
            List<String> keys = new ArrayList<>(urls.size());
            String key;
            for (String url : urls) {
                key = parseURL(url);
                if (key != null) {
                    keys.add(key);
                }
            }
            return deleteObjects(keys, bucketName);
        }
        return Collections.emptyList();
    }

    public void deleteObjectByURL(String url, String bucketName) {
        String key = parseURL(url);
        if (key != null) {
            deleteObject(key, bucketName);
        }
    }

    private String parseURL(String url) {
        if (StringUtils.isNotBlank(url) && url.contains(staticReturnServer)) {
            return url.replaceAll(staticReturnServer, "");
        }
        return null;
    }

    public void systemTemplateJobFlow(OSSFileDO inputFile, OSSFileDO watermarkFile, String outputOSSObjectKey) {
        String analysisJobId = submitAnalysisJob(inputFile, pipelineId);

        AnalysisJob analysisJob = waitAnalysisJobComplete(analysisJobId);

        List<String> templateIds = getSupportTemplateIds(analysisJob);

        // 可能会有多个系统模板，这里采用推荐的第一个系统模板进行转码
        String transcodeJobId = submitTranscodeJob(inputFile, watermarkFile, templateIds.get(0), outputOSSObjectKey);

        Job transcodeJob = waitTranscodeJobComplete(transcodeJobId);

        OSSFile outputFile  = transcodeJob.getOutput().getOutputFile();
        String outputFileOSSUrl = vodReturnServer + outputFile.getObject();
        System.out.println("Transcode success, the target file url is " + outputFileOSSUrl);
	}

    public String submitAnalysisJob(OSSFileDO inputFile, String pipelineId) {
        SubmitAnalysisJobRequest request = new SubmitAnalysisJobRequest();

        request.setInput(inputFile.toJsonString());
        JSONObject jsonObject = new JSONObject();
        JSONObject qualityControlJson = new JSONObject();
        qualityControlJson.put("RateQuality", "25");
        qualityControlJson.put("MethodStreaming", "network");
        jsonObject.put("QualityControl", qualityControlJson);
        request.setAnalysisConfig(jsonObject.toString());
        request.setPipelineId(pipelineId);

        try {
            SubmitAnalysisJobResponse response = aliyunClient.execute(request);
            if(!response.isSuccess()) {
                throw new RuntimeException("SubmitAnalysisJobRequest failed");
        	}
            return response.getAnalysisJob().getId();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
	}

    public AnalysisJob waitAnalysisJobComplete(String analysisJobId) {
        QueryAnalysisJobListRequest request = new QueryAnalysisJobListRequest();
        request.setAnalysisJobIds(analysisJobId);

        try{
            while (true) {
                QueryAnalysisJobListResponse response = aliyunClient.execute(request);
                if(!response.isSuccess()) {
                    throw new RuntimeException("QueryAnalysisJobListRequest failed");
            	}

                AnalysisJob analysisJob = response.getAnalysisJobList().get(0);
                String status = analysisJob.getState();
                if ("Fail".equals(status)) {
                    throw new RuntimeException("analysisJob state Failed");
                }
                if ("Success".equals(status)) {
                	return analysisJob;
                }

                Thread.sleep(5 * 1000);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
	}

    public List<String> getSupportTemplateIds(AnalysisJob analysisJob) {
        List<String> templateIds = new ArrayList<String>(analysisJob.getTemplateList().size());
        for (Template template : analysisJob.getTemplateList()) {
            templateIds.add(template.getId());
        }
        return templateIds;
	}

    public Job waitTranscodeJobComplete(String transcodeJobId) {
        QueryJobListRequest request = new QueryJobListRequest();
        request.setJobIds(transcodeJobId);

        try {
            while (true) {
                QueryJobListResponse response = aliyunClient.execute(request);
                if(!response.isSuccess()) {
                    throw new RuntimeException("QueryJobListRequest failed");
            	}

                Job transcodeJob = response.getJobList().get(0);
                String status = transcodeJob.getState();

                if ("TranscodeFail".equals(status)) {
                    throw new RuntimeException("transcodeJob state Failed");
                }

                if ("TranscodeSuccess".equals(status)) {
                    return transcodeJob;
                }

                Thread.sleep(5 * 1000);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String submitTranscodeJob(OSSFileDO inputFile, OSSFileDO watermarkFile, String templateId, String outputOSSObjectKey) {
        JSONObject jobConfig = new JSONObject();
        jobConfig.put("OutputObject", outputOSSObjectKey);
        jobConfig.put("TemplateId", templateId);

        if(watermarkFile != null) {
            JSONObject waterMarkConfig = new JSONObject();
            waterMarkConfig.put("InputFile", watermarkFile.toJson());
            waterMarkConfig.put("WaterMarkTemplateId", waterMarkTemplateId);
            JSONArray waterMarkConfigArray = new JSONArray();
            waterMarkConfigArray.add(waterMarkConfig);
            jobConfig.put("WaterMarks", waterMarkConfigArray);
        }

        JSONArray jobConfigArray = new JSONArray();
        jobConfigArray.add(jobConfig);

        SubmitJobsRequest request = new SubmitJobsRequest();
        request.setInput(inputFile.toJsonString());
        request.setOutputBucket(vodBucketName);
        request.setOutputs(jobConfigArray.toString());
        request.setPipelineId(pipelineId);

        Integer outputJobCount = 1;

        try {
            SubmitJobsResponse response = aliyunClient.execute(request);
            if(!response.isSuccess()) {
                throw new RuntimeException("SubmitJobsRequest failed");
            }
            if(response.getJobResultList().size() != outputJobCount) {
                throw new RuntimeException("SubmitJobsRequest Size failed");
            }

            return response.getJobResultList().get(0).getJob().getJobId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void transToMp3TemplateJobFlow(OSSFileDO inputFile, OSSFileDO watermarkFile, String outputOSSObjectKey){
        userCustomTemplateJobFlow(inputFile, watermarkFile, mp3TranscodeTemplateId, outputOSSObjectKey);
    }

    public void transToMp4TemplateJobFlow(OSSFileDO inputFile, OSSFileDO watermarkFile, String outputOSSObjectKey){
        userCustomTemplateJobFlow(inputFile, watermarkFile, mp4TranscodeTemplateId, outputOSSObjectKey);
    }


    public void userCustomTemplateJobFlow(OSSFileDO inputFile, OSSFileDO watermarkFile,String transCodeTemplateId, String outputOSSObjectKey) {
        String transcodeJobId = submitTranscodeJob(inputFile, watermarkFile, transCodeTemplateId, outputOSSObjectKey);
        System.out.println("transCode submit success, jobId = " + transcodeJobId);
//        Job transcodeJob = waitTranscodeJobComplete(transcodeJobId);
//
//        OSSFile outputFile  = transcodeJob.getOutput().getOutputFile();
//        String outputFileOSSUrl = vodReturnServer + outputFile.getObject();
//        System.out.println("Transcode success, the target file url is " + outputFileOSSUrl);

	}

    public void snapshotJobFlow(OSSFileDO inputFile, String outputSnapshotKey) {
        String snapshotJobId = submitSnapshotJob(inputFile, outputSnapshotKey);
        System.out.println("snapshot submit success, jobId = " + snapshotJobId);
//        SnapshotJob job = waitSnapshotComplete(snapshotJobId);
//
//        OSSFile outputFile  = job.getSnapshotConfig().getOutputFile();
//        String outputFileOSSUrl = staticReturnServer + outputFile.getObject();
//        System.out.println("snapshot success, the target file url is " + outputFileOSSUrl);
    }

    public String submitSnapshotJob(OSSFileDO inputFile, String outputSnapshotKey){
        OSSFileDO outputSnapshotFile = new OSSFileDO();
        outputSnapshotFile.setBucket(staticBucketName);
        outputSnapshotFile.setLocation(location);
        outputSnapshotFile.setObject(outputSnapshotKey);

        JSONObject jobConfig = new JSONObject();
        jobConfig.put("OutputFile", outputSnapshotFile.toJson());
        jobConfig.put("Time", 1000L);   //snapshot time by ms

        SubmitSnapshotJobRequest request = new SubmitSnapshotJobRequest();
        request.setSnapshotConfig(jobConfig.toString());
        request.setInput(inputFile.toJsonString());

        try {
            SubmitSnapshotJobResponse response = aliyunClient.execute(request);
            if(!response.isSuccess()) {
                throw new RuntimeException("SubmitSnapshotJobRequest failed");
        	}

            return response.getSnapshotJob().getId();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    public SnapshotJob waitSnapshotComplete(String snapshotJobId){
        QuerySnapshotJobListRequest request = new QuerySnapshotJobListRequest();
        request.setSnapshotJobIds(snapshotJobId);

        try {
            while (true){
                QuerySnapshotJobListResponse response = aliyunClient.execute(request);
                if(!response.isSuccess()) {
                    throw new RuntimeException("QuerySnapshotJobListRequest failed");
            	}

                SnapshotJob job = response.getSnapshotJobList().get(0);
                String status = job.getState();

                if ("Fail".equals(status)) {
                    throw new RuntimeException("QuerySnapshotJobListRequest Failed");
                }
                if ("Success".equals(status)) {
                    return job;
                }

                Thread.sleep(2 * 1000);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> getPostObjectPolicy(String type) throws UnsupportedEncodingException {
        long expireTime = 30;
        long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
        java.sql.Date expiration = new java.sql.Date(expireEndTime);
        PolicyConditions policyConds = new PolicyConditions();
//        policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
        policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, basicDir);

        String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
        byte[] binaryData = postPolicy.getBytes("utf-8");
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        String postSignature = ossClient.calculatePostSignature(postPolicy);

        Map<String, String> respMap = new LinkedHashMap<String, String>();
        respMap.put("accessid", accessKeyId);
        respMap.put("policy", encodedPolicy);
        respMap.put("signature", postSignature);
        respMap.put("dir", basicDir);
        if("VOD".equals(type)){
            respMap.put("host", "http://" + vodBucketName + ".oss-cn-hangzhou.aliyuncs.com/");
        }else if("IMG".equals(type)){
            respMap.put("host", "http://" + imgBucketName + ".oss-cn-hangzhou.aliyuncs.com/");
        }else {
            respMap.put("host", "http://" + staticBucketName + ".oss-cn-hangzhou.aliyuncs.com/");
        }
        respMap.put("expire", String.valueOf(expireEndTime / 1000));
        return respMap;
    }
}
