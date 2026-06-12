package com.xf.tools;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.stec.utils.ObjectUtils;
import com.stec.utils.TimeUtil;
import com.xf.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

/**
 * @author tanjunjie
 * @version 1.0
 * @className StecTools
 * @date 2026/5/13 17:42
 * @description TODO
 */
@Slf4j
@EnableScheduling
@SpringBootTest
public class StecTools {

    /**
     * 身份证实名认证
     */
    @Test
    void testIdCardVerify() {
        String host = "https://kzidcardv1.market.alicloudapi.com";
        String path = "/api-mall/api/id_card/check";
        String method = "POST";
        String appcode = "c226da3be6f248ce8212b1db251cbc95";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>();
        Map<String, String> bodys = new HashMap<>();

        bodys.put("name", "彭飞");
        bodys.put("idcard", "411123199105213511");


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            if (ObjectUtils.isNotNull(response) && ObjectUtils.isNotNull(response.getEntity())) {
                String json = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = JSONObject.parseObject(json);
                if (ObjectUtils.isNotNull(jsonObject)) {
                    Integer code = jsonObject.getInteger("code");
                    if (!ObjectUtils.notEqual(code, 200)) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        Integer result = data.getInteger("result");
                        if (!ObjectUtils.notEqual(result, 0)) {
                            System.out.println("IDCardValidator.身份证实名认证通过！");
                        }
                    } else {
                        log.warn("IDCardValidator.身份证实名认证不通过！: {}", json);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 模拟监测数据推送
     */
    @Test
    void getMonitorTestData() {

        List<Long> psnList = ListUtils.newArrayList(1603075739436001L);

        for (Long psn : psnList) {

            Date date = DateUtil.parseDateTime("2025-08-19 17:00:00");
            int value3001 = 10;//TSP
            int value3002 = 20;//噪音
            int value3003 = 30;//温度
            int value3004 = 40;//湿度
            int value3005 = 50;//风速
            int value3006 = 60;//风向
            while (new Date().after(date)) {
                JSONObject jsonObject = new JSONObject();
                JSONArray array = new JSONArray();
                date = TimeUtil.addMinutes(date, 5);
                JSONObject psnJSON = new JSONObject();

                psnJSON.put("psn", psn);
                psnJSON.put("time", date.getTime());

                Random random = new Random();
                int randomInteger = random.nextInt(5);
                boolean flag = randomInteger == 1;
                JSONObject dataJSON = new JSONObject();
//                dataJSON.put("3001", String.valueOf(value3001));
//                dataJSON.put("3002", String.valueOf(flag ? value3002++ : value3002));
//                dataJSON.put("3003", String.valueOf(flag ? value3003++ : value3003));
//                dataJSON.put("3004", String.valueOf(flag ? value3004++ : value3004));
//                dataJSON.put("3005", String.valueOf(flag ? value3005++ : value3005));
//                dataJSON.put("3006", String.valueOf(flag ? value3006++ : value3006));
                dataJSON.put("3001", value3001);
                dataJSON.put("3002", flag ? value3002++ : String.valueOf(value3002));
                dataJSON.put("3003", flag ? value3003++ : value3003);
                dataJSON.put("3004", flag ? value3004++ : value3004);
                dataJSON.put("3005", flag ? value3005++ : value3005);
                dataJSON.put("3006", flag ? value3006++ : value3006);
                psnJSON.put("data", dataJSON);
                array.add(psnJSON);
                jsonObject.put("data", array);
                String url = "http://localhost:19075/receive-http/stecSend";
                log.info("发送消息：{}", jsonObject.toJSONString());
                String response = HttpRequest.post(url)
                        .header("Content-Type", "application/json")
                        .body(jsonObject.toJSONString())
                        .execute()
                        .body();
            }
        }
    }


    @Test
    public void testExportDeviceCheckRawReport() throws Exception {
        String url = "https://jgpt.shsttz.com/promis-web/rest/appletsCheckRaw/deviceCheckRawListReportExport";
        long intervalMillis = 0L;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = buildExportHeaders();
        Path saveRootDir = Paths.get("D:\\export");
        List<ExportTargetConfig> targetConfigs = Arrays.asList(
//                //机场线
//                new ExportTargetConfig(1, "机场线", 8, "施工单位项目负责人", YearMonth.of(2000, 1)),
//                new ExportTargetConfig(1, "机场线", 3, "总监理工程师", YearMonth.of(2000, 1)),
//                new ExportTargetConfig(1, "机场线", 6, "安全监理工程师", YearMonth.of(2000, 1)),
//                new ExportTargetConfig(1, "机场线", 11, "安全员", YearMonth.of(2000, 1))
//
//                //嘉闵线
//                new ExportTargetConfig(17, "嘉闵线", 8, "施工单位项目负责人", YearMonth.of(2000, 1)),
//                new ExportTargetConfig(17, "嘉闵线", 3, "总监理工程师", YearMonth.of(2000, 1)),
//                new ExportTargetConfig(17, "嘉闵线", 6, "安全监理工程师", YearMonth.of(2000, 1)),
//                new ExportTargetConfig(17, "嘉闵线", 11, "安全员", YearMonth.of(2000, 1))
//
//
//                //南汇支线
//                new ExportTargetConfig(63, "南汇支线", 8, "施工单位项目负责人", YearMonth.of(2000, 1)),
//                new ExportTargetConfig(63, "南汇支线", 3, "总监理工程师", YearMonth.of(2000, 1)),
//                new ExportTargetConfig(63, "南汇支线", 6, "安全监理工程师", YearMonth.of(2000, 1)),
//                new ExportTargetConfig(63, "南汇支线", 11, "安全员", YearMonth.of(2000, 1))
//
//                //示范区线
//                new ExportTargetConfig(73, "示范区线", 8, "施工单位项目负责人", YearMonth.of(2000, 1)),
//                new ExportTargetConfig(73, "示范区线", 3, "总监理工程师", YearMonth.of(2000, 1)),
//                new ExportTargetConfig(73, "示范区线", 6, "安全监理工程师", YearMonth.of(2000, 1)),
//                new ExportTargetConfig(73, "示范区线", 11, "安全员", YearMonth.of(2000, 1))
//
//                //南枫线
//                new ExportTargetConfig(214, "南枫线", 8, "施工单位项目负责人", YearMonth.of(2000, 1)),
//                new ExportTargetConfig(214, "南枫线", 3, "总监理工程师", YearMonth.of(2000, 1)),
//                new ExportTargetConfig(214, "南枫线", 6, "安全监理工程师", YearMonth.of(2000, 1)),
//                new ExportTargetConfig(214, "南枫线", 11, "安全员", YearMonth.of(2000, 1))
//
//                //虹桥枢纽
//                new ExportTargetConfig(30, "虹桥枢纽", 8, "施工单位项目负责人", YearMonth.of(2000, 1)),
//                new ExportTargetConfig(30, "虹桥枢纽", 3, "总监理工程师", YearMonth.of(2000, 1)),
//                new ExportTargetConfig(30, "虹桥枢纽", 6, "安全监理工程师", YearMonth.of(2000, 1)),
//                new ExportTargetConfig(30, "虹桥枢纽", 11, "安全员", YearMonth.of(2000, 1))
//
//                //三中心
//                new ExportTargetConfig(72, "三中心", 8, "施工单位项目负责人", YearMonth.of(2000, 1)),
//                new ExportTargetConfig(72, "三中心", 3, "总监理工程师", YearMonth.of(2000, 1)),
//                new ExportTargetConfig(72, "三中心", 6, "安全监理工程师", YearMonth.of(2000, 1)),
//                new ExportTargetConfig(72, "三中心", 11, "安全员", YearMonth.of(2000, 1))

                //马东车辆基地
                new ExportTargetConfig(74, "马东车辆基地", 8, "施工单位项目负责人", YearMonth.of(2000, 1)),
                new ExportTargetConfig(74, "马东车辆基地", 3, "总监理工程师", YearMonth.of(2000, 1)),
                new ExportTargetConfig(74, "马东车辆基地", 6, "安全监理工程师", YearMonth.of(2000, 1)),
                new ExportTargetConfig(74, "马东车辆基地", 11, "安全员", YearMonth.of(2000, 1))
        );
        List<ExportFailureRecord> failureRecords = new ArrayList<>();

        for (ExportTargetConfig targetConfig : targetConfigs) {
            exportTargetByMonthAdaptive(url, restTemplate, headers, saveRootDir, targetConfig, intervalMillis, failureRecords);
        }

        if (failureRecords.isEmpty()) {
            System.out.println("全部导出完成，没有小时级失败记录。");
        } else {
            log.warn("导出完成，共有{}条小时级失败记录：", failureRecords.size());
            for (ExportFailureRecord failureRecord : failureRecords) {
                log.warn("{}", failureRecord);
            }
        }
    }

    private HttpHeaders buildExportHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(
                MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
                MediaType.APPLICATION_OCTET_STREAM,
                MediaType.APPLICATION_JSON,
                MediaType.TEXT_PLAIN,
                MediaType.ALL
        ));
        headers.set("x-auth-token", "bearer 7fdfd3d2-45df-4653-9665-286e094a2b44");
        headers.set("origin", "https://jgpt.shsttz.com");
        headers.set("referer", "https://jgpt.shsttz.com/");
        headers.set("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36");
        headers.set("Cookie", "language=false");
        return headers;
    }

    private Map<String, Object> buildExportBody(ExportTargetConfig targetConfig, String beginDate, String endDate) {
        Map<String, Object> body = new HashMap<>();
        //项目ID
        body.put("projectId", targetConfig.projectId);
        //姓名
        body.put("name", "");
        //身份证
        body.put("idCard", "");
        //岗位
        body.put("station", targetConfig.stationCode);
        body.put("stationName", targetConfig.stationName);
        //进出位置
        body.put("areaType", "");
        body.put("areaId", null);
        //闸机
        body.put("deviceId", null);
        //所属标段
        body.put("belongTendersId", null);
        //进出日期
        body.put("beginDate", beginDate);
        body.put("endDate", endDate);
        return body;
    }

    private void exportTargetByMonthAdaptive(String url,
                                             RestTemplate restTemplate,
                                             HttpHeaders headers,
                                             Path saveRootDir,
                                             ExportTargetConfig targetConfig,
                                             long intervalMillis,
                                             List<ExportFailureRecord> failureRecords) throws InterruptedException {
        YearMonth endMonth = YearMonth.now();

        for (YearMonth currentMonth = targetConfig.startMonth; !currentMonth.isAfter(endMonth); currentMonth = currentMonth.plusMonths(1)) {
            exportMonthAdaptive(url, restTemplate, headers, saveRootDir, targetConfig, currentMonth, intervalMillis, failureRecords);
        }
    }

    private void exportMonthAdaptive(String url,
                                     RestTemplate restTemplate,
                                     HttpHeaders headers,
                                     Path saveRootDir,
                                     ExportTargetConfig targetConfig,
                                     YearMonth currentMonth,
                                     long intervalMillis,
                                     List<ExportFailureRecord> failureRecords) throws InterruptedException {
        LocalDate startDate = currentMonth.atDay(1);
        LocalDate endDate = currentMonth.atEndOfMonth();
        String beginDate = startDate + " 00:00:00";
        String endDateTime = endDate + " 23:59:59";
        Path filePath = buildMonthFilePath(saveRootDir, targetConfig, currentMonth);
        ExportResult exportResult = exportRange(url, restTemplate, headers, targetConfig, beginDate, endDateTime, filePath);
        sleepAfterRequest(intervalMillis);

        if (exportResult.status == ExportStatus.SUCCESS) {
            System.out.println(currentMonth + "按月导出成功：" + beginDate + " ~ " + endDateTime + "，文件：" + filePath.toAbsolutePath());
            return;
        }
        if (exportResult.status == ExportStatus.NO_DATA && probeNoDataDateRange(url, restTemplate, headers, targetConfig, startDate, endDate, intervalMillis)) {
            log.info("{}按月哨兵探测均无数据，跳过整月，beginDate={}，endDate={}", currentMonth, beginDate, endDateTime);
            return;
        }

        log.warn("{}按月导出未完成，开始自适应拆分，beginDate={}，endDate={}，reason={}",
                currentMonth, beginDate, endDateTime, exportResult.message);
        exportSplitDateRangeAdaptive(url, restTemplate, headers, saveRootDir, targetConfig, startDate, endDate, intervalMillis, failureRecords);
    }

    private void exportDateRangeAdaptive(String url,
                                         RestTemplate restTemplate,
                                         HttpHeaders headers,
                                         Path saveRootDir,
                                         ExportTargetConfig targetConfig,
                                         LocalDate startDate,
                                         LocalDate endDate,
                                         long intervalMillis,
                                         List<ExportFailureRecord> failureRecords) throws InterruptedException {
        if (startDate.isAfter(endDate)) {
            return;
        }
        if (startDate.equals(endDate)) {
            exportSingleDay(url, restTemplate, headers, saveRootDir, targetConfig, startDate, intervalMillis, failureRecords);
            return;
        }

        String beginDate = startDate + " 00:00:00";
        String endDateTime = endDate + " 23:59:59";
        ExportResult exportResult = exportRange(url, restTemplate, headers, targetConfig, beginDate, endDateTime, null);
        sleepAfterRequest(intervalMillis);

        if (exportResult.status == ExportStatus.SUCCESS) {
            log.info("{} ~ {} 多天范围导出可成功，继续拆到单天保存", startDate, endDate);
            exportDateRangeByDay(url, restTemplate, headers, saveRootDir, targetConfig, startDate, endDate, intervalMillis, failureRecords);
            return;
        }
        if (exportResult.status == ExportStatus.NO_DATA && probeNoDataDateRange(url, restTemplate, headers, targetConfig, startDate, endDate, intervalMillis)) {
            log.info("{} ~ {} 哨兵探测均无数据，跳过该日期范围", startDate, endDate);
            return;
        }

        log.warn("{} ~ {} 范围导出未完成，继续二分拆分，reason={}", startDate, endDate, exportResult.message);
        exportSplitDateRangeAdaptive(url, restTemplate, headers, saveRootDir, targetConfig, startDate, endDate, intervalMillis, failureRecords);
    }

    private void exportSplitDateRangeAdaptive(String url,
                                              RestTemplate restTemplate,
                                              HttpHeaders headers,
                                              Path saveRootDir,
                                              ExportTargetConfig targetConfig,
                                              LocalDate startDate,
                                              LocalDate endDate,
                                              long intervalMillis,
                                              List<ExportFailureRecord> failureRecords) throws InterruptedException {
        LocalDate splitDate = splitDateRange(startDate, endDate);
        exportDateRangeAdaptive(url, restTemplate, headers, saveRootDir, targetConfig, startDate, splitDate, intervalMillis, failureRecords);
        exportDateRangeAdaptive(url, restTemplate, headers, saveRootDir, targetConfig, splitDate.plusDays(1), endDate, intervalMillis, failureRecords);
    }

    private void exportDateRangeByDay(String url,
                                      RestTemplate restTemplate,
                                      HttpHeaders headers,
                                      Path saveRootDir,
                                      ExportTargetConfig targetConfig,
                                      LocalDate startDate,
                                      LocalDate endDate,
                                      long intervalMillis,
                                      List<ExportFailureRecord> failureRecords) throws InterruptedException {
        for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)) {
            exportSingleDay(url, restTemplate, headers, saveRootDir, targetConfig, currentDate, intervalMillis, failureRecords);
        }
    }

    private void exportSingleDay(String url,
                                 RestTemplate restTemplate,
                                 HttpHeaders headers,
                                 Path saveRootDir,
                                 ExportTargetConfig targetConfig,
                                 LocalDate currentDate,
                                 long intervalMillis,
                                 List<ExportFailureRecord> failureRecords) throws InterruptedException {
        String beginDate = currentDate + " 00:00:00";
        String endDate = currentDate + " 23:59:59";
        Path filePath = buildDayFilePath(saveRootDir, targetConfig, currentDate);
        ExportResult exportResult = exportRange(url, restTemplate, headers, targetConfig, beginDate, endDate, filePath);
        sleepAfterRequest(intervalMillis);

        if (exportResult.status == ExportStatus.SUCCESS) {
            System.out.println(currentDate + "按天导出成功：" + beginDate + " ~ " + endDate + "，文件：" + filePath.toAbsolutePath());
        } else {
            log.warn("{}按天导出未完成，准备按小时拆分，beginDate={}，endDate={}，reason={}",
                    currentDate, beginDate, endDate, exportResult.message);
            exportDayByHour(url, restTemplate, headers, saveRootDir, targetConfig, currentDate, intervalMillis, failureRecords);
        }
    }

    private boolean probeNoDataDateRange(String url,
                                         RestTemplate restTemplate,
                                         HttpHeaders headers,
                                         ExportTargetConfig targetConfig,
                                         LocalDate startDate,
                                         LocalDate endDate,
                                         long intervalMillis) throws InterruptedException {
        List<LocalDate> probeDates = buildProbeDates(startDate, endDate);
        for (LocalDate probeDate : probeDates) {
            String beginDate = probeDate + " 00:00:00";
            String endDateTime = probeDate + " 23:59:59";
            ExportResult probeResult = exportRange(url, restTemplate, headers, targetConfig, beginDate, endDateTime, null);
            sleepAfterRequest(intervalMillis);
            if (probeResult.status != ExportStatus.NO_DATA) {
                log.info("{} ~ {} 哨兵探测命中非空/异常日期：{}，status={}，reason={}",
                        startDate, endDate, probeDate, probeResult.status, probeResult.message);
                return false;
            }
        }
        return true;
    }

    private List<LocalDate> buildProbeDates(LocalDate startDate, LocalDate endDate) {
        LinkedHashSet<LocalDate> probeDates = new LinkedHashSet<>();
        probeDates.add(startDate);
        probeDates.add(splitDateRange(startDate, endDate));
        probeDates.add(endDate);
        return new ArrayList<>(probeDates);
    }

    private LocalDate splitDateRange(LocalDate startDate, LocalDate endDate) {
        long days = endDate.toEpochDay() - startDate.toEpochDay();
        return startDate.plusDays(days / 2);
    }

    private void exportDayByHour(String url,
                                 RestTemplate restTemplate,
                                 HttpHeaders headers,
                                 Path saveRootDir,
                                 ExportTargetConfig targetConfig,
                                 LocalDate currentDate,
                                 long intervalMillis,
                                 List<ExportFailureRecord> failureRecords) throws InterruptedException {
        for (int hour = 0; hour < 24; hour++) {
            String beginDate = currentDate + String.format(" %02d:00:00", hour);
            String endDate = currentDate + String.format(" %02d:59:59", hour);
            Path filePath = buildHourFilePath(saveRootDir, targetConfig, currentDate, hour);
            ExportResult exportResult = exportRange(url, restTemplate, headers, targetConfig, beginDate, endDate, filePath);
            sleepAfterRequest(intervalMillis);

            if (exportResult.status == ExportStatus.SUCCESS) {
                System.out.println(currentDate + " " + hour + "点按小时导出成功：" + beginDate + " ~ " + endDate + "，文件：" + filePath.toAbsolutePath());
            } else if (exportResult.status == ExportStatus.NO_DATA) {
                log.info("{} {}点按小时导出无数据，beginDate={}，endDate={}", currentDate, hour, beginDate, endDate);
            } else {
                failureRecords.add(new ExportFailureRecord(
                        targetConfig.projectName,
                        targetConfig.stationName,
                        "小时",
                        beginDate,
                        endDate,
                        exportResult.message
                ));
                log.warn("{} {}点按小时导出失败，已记录失败清单，beginDate={}，endDate={}，reason={}",
                        currentDate, hour, beginDate, endDate, exportResult.message);
            }
        }
    }

    private ExportResult exportRange(String url,
                                     RestTemplate restTemplate,
                                     HttpHeaders headers,
                                     ExportTargetConfig targetConfig,
                                     String beginDate,
                                     String endDate,
                                     Path filePath) {
        try {
            Map<String, Object> body = buildExportBody(targetConfig, beginDate, endDate);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    byte[].class
            );

            ExportResult checkResult = checkExportResponse(response);
            if (checkResult.status != ExportStatus.SUCCESS) {
                return checkResult;
            }

            if (filePath == null) {
                return ExportResult.success();
            }
            Files.createDirectories(filePath.getParent());
            Files.write(
                    filePath,
                    response.getBody(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
            return ExportResult.success();
        } catch (RestClientResponseException e) {
            return ExportResult.failed("status=" + e.getRawStatusCode()
                    + "，bodySize=" + e.getResponseBodyAsByteArray().length
                    + "，body=" + previewBody(e.getResponseBodyAsByteArray()));
        } catch (RestClientException e) {
            return ExportResult.failed(e.getClass().getSimpleName() + "：" + e.getMessage());
        } catch (Exception e) {
            return ExportResult.failed(e.getClass().getSimpleName() + "：" + e.getMessage());
        }
    }

    private ExportResult checkExportResponse(ResponseEntity<byte[]> response) {
        byte[] responseBody = response.getBody();
        if (!response.getStatusCode().is2xxSuccessful()) {
            return ExportResult.failed("status=" + response.getStatusCodeValue()
                    + "，bodySize=" + (responseBody == null ? 0 : responseBody.length));
        }
        if (responseBody == null || responseBody.length == 0) {
            return ExportResult.noData();
        }
        if (isExcelResponse(response.getHeaders(), responseBody)) {
            return ExportResult.success();
        }

        String bodyPreview = previewBody(responseBody);
        if (isNoDataResponse(bodyPreview)) {
            return ExportResult.noData();
        }

//        return ExportResult.failed("响应不是Excel，status=" + response.getStatusCodeValue()
//                + "，contentType=" + response.getHeaders().getContentType()
//                + "，bodySize=" + responseBody.length
//                + "，body=" + bodyPreview);
        return ExportResult.noData();
    }

    private boolean isExcelResponse(HttpHeaders headers, byte[] responseBody) {
        MediaType contentType = headers.getContentType();
        if (contentType != null) {
            String contentTypeValue = contentType.toString().toLowerCase(Locale.ROOT);
            if (contentTypeValue.contains("spreadsheet") || contentTypeValue.contains("excel")) {
                return true;
            }
        }
        return responseBody.length >= 2 && responseBody[0] == 'P' && responseBody[1] == 'K';
    }

    private String previewBody(byte[] responseBody) {
        if (responseBody == null || responseBody.length == 0) {
            return "";
        }
        String body = new String(responseBody, StandardCharsets.UTF_8).replaceAll("\\s+", " ").trim();
        if (body.length() > 200) {
            return body.substring(0, 200);
        }
        return body;
    }

    private boolean isNoDataResponse(String responseBody) {
        if (responseBody == null || responseBody.length() == 0) {
            return true;
        }
        String lowerBody = responseBody.toLowerCase(Locale.ROOT);
        return responseBody.contains("暂无数据")
                || responseBody.contains("无数据")
                || responseBody.contains("没有数据")
                || responseBody.contains("未查询到")
                || responseBody.contains("未查到")
                || responseBody.contains("查询结果为空")
                || lowerBody.contains("no data");
    }

    private Path buildMonthFilePath(Path saveRootDir, ExportTargetConfig targetConfig, YearMonth currentMonth) {
        return saveRootDir
                .resolve(targetConfig.projectName)
                .resolve(targetConfig.stationName)
                .resolve(currentMonth.getYear() + "年")
                .resolve(currentMonth.getMonthValue() + "月")
                .resolve(String.format("%s-%s-%d年-%d月份考勤记录.xlsx",
                        targetConfig.projectName,
                        targetConfig.stationName,
                        currentMonth.getYear(),
                        currentMonth.getMonthValue()));
    }

    private Path buildDayFilePath(Path saveRootDir, ExportTargetConfig targetConfig, LocalDate currentDate) {
        return saveRootDir
                .resolve(targetConfig.projectName)
                .resolve(targetConfig.stationName)
                .resolve(currentDate.getYear() + "年")
                .resolve(currentDate.getMonthValue() + "月")
                .resolve(String.format("%s-%s-%d年-%d月%d号-考勤记录.xlsx",
                        targetConfig.projectName,
                        targetConfig.stationName,
                        currentDate.getYear(),
                        currentDate.getMonthValue(),
                        currentDate.getDayOfMonth()));
    }

    private Path buildHourFilePath(Path saveRootDir, ExportTargetConfig targetConfig, LocalDate currentDate, int hour) {
        return saveRootDir
                .resolve(targetConfig.projectName)
                .resolve(targetConfig.stationName)
                .resolve(currentDate.getYear() + "年")
                .resolve(currentDate.getMonthValue() + "月")
                .resolve(String.format("%s-%s-%d年-%d月%d号%d点-考勤记录.xlsx",
                        targetConfig.projectName,
                        targetConfig.stationName,
                        currentDate.getYear(),
                        currentDate.getMonthValue(),
                        currentDate.getDayOfMonth(),
                        hour));
    }

    private void sleepAfterRequest(long intervalMillis) throws InterruptedException {
        if (intervalMillis > 0) {
            Thread.sleep(intervalMillis);
        }
    }

    private enum ExportStatus {
        SUCCESS,
        NO_DATA,
        FAILED
    }

    private static class ExportTargetConfig {
        private final Integer projectId;
        private final String projectName;
        private final Integer stationCode;
        private final String stationName;
        private final YearMonth startMonth;

        private ExportTargetConfig(Integer projectId,
                                   String projectName,
                                   Integer stationCode,
                                   String stationName,
                                   YearMonth startMonth) {
            this.projectId = projectId;
            this.projectName = projectName;
            this.stationCode = stationCode;
            this.stationName = stationName;
            this.startMonth = startMonth;
        }
    }

    private static class ExportResult {
        private final ExportStatus status;
        private final String message;

        private ExportResult(ExportStatus status, String message) {
            this.status = status;
            this.message = message;
        }

        private static ExportResult success() {
            return new ExportResult(ExportStatus.SUCCESS, "success");
        }

        private static ExportResult noData() {
            return new ExportResult(ExportStatus.NO_DATA, "empty body or no data message");
        }

        private static ExportResult failed(String message) {
            return new ExportResult(ExportStatus.FAILED, message);
        }
    }

    private static class ExportFailureRecord {
        private final String projectName;
        private final String stationName;
        private final String rangeType;
        private final String beginDate;
        private final String endDate;
        private final String reason;

        private ExportFailureRecord(String projectName,
                                    String stationName,
                                    String rangeType,
                                    String beginDate,
                                    String endDate,
                                    String reason) {
            this.projectName = projectName;
            this.stationName = stationName;
            this.rangeType = rangeType;
            this.beginDate = beginDate;
            this.endDate = endDate;
            this.reason = reason;
        }

        @Override
        public String toString() {
            return String.format("[%s-%s][%s] %s ~ %s，原因：%s",
                    projectName, stationName, rangeType, beginDate, endDate, reason);
        }
    }




}
