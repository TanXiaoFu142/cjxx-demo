package com.xf.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.FileUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.stec.utils.CollectionUtils;
import com.stec.utils.ObjectUtils;
import com.stec.utils.StringUtils;
import com.xf.entity.ScSafetyIssueExcelDto;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.aspectj.org.eclipse.jdt.internal.compiler.apt.util.Archive;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.*;
import java.util.*;
import java.nio.file.Files;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author tanjunjie
 * @version 1.0
 * @className TestController
 * @date 2024/12/26 17:31
 * @description TODO
 */
@RestController
@RequestMapping(value = "/rest/test")
public class TestController {
    public String downloadImage(String urlLink, Path tempDir) {
        try {
            URL url = new URL(urlLink);
            InputStream in = url.openStream();
            Path tempFile = Files.createTempFile(tempDir, "downloaded_image", ".jpg");
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
            in.close();

            System.out.println("图片已下载到临时文件: " + tempFile.toAbsolutePath());
            return tempFile.toAbsolutePath().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, String> downloadImages(List<String> urlLinks, Path tempDir) {
        ExecutorService executorService = Executors.newFixedThreadPool(20); // 调整线程池大小
        Map<String, CompletableFuture<String>> futureMap = urlLinks.stream()
                .collect(Collectors.toMap(
                        urlLink -> urlLink,
                        urlLink -> CompletableFuture.supplyAsync(() -> downloadImage(urlLink, tempDir), executorService)
                ));

        Map<String, String> resultMap = futureMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            try {
                                return entry.getValue().get(); // 获取结果
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                                return null; // 如果下载失败，记录为 null
                            }
                        }
                ));

        executorService.shutdown(); // 关闭线程池
        return resultMap;
    }

    private byte[] compressBytes(byte[] bytes) throws IOException {
        // 将字节数组转换为 BufferedImage
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        BufferedImage originalImage = ImageIO.read(bais);

        // 设置压缩后的图片宽度和高度
        int width = originalImage.getWidth() / 2; // 示例：宽度减半
        int height = originalImage.getHeight() / 2; // 示例：高度减半

        // 创建一个新的 BufferedImage 来存储压缩后的图片
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        // 将压缩后的 BufferedImage 转换为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", baos);
        return baos.toByteArray();
    }

    @ApiOperation("安全问题导出")
    @RequestMapping(value = "/exportSafetyIssue", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public void exportSafetyIssue(HttpServletResponse response) throws IOException {
        List<ScSafetyIssueExcelDto> excelDtoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ScSafetyIssueExcelDto dto = new ScSafetyIssueExcelDto();
            dto.setNodeName("11111");
            excelDtoList.add(dto);
        }
        Path tempDir = null;
        // 创建临时文件夹
        tempDir = Files.createTempDirectory("temp_images");
        System.out.println("临时文件夹已创建: " + tempDir.toAbsolutePath());
        for (ScSafetyIssueExcelDto excelDto : excelDtoList) {
            String urlLink = "https://gkpt.jxttgroup.com/stec-platform-doc/doc/wKgAUmSdaTyEJxXZAAAAADGR7Wk135.jpg";
            String tempFilePath = downloadImage(urlLink, tempDir);
            if (tempFilePath != null) {
                Path tempFile = Paths.get(tempFilePath);
                byte[] bytes = FileUtils.readFileToByteArray(tempFile.toFile());

                //压缩这个bytes图片
                excelDto.setPhotos(this.compressBytes(bytes));
            }
        }

        //导出操作
        ExcelWriter writer = null;
        try {
            String fileName = StringUtils.bond("安全问题查询导出" + DateUtil.formatDate(new Date()) + ".xlsx");
            // 设置response参数，可以打开下载页面
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "attachment");
            response.setHeader("attachment", URLEncoder.encode(fileName, "utf-8"));

            writer = EasyExcel.write(response.getOutputStream(), ScSafetyIssueExcelDto.class).build();
            WriteSheet sheet = EasyExcel.writerSheet("查询结果").build();
            writer.write(excelDtoList, sheet);
        } catch (Exception e) {
            response.reset();
            e.printStackTrace();
        } finally {
            if (ObjectUtils.isNotNull(writer)) {
                writer.finish();
            }
            // 删除临时文件夹及其内容
            if (tempDir != null) {
                try {
                    Files.walk(tempDir)
                            .sorted((p1, p2) -> -p1.compareTo(p2)) // 逆序删除以确保文件夹先于其内容删除
                            .map(Path::toFile)
                            .forEach(File::delete);
                    System.out.println("临时文件夹及其内容已删除: " + tempDir.toAbsolutePath());
                } catch (IOException e) {
                    System.err.println("删除临时文件夹失败: " + tempDir.toAbsolutePath() + ", 原因: " + e.getMessage());
                }
            }
        }
    }


}
