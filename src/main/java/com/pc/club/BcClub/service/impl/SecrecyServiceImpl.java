package com.pc.club.BcClub.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pc.club.BcClub.service.SecrecyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author jyWei
 * @Version V1.0.0
 * @Created by jinYan
 * @Date 2022/5/12
 */
@Service
@Slf4j
public class SecrecyServiceImpl implements SecrecyService {

    // 查询资源目录
    private static final String getCourseDirectoryListUrl = "http://www.baomi.org.cn/portal/api/v2/coursePacket/getCourseDirectoryList?" +
            "scale=1&coursePacketId=fc5489db-34c7-4db1-a856-96d501ea5a78&timestamps={timestamps}";
    // 查询资源详情
    private static final String getCourseResourceListUrl  = "http://www.baomi.org.cn/portal/api/v2/coursePacket/getCourseResourceList?" +
            "coursePacketId={coursePacketId}&directoryId={directoryId}&token={token}&timestamps=1652329838847";

    private static final String saveCoursePackageUrl = "http://www.baomi.org.cn/portal/api/v2/studyTime/saveCoursePackage.do?" +
            "courseId={courseId}&resourceId={resourceId}&resourceDirectoryId={resourceDirectoryId}&resourceLength={resourceLength}" +
            "&studyLength={studyLength}&studyTime={studyTime}&startTime={startTime}&resourceName={resourceName}&resourceType={resourceType}" +
            "&resourceLibId={resourceLibId}&studyResourceId={studyResourceId}&token={token}&timestamps={timestamps}";

    private static final String url1 = "http://www.baomi.org.cn/portal/api/v2/coursePacket/getCourseResourceList?" +
            "coursePacketId=fc5489db-34c7-4db1-a856-96d501ea5a78" +
            "&directoryId=576b1cb9-820f-4866-baf8-430c072158ff" +
            "&token={token}" +
            "&timestamps={timestamps}";

    private static final String url2 = "http://www.baomi.org.cn/portal/api/v2/coursePacket/getCourseResourceList?" +
            "coursePacketId=fc5489db-34c7-4db1-a856-96d501ea5a78" +
            "&directoryId=0feca8e9-ee84-474f-96b7-22d6499e4dc4" +
            "&token={token}" +
            "&timestamps={timestamps}";

    private static final String getAnswerUrl = "http://www.baomi.org.cn/portal/api/v2/activity/exam/getExamContentData.do?examId={examId}&timestamps={timestamps}";

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private Gson gson;

    /**
     * 根据token获取全部的resource
     *
     * @param token token
     */
    @Override
    public void insertResource(String token, Boolean special) {
        long start = System.currentTimeMillis();
        if (special) {
            log.info("url:{}", getCourseDirectoryListUrl);
            String directoryUrl = getCourseDirectoryListUrl.replace("{timestamps}", String.valueOf(System.currentTimeMillis()));
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(directoryUrl, String.class);
            JsonObject response = gson.fromJson(responseEntity.getBody(), JsonObject.class);
            response.get("data").getAsJsonArray().forEach(e -> {
                log.info(gson.toJson(e));
                JsonObject jsonObject = e.getAsJsonObject();
                String coursePacketID = jsonObject.get("coursePacketID").getAsString();
                if (jsonObject.has("subDirectory") && null != jsonObject.get("subDirectory")) {
                    JsonArray subDirectory = jsonObject.get("subDirectory").getAsJsonArray();
                    subDirectory.forEach(directory -> {
                        JsonObject directoryJson = directory.getAsJsonObject();
                        String directoryId = directoryJson.get("SYS_UUID").getAsString();
                        String courseResourceListUrl = getCourseResourceListUrl.replace("{coursePacketId}", coursePacketID)
                            .replace("{directoryId}", directoryId).replace("{token}", token)
                            .replace("{timestamps}", String.valueOf(System.currentTimeMillis()));
                        getCourseResourceList(courseResourceListUrl, token);
                    });
                }
            });
        }
        log.info("现在开始执行特殊的任务");
        String specialUrl = url1.replace("{token}", token).replace("{timestamps}", String.valueOf(System.currentTimeMillis()));
        String specialUr2 = url2.replace("{token}", token).replace("{timestamps}", String.valueOf(System.currentTimeMillis()));
        getCourseResourceList(specialUrl, token);
        getCourseResourceList(specialUr2, token);
        log.info("全部任务执行完毕~,耗时:{}ms", System.currentTimeMillis() - start);
    }

    /**
     * 获取答案 8ad581948051551b01805431f9f7018c
     *
     * @param examId examId
     */
    @Override
    public String getAnswer(String examId) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(getAnswerUrl.replace("{examId}", examId)
            .replace("{timestamps}", String.valueOf(System.currentTimeMillis())), String.class);
        JsonObject response = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        Map<String, Map<String, String>> answerMap = new HashMap<>();
        List<String> stringList = new ArrayList<>();
        response.get("data").getAsJsonObject().get("typeList").getAsJsonArray().forEach(e -> {
            JsonObject questionJson = e.getAsJsonObject();
            String typeName = questionJson.get("typeName").getAsString();
            Map<String, String> typeAnswerMap = new HashMap<>();
            answerMap.put(typeName, typeAnswerMap);
            questionJson.get("questionList").getAsJsonArray().forEach(question -> {
                JsonObject ques = question.getAsJsonObject();
                typeAnswerMap.put(ques.get("content").getAsString().replace("<XHTML xmlns:mml=\\\\\"http://www.w3.org/1998/Math/MathML\\\\\" align=\\\\\"center\\\\\">", ""), ques.get("answer").getAsString());
                stringList.add(ques.get("answer").getAsString());
            });
        });
        String result = gson.toJson(answerMap).replace("\"", "");
        log.info(result);
        log.info(gson.toJson(stringList));
        return result;
    }

    /**
     * 获取每个标题下的全部视频或音频
     *
     * @param url url
     * @param token token
     */
    private void getCourseResourceList(String url, String token) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        JsonObject response = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        response.get("data").getAsJsonObject().get("listdata").getAsJsonArray().forEach(listData -> {
            JsonObject data = listData.getAsJsonObject();
            String startTime = String.valueOf(System.currentTimeMillis());
            String timeLength = calTimeLength(data.get("timeLength").getAsString());
            String endTime = String.valueOf(Long.parseLong(startTime) + Long.parseLong(timeLength) * 1000);
            String saveUrl = saveCoursePackageUrl.replace("{courseId}", data.get("coursePacketID").getAsString())
                .replace("{resourceId}", data.get("resourceID").getAsString())
                .replace("{resourceDirectoryId}", data.get("SYS_UUID").getAsString())
                .replace("{resourceLength}", timeLength)
                .replace("{studyTime}", timeLength)
                .replace("{resourceName}", data.get("name").getAsString())
                .replace("{resourceType}", data.get("resourceType").getAsString())
                .replace("{resourceLibId}", data.get("SYS_DOCLIBID").getAsString())
                .replace("{studyResourceId}", data.get("SYS_DOCUMENTID").getAsString())
                .replace("{token}", token);
            String startUrl = saveUrl.replace("{studyLength}", "0").replace("{timestamps}", startTime).replace("{startTime}", startTime);
            ResponseEntity<String> startSaveResult = restTemplate.getForEntity(startUrl, String.class);
            log.info("开始任务执行完成，" + data.get("name").getAsString() + ": startSaveResult: " + startSaveResult.getBody());
            try {
                log.info("线程睡眠1秒再执行结束任务");
                Thread.sleep(1000 * 1);
            } catch (Exception e) {
                log.info(e.getMessage());
            }
            String endUrl = saveUrl.replace("{studyLength}", timeLength).replace("{timestamps}", endTime).replace("{startTime}", startTime);
            ResponseEntity<String> endSaveResult = restTemplate.getForEntity(endUrl, String.class);
            log.info("endUrl:{}", endUrl);
            log.info("结束任务执行完成，" + data.get("name").getAsString() + ": endSaveResult: " + endSaveResult.getBody());
        });
    }

    private String calTimeLength(String time) {
        int result = 60*60;
        String[] timeArray = time.split(":");
        if (timeArray.length == 3) {
            result = Integer.parseInt(timeArray[0])*60*60 + Integer.parseInt(timeArray[1])*60 + Integer.parseInt(timeArray[2]);
        }
        log.info("timeLength:{}s", result);
        return Integer.toString(result);
    }
}
