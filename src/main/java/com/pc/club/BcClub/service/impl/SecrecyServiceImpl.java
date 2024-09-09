package com.pc.club.BcClub.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pc.club.BcClub.service.SecrecyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

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
    private static final String getCourseDirectoryListUrl = "https://www.baomi.org.cn/portal/main-api/v2/coursePacket/getCourseDirectoryList?" +
            "scale=1&coursePacketId=78e6a04c-dd87-4794-8214-9de32be7cae1&Authtoken={Authtoken}&Cookie={Cookie}&timestamps={timestamps}";

    // 查询资源详情
    private static final String getCourseResourceListUrl  = "https://www.baomi.org.cn/portal/main-api/v2/coursePacket/getCourseResourceList?" +
            "coursePacketId={coursePacketId}&directoryId={directoryId}&token={token}&Authtoken={Authtoken}&Cookie={Cookie}&timestamps=1723126358216";

    private static final String saveCoursePackageUrl = "https://www.baomi.org.cn/portal/main-api/v2/studyTime/saveCoursePackage.do?" +
            "courseId={courseId}&resourceId={resourceId}&resourceDirectoryId={resourceDirectoryId}&resourceLength={resourceLength}" +
            "&studyLength={studyLength}&studyTime={studyTime}&startTime={startTime}&resourceType={resourceType}" +
            "&resourceLibId={resourceLibId}&studyResourceId={studyResourceId}&token={token}&timestamps={timestamps}";

    private Set<String> urlList = new HashSet<>();
//
//    private static final String url1 = "http://www.baomi.org.cn/portal/api/v2/coursePacket/getCourseResourceList?" +
//            "coursePacketId=fc5489db-34c7-4db1-a856-96d501ea5a78" +
//            "&directoryId=576b1cb9-820f-4866-baf8-430c072158ff" +
//            "&token={token}" +
//            "&timestamps={timestamps}";
//
//    private static final String url2 = "http://www.baomi.org.cn/portal/api/v2/coursePacket/getCourseResourceList?" +
//            "coursePacketId=fc5489db-34c7-4db1-a856-96d501ea5a78" +
//            "&directoryId=0feca8e9-ee84-474f-96b7-22d6499e4dc4" +
//            "&token={token}" +
//            "&timestamps={timestamps}";

    private static final String getAnswerUrl = "http://www.baomi.org.cn/portal/api/v2/activity/exam/getExamContentData.do?examId={examId}&timestamps={timestamps}";

    String Authtoken = "3613146b9c1a486b8666e86a78298a1a";
    String Cookie = "selectedStreamLevel=FD; acw_tc=2760822e17231245746754141e2af53b0c67e0befdfeb8a6b292150d5dc32c; qimo_seosource_0=%E5%85%B6%E4%BB%96%E7%BD%91%E7%AB%99; qimo_seokeywords_0=%E6%9C%AA%E7%9F%A5; uuid_c13af0e0-064c-11ed-a8a8-c33dc9621234=f10a86dd-5a66-46e3-91a5-9275f31e82fc; qimo_seosource_c13af0e0-064c-11ed-a8a8-c33dc9621234=%E5%85%B6%E4%BB%96%E7%BD%91%E7%AB%99; qimo_seokeywords_c13af0e0-064c-11ed-a8a8-c33dc9621234=%E6%9C%AA%E7%9F%A5; qimo_xstKeywords_c13af0e0-064c-11ed-a8a8-c33dc9621234=; href=https%3A%2F%2Fwww.baomi.org.cn%2FbmCourseDetail%2Finfo%3Fid%3D78e6a04c-dd87-4794-8214-9de32be7cae1%26index%3D2%26docId%3D23466405%26docLibId%3D-15%26productId%3D2263%26pubId%3D35726%26siteId%3D95; accessId=c13af0e0-064c-11ed-a8a8-c33dc9621234; pageViewNum=1";
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
    public Set<String> insertResource(String token, Boolean special) {
        long start = System.currentTimeMillis();
        HttpHeaders httpHeaders = new HttpHeaders();
                MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        HttpEntity<Object> requestEntity = new HttpEntity<>(map, httpHeaders);
        if (special) {

            String directoryUrl = getCourseDirectoryListUrl
                    .replace("{Authtoken}", Authtoken)
                    .replace("{Cookie}", Cookie)
                    .replace("{timestamps}", String.valueOf(System.currentTimeMillis()));
            log.info("url:{}", directoryUrl);
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(directoryUrl, String.class, requestEntity);
            JsonObject response = gson.fromJson(responseEntity.getBody(), JsonObject.class);
            response.get("data").getAsJsonArray().forEach(e -> {
//                log.info(gson.toJson(e));
                JsonObject jsonObject = e.getAsJsonObject();
                String coursePacketID = jsonObject.get("coursePacketID").getAsString();
                if (jsonObject.has("subDirectory") && null != jsonObject.get("subDirectory")) {
                    JsonArray subDirectory = jsonObject.get("subDirectory").getAsJsonArray();
                    subDirectory.forEach(directory -> {
                        JsonObject directoryJson = directory.getAsJsonObject();
                        String directoryId = directoryJson.get("SYS_UUID").getAsString();
                        String courseResourceListUrl = getCourseResourceListUrl
                                .replace("{Authtoken}", Authtoken)
                                .replace("{Cookie}", Cookie)
                                .replace("{Authtoken}", Authtoken)
                                .replace("{Cookie}", Cookie).replace("{coursePacketId}", coursePacketID)
                            .replace("{directoryId}", directoryId).replace("{token}", token)
                            .replace("{timestamps}", String.valueOf(System.currentTimeMillis()));
                        getCourseResourceList(courseResourceListUrl, token);
                    });
                }
            });
        }
        log.info("现在开始执行特殊的任务");
//        String specialUrl = url1.replace("{token}", token).replace("{timestamps}", String.valueOf(System.currentTimeMillis()));
//        String specialUr2 = url2.replace("{token}", token).replace("{timestamps}", String.valueOf(System.currentTimeMillis()));
//        getCourseResourceList(specialUrl, token);
//        getCourseResourceList(specialUr2, token);
        log.info("全部任务执行完毕~,耗时:{}ms", System.currentTimeMillis() - start);
        log.info(new Gson().toJson(urlList));
        return urlList;
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
//                .replace("{resourceName}", data.get("name").getAsString())
                .replace("{resourceType}", data.get("resourceType").getAsString())
                .replace("{resourceLibId}", data.get("SYS_DOCLIBID").getAsString())
                .replace("{studyResourceId}", data.get("SYS_DOCUMENTID").getAsString())
                .replace("{token}", token).replace("{Authtoken}", Authtoken)
                    .replace("{Cookie}", Cookie);;
            String startUrl = saveUrl.replace("{studyLength}", "0").replace("{timestamps}", startTime).replace("{startTime}", startTime)
                    .replace("{Authtoken}", Authtoken)
                    .replace("{Cookie}", Cookie);
            log.info("startUrl: " + startUrl);
            urlList.add(startUrl);
            log.info("startUrl: " + startUrl);
            HttpHeaders httpHeaders = setHeader();
            HttpEntity<JSONObject> httpEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<String> startSaveResult = restTemplate.getForEntity(startUrl, String.class);
            ResponseEntity<String> exchange = restTemplate.exchange(startUrl, HttpMethod.GET, httpEntity, String.class);
            log.info("start: " + exchange);
            log.info("开始任务执行完成，" + data.get("name").getAsString() + ": startSaveResult: " + startSaveResult.getBody());
            try {
                log.info("线程睡眠1秒再执行结束任务");
                Thread.sleep(1000);
            } catch (Exception e) {
                log.info(e.getMessage());
            }
            String endUrl = saveUrl.replace("{studyLength}", timeLength).replace("{timestamps}", endTime).replace("{startTime}", startTime);
            exchange = restTemplate.exchange(endUrl, HttpMethod.GET, httpEntity, String.class);
            log.info("end: " + exchange);
            ResponseEntity<String> endSaveResult = restTemplate.getForEntity(endUrl, String.class);
            log.info("endUrl:{}", endUrl);
            urlList.add(endUrl);
            log.info("endUrl: " + endUrl);
            log.info("结束任务执行完成，" + data.get("name").getAsString() + ": endSaveResult: " + endSaveResult.getBody());
        });
    }

    private String calTimeLength(String time) {
        int result = 60*60;
        String[] timeArray = time.split(":");
        if (timeArray.length == 3) {
            result = Integer.parseInt(timeArray[0])*60*60 + Integer.parseInt(timeArray[1])*60 + Integer.parseInt(timeArray[2]);
        }
//        log.info("timeLength:{}s", result);
        return Integer.toString(result);
    }

    private HttpHeaders setHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();

        String json = "[\n" +
                "            {\n" +
                "              \"name\": \"Accept\",\n" +
                "              \"value\": \"application/json, text/plain, */*\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"Accept-Encoding\",\n" +
                "              \"value\": \"gzip, deflate\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"Accept-Language\",\n" +
                "              \"value\": \"zh-CN,zh;q=0.9\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"Connection\",\n" +
                "              \"value\": \"keep-alive\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"Cookie\",\n" +
                "              \"value\": \"p_h5_u=9EA254F3-31D5-4534-AEC3-0DE54D7AFA87; accessId=c13af0e0-064c-11ed-a8a8-c33dc9621234; selectedStreamLevel=FD; acw_tc=2760775616921090181365327e9f63910b2a9246659314c09a30436d640e72; acw_sc__v3=64db88db9f775bc63a04c0aa7805e902c8bd638b; qimo_seosource_0=%E7%AB%99%E5%86%85; qimo_seokeywords_0=; uuid_c13af0e0-064c-11ed-a8a8-c33dc9621234=6aa37c08-b5c4-4993-af0d-627347eafd7e; qimo_seosource_c13af0e0-064c-11ed-a8a8-c33dc9621234=%E7%AB%99%E5%86%85; qimo_seokeywords_c13af0e0-064c-11ed-a8a8-c33dc9621234=; qimo_xstKeywords_c13af0e0-064c-11ed-a8a8-c33dc9621234=; href=http%3A%2F%2Fwww.baomi.org.cn%2F%3Fu_atoken%3Dd7cbde9a-4fa8-479a-b6b6-7a17482aa9d4%26u_asession%3D01Rn4IJnzpIawfbs6RtvqiT8P_Oz4oQ8_HWSqZptiE_w7jqlE5wnTlBMysIT3IxAiPX0KNBwm7Lovlpxjd_P_q4JsKWYrT3W_NKPr8w6oU7K_Iv9a4BWiNksV1d1cNkg4ioqYap4IcfpCWBPy06QojUWBkFo3NEHBv0PZUm6pbxQU%26u_asig%3D05hxbw812eS2ElatLElaMrwvg5y96ngkd13TjZIZwenEpXj_tsZkhyyG7_QxBmjdBX3VqjZZe5feL3PLSqm80NN47FBU_yUNPtxoMXWwQjYcXfJDB7VY5_-d2nBmNr6Rl2jMh9LOXVUiBYy1oK3dUDw6RVvh8yU8P8ftR0PJui0hj9JS7q8ZD7Xtz2Ly-b0kmuyAKRFSVJkkdwVUnyHAIJzfoaymtl3sEqfx9upMRtCuB63DmavDd1qX77jLRL1ayyXlXp3azFSZNAdq4krLXsk-3h9VXwMyh6PgyDIVSG1W_7u2j_PyxKnkuM7wi_-FVugM7ydBJ76cLfYnAuPfmtgnXnFZinM9nyclT7Sc25foBmi_blPv9Q1MxBGFhVrWZLmWspDxyAEEo4kbsryBKb9Q%26u_aref%3DVK3TV3F%252FRcF65NBcsUJI5aYfN6k%253D%26siteId%3D95; pageViewNum=15\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"Host\",\n" +
                "              \"value\": \"www.baomi.org.cn\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"User-Agent\",\n" +
                "              \"value\": \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"authToken\",\n" +
                "              \"value\": \"3613146b9c1a486b8666e86a78298a1a\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"siteId\",\n" +
                "              \"value\": \"95\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"token\",\n" +
                "              \"value\": \"28fed03099ce4f28912ab09d79758a93\"\n" +
                "            }\n" +
                "          ]";

        JsonArray response = gson.fromJson(json, JsonArray.class);
        response.forEach(e -> {
            String name = e.getAsJsonObject().get("name").getAsString();
            String value = e.getAsJsonObject().get("value").getAsString();
            httpHeaders.set(name, value);
        });
        addCookie(httpHeaders);
        return httpHeaders;
    }


    private void addCookie(HttpHeaders httpHeaders) {
        String json = "[\n" +
                "            {\n" +
                "              \"name\": \"p_h5_u\",\n" +
                "              \"value\": \"9EA254F3-31D5-4534-AEC3-0DE54D7AFA87\",\n" +
                "              \"path\": \"/\",\n" +
                "              \"domain\": \"www.baomi.org.cn\",\n" +
                "              \"expires\": \"2024-05-11T01:56:37.000Z\",\n" +
                "              \"httpOnly\": false,\n" +
                "              \"secure\": false\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"accessId\",\n" +
                "              \"value\": \"c13af0e0-064c-11ed-a8a8-c33dc9621234\",\n" +
                "              \"path\": \"/\",\n" +
                "              \"domain\": \"www.baomi.org.cn\",\n" +
                "              \"expires\": \"2023-08-16T14:20:51.000Z\",\n" +
                "              \"httpOnly\": false,\n" +
                "              \"secure\": false\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"selectedStreamLevel\",\n" +
                "              \"value\": \"FD\",\n" +
                "              \"path\": \"/\",\n" +
                "              \"domain\": \"www.baomi.org.cn\",\n" +
                "              \"expires\": \"2024-08-13T11:02:33.000Z\",\n" +
                "              \"httpOnly\": false,\n" +
                "              \"secure\": false\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"acw_tc\",\n" +
                "              \"value\": \"2760775616921090181365327e9f63910b2a9246659314c09a30436d640e72\",\n" +
                "              \"path\": \"/\",\n" +
                "              \"domain\": \"www.baomi.org.cn\",\n" +
                "              \"expires\": \"2023-08-15T14:46:58.228Z\",\n" +
                "              \"httpOnly\": true,\n" +
                "              \"secure\": false\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"acw_sc__v3\",\n" +
                "              \"value\": \"64db88db9f775bc63a04c0aa7805e902c8bd638b\",\n" +
                "              \"path\": \"/\",\n" +
                "              \"domain\": \"www.baomi.org.cn\",\n" +
                "              \"expires\": \"2023-08-15T14:47:00.040Z\",\n" +
                "              \"httpOnly\": true,\n" +
                "              \"secure\": false\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"qimo_seosource_0\",\n" +
                "              \"value\": \"%E7%AB%99%E5%86%85\",\n" +
                "              \"path\": \"/\",\n" +
                "              \"domain\": \"www.baomi.org.cn\",\n" +
                "              \"expires\": \"2023-08-15T14:50:51.000Z\",\n" +
                "              \"httpOnly\": false,\n" +
                "              \"secure\": false\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"qimo_seokeywords_0\",\n" +
                "              \"value\": \"\",\n" +
                "              \"path\": \"/\",\n" +
                "              \"domain\": \"www.baomi.org.cn\",\n" +
                "              \"expires\": \"2023-08-15T14:50:51.000Z\",\n" +
                "              \"httpOnly\": false,\n" +
                "              \"secure\": false\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"uuid_c13af0e0-064c-11ed-a8a8-c33dc9621234\",\n" +
                "              \"value\": \"6aa37c08-b5c4-4993-af0d-627347eafd7e\",\n" +
                "              \"path\": \"/\",\n" +
                "              \"domain\": \"www.baomi.org.cn\",\n" +
                "              \"expires\": \"2023-08-16T14:17:00.000Z\",\n" +
                "              \"httpOnly\": false,\n" +
                "              \"secure\": false\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"qimo_seosource_c13af0e0-064c-11ed-a8a8-c33dc9621234\",\n" +
                "              \"value\": \"%E7%AB%99%E5%86%85\",\n" +
                "              \"path\": \"/\",\n" +
                "              \"domain\": \"www.baomi.org.cn\",\n" +
                "              \"expires\": \"2023-08-15T14:50:51.000Z\",\n" +
                "              \"httpOnly\": false,\n" +
                "              \"secure\": false\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"qimo_seokeywords_c13af0e0-064c-11ed-a8a8-c33dc9621234\",\n" +
                "              \"value\": \"\",\n" +
                "              \"path\": \"/\",\n" +
                "              \"domain\": \"www.baomi.org.cn\",\n" +
                "              \"expires\": \"2023-08-15T14:50:51.000Z\",\n" +
                "              \"httpOnly\": false,\n" +
                "              \"secure\": false\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"qimo_xstKeywords_c13af0e0-064c-11ed-a8a8-c33dc9621234\",\n" +
                "              \"value\": \"\",\n" +
                "              \"path\": \"/\",\n" +
                "              \"domain\": \"www.baomi.org.cn\",\n" +
                "              \"expires\": \"2023-08-15T14:50:51.000Z\",\n" +
                "              \"httpOnly\": false,\n" +
                "              \"secure\": false\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"href\",\n" +
                "              \"value\": \"http%3A%2F%2Fwww.baomi.org.cn%2F%3Fu_atoken%3Dd7cbde9a-4fa8-479a-b6b6-7a17482aa9d4%26u_asession%3D01Rn4IJnzpIawfbs6RtvqiT8P_Oz4oQ8_HWSqZptiE_w7jqlE5wnTlBMysIT3IxAiPX0KNBwm7Lovlpxjd_P_q4JsKWYrT3W_NKPr8w6oU7K_Iv9a4BWiNksV1d1cNkg4ioqYap4IcfpCWBPy06QojUWBkFo3NEHBv0PZUm6pbxQU%26u_asig%3D05hxbw812eS2ElatLElaMrwvg5y96ngkd13TjZIZwenEpXj_tsZkhyyG7_QxBmjdBX3VqjZZe5feL3PLSqm80NN47FBU_yUNPtxoMXWwQjYcXfJDB7VY5_-d2nBmNr6Rl2jMh9LOXVUiBYy1oK3dUDw6RVvh8yU8P8ftR0PJui0hj9JS7q8ZD7Xtz2Ly-b0kmuyAKRFSVJkkdwVUnyHAIJzfoaymtl3sEqfx9upMRtCuB63DmavDd1qX77jLRL1ayyXlXp3azFSZNAdq4krLXsk-3h9VXwMyh6PgyDIVSG1W_7u2j_PyxKnkuM7wi_-FVugM7ydBJ76cLfYnAuPfmtgnXnFZinM9nyclT7Sc25foBmi_blPv9Q1MxBGFhVrWZLmWspDxyAEEo4kbsryBKb9Q%26u_aref%3DVK3TV3F%252FRcF65NBcsUJI5aYfN6k%253D%26siteId%3D95\",\n" +
                "              \"path\": \"/\",\n" +
                "              \"domain\": \"www.baomi.org.cn\",\n" +
                "              \"expires\": \"1969-12-31T23:59:59.000Z\",\n" +
                "              \"httpOnly\": false,\n" +
                "              \"secure\": false\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"pageViewNum\",\n" +
                "              \"value\": \"15\",\n" +
                "              \"path\": \"/\",\n" +
                "              \"domain\": \"www.baomi.org.cn\",\n" +
                "              \"expires\": \"2023-08-16T14:17:39.000Z\",\n" +
                "              \"httpOnly\": false,\n" +
                "              \"secure\": false\n" +
                "            }\n" +
                "          ]";
        JsonArray jsonArray = gson.fromJson(json, JsonArray.class);
        List<String> cookies = new ArrayList<>();
        jsonArray.forEach(e -> {
            String name = e.getAsJsonObject().get("name").getAsString();
            String value = e.getAsJsonObject().get("value").getAsString();
            cookies.add(value);
        });

        httpHeaders.put(HttpHeaders.COOKIE, cookies);
    }





}
