package com.xf.controller;

import freemarker.template.Configuration;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import freemarker.template.Template;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
public class DownloadController {

    @Autowired
    private Configuration configuration;

    @ApiOperation("从指定路径下载文件并压缩")
    @GetMapping("/download")
    public void downloadFiles(HttpServletResponse response) throws IOException {

        //输入要下载的文件列表
        List<String> files = Arrays.asList("AQAAA.txt");

        // 定义一个字节数组输出流，用于写入zip文件
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // 创建ZipOutputStream
        ZipOutputStream zos = new ZipOutputStream(bos);

        // 遍历文件列表
        for (String file : files) {

            // 读取文件
            Resource resource = new ClassPathResource("D:\\files\\" + file);
            InputStream is = resource.getInputStream();

            // 添加文件到ZipOutputStream
            zos.putNextEntry(new ZipEntry(file));

            byte[] bytes = new byte[1024];
            int length;
            while ((length = is.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }

            // 关闭Entry
            zos.closeEntry();
            // 关闭输入流
            is.close();
        }

        // 关闭ZipOutputStream
        zos.close();

        // 设置response header
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=files.zip");

        // 写入response的输出流
        OutputStream os = response.getOutputStream();
        bos.writeTo(os);

        // 关闭输出流
        os.flush();
        os.close();
    }

    /******************************************************************************************************************/
    @ApiOperation("从远程服务器下载多个文件并压缩（GET请求）")
    @GetMapping("/downloadFilesList")
    public ResponseEntity<byte[]> downloadFilesList() throws IOException {

        String urlPath = "https://tech.suitbim.com/stec-platform-doc/doc/";

        List<String> urls = new ArrayList<>();
        urls.add(urlPath + "wKhjGmSCoMOEKk2nAAAAAONlMz8345.pdf");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);

        for (String url : urls) {
            URL fileUrl = new URL(url);
            String fileName = getFileName(fileUrl.getPath());

            // Download the file
            byte[] fileBytes = StreamUtils.copyToByteArray(fileUrl.openStream());

            // Add the file to the zip archive
            ZipEntry zipEntry = new ZipEntry("测试111.pdf");
            zos.putNextEntry(zipEntry);
            zos.write(fileBytes);
            zos.closeEntry();
        }

        zos.finish();
        zos.close();

        // Set the response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "files.zip");


        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
    }

    private String getFileName(String filePath) {
        Path path = Paths.get(filePath);
        return path.getFileName().toString();
    }

    /******************************************************************************************************************/

    @ApiOperation("从远程服务器下载多个文件并压缩（POST请求）")
    @PostMapping("/files")
    public ResponseEntity<byte[]> files() throws IOException {
        String[] fileUrls = {"https://tech.suitbim.com/stec-platform-doc/doc/file1.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/file2.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/file3.pdf"};

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

        for (String fileUrl : fileUrls) {
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            ZipEntry zipEntry = new ZipEntry(getFileNameFromUrl(fileUrl));
            zipOutputStream.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, length);
            }

            inputStream.close();
            zipOutputStream.closeEntry();
            connection.disconnect();
        }

        zipOutputStream.close();
        outputStream.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "files.zip");

        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }

    private String getFileNameFromUrl(String url) {
        int lastSlashIndex = url.lastIndexOf("/");
        if (lastSlashIndex != -1 && lastSlashIndex < url.length() - 1) {
            return url.substring(lastSlashIndex + 1);
        } else {
            throw new IllegalArgumentException("Invalid URL");
        }
    }

    /******************************************************************************************************************/

    private static final String BASE_URL = "https://tech.suitbim.com/stec-platform-doc/doc";
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @ApiModelProperty("多线程下载多个文件并压缩")
    @RequestMapping(value = "/syncDownloadFileList", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> syncDownloadFileList(RedirectAttributes redirectAttributes) throws IOException {

        long a = System.currentTimeMillis();

        // 获取文件流
        List<Callable<byte[]>> tasks = new ArrayList<>();

        List<String> files = new ArrayList<>(Arrays.asList(
                "wKhjGmSMLkuEdlgnAAAAAGk-LQo177.pdf",
                "wKhjGmSMKY2ECN4XAAAAAONlMz8995.pdf",
                "wKhjGmSMKY2EFSWKAAAAAONlMz8417.pdf",
                "wKhjGmSMKY2EWFDQAAAAAONlMz8219.pdf",
                "wKhjGmSMKYyEXWw_AAAAAG_5s3I896.pdf",
                "wKhjGmSMKYyEax01AAAAAONlMz8248.pdf",
                "wKhjGmSMKYyEU1BiAAAAAPd7LVY221.pdf",
                "wKhjGmSMKYuEEfAfAAAAAONlMz8579.pdf",
                "wKhjGmSMKYuEVIsaAAAAAONlMz8599.pdf",
                "wKhjGmSMKYqEfBUnAAAAAONlMz8340.pdf",
                "wKhjGmSMKYqETCibAAAAAAL3lKw688.pdf",
                "wKhjGmSMKYqEBYgKAAAAAPd7LVY763.pdf",
                "wKhjGmSMKYmEWhVfAAAAAF29wK8769.pdf",
                "wKhjGmSMKYiEUfeoAAAAANnF7Dg659.pdf",
                "wKhjGmSMKYiEMP57AAAAAAGvGLY123.pdf",
                "wKhjGmSMKYeEK2quAAAAAONlMz8015.pdf",
                "wKhjGmSMKYeEJMdVAAAAAG_5s3I958.pdf",
                "wKhjGmSMKYeEJJ_ZAAAAAG_5s3I299.pdf",
                "wKhjGmSMKYaEXDO9AAAAAONlMz8204.pdf",
                "wKhjGmSMKYaETSPLAAAAAG_5s3I326.pdf",
                "wKhjGmSMKYaEAOy9AAAAAONlMz8753.pdf",
                "wKhjGmSMKYaEECQHAAAAAONlMz8772.pdf",
                "wKhjGmSMKYaEGFlaAAAAAONlMz8727.pdf",
                "wKhjGmSMKYWERc7IAAAAAI_WYJg565.pdf",
                "wKhjGmSMKYWEFz2LAAAAAONlMz8696.pdf",
                "wKhjGmSMKYWEGGGlAAAAAONlMz8250.pdf",
                "wKhjGmSMKYWEZ1QNAAAAAONlMz8730.pdf",
                "wKhjGmSMKYSEJ98vAAAAAAGvGLY930.pdf",
                "wKhjGmSMKYSEYLZNAAAAAG_5s3I634.pdf",
                "wKhjGmSMKYSENK9XAAAAAG_5s3I596.pdf",
                "wKhjGmSMKYOEF4MdAAAAAG_5s3I918.pdf",
                "wKhjGmSMKYOEHPH7AAAAANnF7Dg216.pdf",
                "wKhjGmSMKYOEYM0aAAAAAPd7LVY339.pdf",
                "wKhjGmSMKYKEddyaAAAAAAL3lKw719.pdf",
                "wKhjGmSMKYGEXWMOAAAAAPd7LVY315.pdf",
                "wKhjGmSMKYCEXkEpAAAAAONlMz8309.pdf",
                "wKhjGmSMKYCEQOZvAAAAAAGvGLY370.pdf",
                "wKhjGmSMKYCEAVZ_AAAAAG_5s3I164.pdf",
                "wKhjGmSMKYCEaxHiAAAAANnF7Dg537.pdf",
                "wKhjGmSMKX-EAwjgAAAAAPd7LVY572.pdf",
                "wKhjGmSMKX6EbSwdAAAAAF29wK8821.pdf",
                "wKhjGmSMKX6EPCJFAAAAAAL3lKw862.pdf",
                "wKhjGmSMKX6EC4ixAAAAAONlMz8262.pdf",
                "wKhjGmSMKX6EHhqrAAAAAONlMz8355.pdf",
                "wKhjGmSMKX2EMVKkAAAAAPd7LVY949.pdf",
                "wKhjGmSMKXyEMX3zAAAAAONlMz8547.pdf",
                "wKhjGmSMKXyEX3pkAAAAAONlMz8645.pdf",
                "wKhjGmSMKXyETddkAAAAAG_5s3I510.pdf",
                "wKhjGmSMKXyEeJGMAAAAAI_WYJg656.pdf",
                "wKhjGmSMKXuEepXjAAAAAAGvGLY803.pdf",
                "wKhjGmSMKXuEKxx6AAAAAONlMz8920.pdf"));

        for (String filename : files) {
            if (!StringUtils.hasText(filename)) {
                continue;
            }

            String url = BASE_URL + "/" + filename;
            tasks.add(() -> {
                try (InputStream inputStream = new UrlResource(url).getInputStream()) {
                    byte[] data = null;
                    if (inputStream != null) {
                        data = toByteArray(inputStream);
                    }
                    return data;
                }
            });
        }

        // 并发下载
        List<byte[]> data = new ArrayList<>();
        try {
            data = executorService.invokeAll(tasks).stream()
                    .map(future -> {
                        try {
                            System.out.println(Thread.currentThread().getName());
                            return future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            // handle exception
                            return null;
                        }
                    })
                    .filter(bytes -> bytes != null && bytes.length > 0)
                    .collect(ArrayList::new, ArrayList::add, (lst1, lst2) -> lst1.addAll(lst2));
        } catch (InterruptedException e) {
            // handle exception
        }

        // 返回压缩后的zip文件
        byte[] zipData = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ZipOutputStream zos = new ZipOutputStream(bos)) {
            for (int i = 0; i < data.size(); i++) {
                ZipEntry entry = new ZipEntry(files.get(i));
                zos.putNextEntry(entry);
                zos.write(data.get(i));
                zos.closeEntry();
            }
            zos.finish();
            zipData = bos.toByteArray();
        } catch (IOException e) {
            // handle exception
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "files.zip");
        System.out.println(System.currentTimeMillis() - a);
        return new ResponseEntity<>(zipData, headers, HttpStatus.OK);
    }

    /**
     * 将输入流转化为字节数组
     *
     * @param inputStream 输入流
     * @return 字节数组
     * @throws IOException IO异常
     */
    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) > -1) {
            bos.write(buffer, 0, len);
        }
        bos.flush();
        return bos.toByteArray();
    }

    /******************************************************************************************************************/

    @ApiModelProperty("多线程下载多个文件并压缩")
    @GetMapping("/download-and-zip")
    public void downloadAndZipFiles(HttpServletResponse response) {
        long a = System.currentTimeMillis();
        //定义线程数量
        int threadCount = 5;
        //创建下载文件的链接列表
        List<String> urls = Arrays.asList(
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMLkuEdlgnAAAAAGk-LQo177.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKY2ECN4XAAAAAONlMz8995.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKY2EFSWKAAAAAONlMz8417.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKY2EWFDQAAAAAONlMz8219.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYyEXWw_AAAAAG_5s3I896.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYyEax01AAAAAONlMz8248.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYyEU1BiAAAAAPd7LVY221.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYuEEfAfAAAAAONlMz8579.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYuEVIsaAAAAAONlMz8599.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYqEfBUnAAAAAONlMz8340.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYqETCibAAAAAAL3lKw688.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYqEBYgKAAAAAPd7LVY763.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYmEWhVfAAAAAF29wK8769.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYiEUfeoAAAAANnF7Dg659.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYiEMP57AAAAAAGvGLY123.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYeEK2quAAAAAONlMz8015.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYeEJMdVAAAAAG_5s3I958.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYeEJJ_ZAAAAAG_5s3I299.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYaEXDO9AAAAAONlMz8204.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYaETSPLAAAAAG_5s3I326.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYaEAOy9AAAAAONlMz8753.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYaEECQHAAAAAONlMz8772.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYaEGFlaAAAAAONlMz8727.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYWERc7IAAAAAI_WYJg565.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYWEFz2LAAAAAONlMz8696.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYWEGGGlAAAAAONlMz8250.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYWEZ1QNAAAAAONlMz8730.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYSEJ98vAAAAAAGvGLY930.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYSEYLZNAAAAAG_5s3I634.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYSENK9XAAAAAG_5s3I596.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYOEF4MdAAAAAG_5s3I918.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYOEHPH7AAAAANnF7Dg216.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYOEYM0aAAAAAPd7LVY339.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYKEddyaAAAAAAL3lKw719.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYGEXWMOAAAAAPd7LVY315.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYCEXkEpAAAAAONlMz8309.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYCEQOZvAAAAAAGvGLY370.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYCEAVZ_AAAAAG_5s3I164.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYCEaxHiAAAAANnF7Dg537.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKX-EAwjgAAAAAPd7LVY572.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKX6EbSwdAAAAAF29wK8821.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKX6EPCJFAAAAAAL3lKw862.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKX6EC4ixAAAAAONlMz8262.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKX6EHhqrAAAAAONlMz8355.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKX2EMVKkAAAAAPd7LVY949.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKXyEMX3zAAAAAONlMz8547.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKXyEX3pkAAAAAONlMz8645.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKXyETddkAAAAAG_5s3I510.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKXyEeJGMAAAAAI_WYJg656.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKXuEepXjAAAAAAGvGLY803.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKXuEKxx6AAAAAONlMz8920.pdf"
        );


        try {
            //创建一个临时目录
            String tempDir = System.getProperty("java.io.tmpdir") + "/download-and-zip";
            File dir = new File(tempDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            //创建一个线程池
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);

            //创建一个Future列表
            List<Future<File>> futures = new ArrayList<>();

            //循环处理每个文件的下载
            for (String url : urls) {
                futures.add(executor.submit(() -> {
                    try {
                        String fileName = StringUtils.getFilename(url);
                        File file = new File(tempDir, fileName);
                        FileUtils.copyURLToFile(new URL(url), file);
                        return file;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }));

            }

            //等待所有线程完成
            List<File> files = new ArrayList<>();
            for (Future<File> future : futures) {
                files.add(future.get());
            }

            //关闭线程池并等待一段时间确保所有线程都已关闭
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);

            //创建压缩文件
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=Files.zip");
            try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
                for (File file : files) {
                    String fileName = file.getName();
                    ZipEntry zipEntry = new ZipEntry(fileName);
                    zipOut.putNextEntry(zipEntry);
                    FileInputStream fis = new FileInputStream(file);
                    IOUtils.copy(fis, zipOut);
                    fis.close();
                    file.delete();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println(System.currentTimeMillis() - a);
    }

    /******************************************************************************************************************/

    @ApiModelProperty("多线程下载多个文件并压缩")
    @GetMapping("/syncDownload")
    public void syncDownload() throws Exception {
        long a = System.currentTimeMillis();
        List<String> urls = new ArrayList<>(Arrays.asList(
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMLkuEdlgnAAAAAGk-LQo177.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKY2ECN4XAAAAAONlMz8995.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKY2EFSWKAAAAAONlMz8417.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKY2EWFDQAAAAAONlMz8219.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYyEXWw_AAAAAG_5s3I896.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYyEax01AAAAAONlMz8248.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYyEU1BiAAAAAPd7LVY221.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYuEEfAfAAAAAONlMz8579.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYuEVIsaAAAAAONlMz8599.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYqEfBUnAAAAAONlMz8340.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYqETCibAAAAAAL3lKw688.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYqEBYgKAAAAAPd7LVY763.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYmEWhVfAAAAAF29wK8769.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYiEUfeoAAAAANnF7Dg659.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYiEMP57AAAAAAGvGLY123.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYeEK2quAAAAAONlMz8015.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYeEJMdVAAAAAG_5s3I958.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYeEJJ_ZAAAAAG_5s3I299.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYaEXDO9AAAAAONlMz8204.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYaETSPLAAAAAG_5s3I326.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYaEAOy9AAAAAONlMz8753.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYaEECQHAAAAAONlMz8772.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYaEGFlaAAAAAONlMz8727.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYWERc7IAAAAAI_WYJg565.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYWEFz2LAAAAAONlMz8696.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYWEGGGlAAAAAONlMz8250.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYWEZ1QNAAAAAONlMz8730.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYSEJ98vAAAAAAGvGLY930.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYSEYLZNAAAAAG_5s3I634.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYSENK9XAAAAAG_5s3I596.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYOEF4MdAAAAAG_5s3I918.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYOEHPH7AAAAANnF7Dg216.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYOEYM0aAAAAAPd7LVY339.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYKEddyaAAAAAAL3lKw719.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYGEXWMOAAAAAPd7LVY315.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYCEXkEpAAAAAONlMz8309.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYCEQOZvAAAAAAGvGLY370.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYCEAVZ_AAAAAG_5s3I164.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYCEaxHiAAAAANnF7Dg537.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKX-EAwjgAAAAAPd7LVY572.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKX6EbSwdAAAAAF29wK8821.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKX6EPCJFAAAAAAL3lKw862.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKX6EC4ixAAAAAONlMz8262.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKX6EHhqrAAAAAONlMz8355.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKX2EMVKkAAAAAPd7LVY949.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKXyEMX3zAAAAAONlMz8547.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKXyEX3pkAAAAAONlMz8645.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKXyETddkAAAAAG_5s3I510.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKXyEeJGMAAAAAI_WYJg656.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKXuEepXjAAAAAAGvGLY803.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKXuEKxx6AAAAAONlMz8920.pdf"
        ));
        List<File> files = downloadFilesAsync(urls);
        File zipFile = compressFiles(files);
        downloadFile(zipFile);
        System.out.println(System.currentTimeMillis() - a);
    }

    private List<File> downloadFilesAsync(List<String> urls) {
        List<File> files = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            String url = urls.get(i);
            Thread thread = new Thread(() -> {
                try {
                    String fileName = url.substring(url.lastIndexOf("/") + 1);
                    String threadName = Thread.currentThread().getName();
                    System.out.println(threadName + " - Starting download: " + fileName);
                    URL fileUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
                    conn.setRequestMethod("GET");
                    InputStream inputStream = conn.getInputStream();
                    byte[] buffer = new byte[1024];
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    int length;
                    while ((length = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }
                    File file = new File(fileName);
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    outputStream.writeTo(fileOutputStream);
                    outputStream.close();
                    fileOutputStream.close();
                    inputStream.close();
                    System.out.println(threadName + " - Download finished: " + fileName);
                    files.add(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return files;
    }

    private File compressFiles(List<File> files) {
        File zipFile = new File("download.zip");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
            for (File file : files) {
                FileInputStream fileInputStream = new FileInputStream(file);
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zipOutputStream.putNextEntry(zipEntry);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fileInputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, length);
                }
                zipOutputStream.closeEntry();
                fileInputStream.close();
            }
            zipOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zipFile;
    }

    private void downloadFile(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            assert response != null;
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
            OutputStream outputStream = response.getOutputStream();
            while ((length = fileInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /******************************************************************************************************************/

    private static final int THREAD_NUM = 5;
    private static final String DOWNLOAD_URL = "https://tech.suitbim.com/stec-platform-doc/doc";
    private static final String ZIPFILE_NAME = "download.zip";
    private static final String DOWNLOAD_PREFIX = "download_";
    private static final String DOWNLOAD_SUFFIX = ".pdf";
    private static final int BUFFER_SIZE = 4096;


    @GetMapping("/sync-download")
    public ResponseEntity<Resource> syncDownloads() throws IOException, InterruptedException, ExecutionException {
        long a = System.currentTimeMillis();
        List<String> urls = getUrls();
        List<File> fileList = downloadFiles(urls);
        File zipFile = zipFiles(fileList);

        InputStreamResource resource = new InputStreamResource(new FileInputStream(zipFile));

        System.out.println("耗时（毫秒）：" + (System.currentTimeMillis() - a));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + zipFile.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(zipFile.length())
                .body(resource);
    }

    private List<String> getUrls() {
        // 返回待下载的文件url列表
        // 此处返回两个样例url
        List<String> urls = new ArrayList<>(Arrays.asList(
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMLkuEdlgnAAAAAGk-LQo177.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKY2ECN4XAAAAAONlMz8995.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKY2EFSWKAAAAAONlMz8417.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKY2EWFDQAAAAAONlMz8219.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYyEXWw_AAAAAG_5s3I896.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYyEax01AAAAAONlMz8248.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYyEU1BiAAAAAPd7LVY221.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYuEEfAfAAAAAONlMz8579.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYuEVIsaAAAAAONlMz8599.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYqEfBUnAAAAAONlMz8340.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYqETCibAAAAAAL3lKw688.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYqEBYgKAAAAAPd7LVY763.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYmEWhVfAAAAAF29wK8769.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYiEUfeoAAAAANnF7Dg659.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYiEMP57AAAAAAGvGLY123.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYeEK2quAAAAAONlMz8015.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYeEJMdVAAAAAG_5s3I958.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYeEJJ_ZAAAAAG_5s3I299.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYaEXDO9AAAAAONlMz8204.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYaETSPLAAAAAG_5s3I326.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYaEAOy9AAAAAONlMz8753.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYaEECQHAAAAAONlMz8772.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYaEGFlaAAAAAONlMz8727.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYWERc7IAAAAAI_WYJg565.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYWEFz2LAAAAAONlMz8696.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYWEGGGlAAAAAONlMz8250.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYWEZ1QNAAAAAONlMz8730.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYSEJ98vAAAAAAGvGLY930.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYSEYLZNAAAAAG_5s3I634.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYSENK9XAAAAAG_5s3I596.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYOEF4MdAAAAAG_5s3I918.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYOEHPH7AAAAANnF7Dg216.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYOEYM0aAAAAAPd7LVY339.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYKEddyaAAAAAAL3lKw719.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYGEXWMOAAAAAPd7LVY315.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYCEXkEpAAAAAONlMz8309.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYCEQOZvAAAAAAGvGLY370.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYCEAVZ_AAAAAG_5s3I164.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKYCEaxHiAAAAANnF7Dg537.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKX-EAwjgAAAAAPd7LVY572.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKX6EbSwdAAAAAF29wK8821.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKX6EPCJFAAAAAAL3lKw862.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKX6EC4ixAAAAAONlMz8262.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKX6EHhqrAAAAAONlMz8355.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKX2EMVKkAAAAAPd7LVY949.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKXyEMX3zAAAAAONlMz8547.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKXyEX3pkAAAAAONlMz8645.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKXyETddkAAAAAG_5s3I510.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKXyEeJGMAAAAAI_WYJg656.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKXuEepXjAAAAAAGvGLY803.pdf",
                "https://tech.suitbim.com/stec-platform-doc/doc/wKhjGmSMKXuEKxx6AAAAAONlMz8920.pdf"
        ));
        return urls;
    }

    private List<File> downloadFiles(List<String> urls) throws IOException, InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUM);

        List<Callable<File>> callables = new ArrayList<>();
        for (String url : urls) {
            callables.add(() -> {
                Thread currentThread = Thread.currentThread();
                System.out.println("Downloading " + url + " with thread " + currentThread.getName());
                String fileName = getFileNameByUrl(url);
                String filePath = DOWNLOAD_PREFIX + fileName;
                File file = new File(filePath);
                FileUtils.copyURLToFile(new URL(url), file);
                System.out.println("Download finished: " + url);
                return file;
            });
        }

        List<Future<File>> futures = executorService.invokeAll(callables);

        List<File> fileList = new ArrayList<>();
        for (Future<File> future : futures) {
            File file = future.get();
            fileList.add(file);
        }

        executorService.shutdown();

        return fileList;
    }

    private File zipFiles(List<File> fileList) throws IOException {
        File zipFile = new File(ZIPFILE_NAME);
        FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

        for (File file : fileList) {
            FileInputStream fileInputStream = new FileInputStream(file);
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipOutputStream.putNextEntry(zipEntry);

            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = fileInputStream.read(buffer)) >= 0) {
                zipOutputStream.write(buffer, 0, length);
            }

            zipOutputStream.closeEntry();
            fileInputStream.close();
        }

        zipOutputStream.close();
        fileOutputStream.close();

        return zipFile;
    }

    private String getFileNameByUrl(String url) {
        // 从url中解析出文件名
        String[] parts = url.split("/");
        return parts[parts.length - 1];
    }

    /******************************************************************************************************************/

    @ApiOperation("风险管控评价表 模板下载")
    @RequestMapping(value = "/downloadCommentTemplate")
    public String downloadCommentTemplate(HttpServletResponse response) {
        try {
            String fileName = "风险管控评价表.docx";
            String templateName = "risk_control_evaluation_form_template.ftl";
            this.export(fileName, templateName, response);
            return "下载成功！";
        } catch (Exception e) {
            response.reset();
            e.printStackTrace();
            return "下载失败！";
        }

    }

    private void export(String fileName, String templateName, HttpServletResponse response) throws IOException {

        // 获取word模板的InputStream，方法可根据实际情况修改
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("templates" + File.separator + "riskSupervision" + File.separator + templateName);

        // 设置响应头word
        response.setContentType("application/msword");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

        // 从输入流读取数据并写入输出流
        IOUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();

//        if (StringUtils.isEmpty(fileName) || StringUtils.isEmpty(templateName) || ObjectUtils.isNull(response)) {
//            return;
//        }
//        try {
//            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("templates" + File.separator + "riskSupervision" + File.separator + templateName);
//
////            response.setContentType("application/msword;charset=UTF-8");
//////            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
////            response.setHeader("Content-Disposition", "attachment");
//            response.setContentType("application/octet-stream");
//            response.setHeader("Access-control-Expose-Headers", "attachment");
//            response.setHeader("attachment", URLEncoder.encode(fileName, "UTF-8"));
//            response.setCharacterEncoding("UTF-8");
//
//            File temp = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".docx");
//            FileUtils.copyInputStreamToFile(inputStream, temp);
//            String absolutePath = temp.getAbsolutePath();
//
//            FileInputStream fis = new FileInputStream(absolutePath);
//            OutputStream out = response.getOutputStream();
//            byte[] buffer = new byte[1024];
//            int len = 0;
//            //循环将输入流中的内容读取到缓冲区当中
//            while ((len = fis.read(buffer)) > 0) {
//                //输出缓冲区的内容到浏览器，实现文件下载
//                out.write(buffer, 0, len);
//            }
//            //关闭文件输入流
//            fis.close();
//            //关闭输出流
//            out.close();
//            // 删除缓存文件
//            if (temp.exists()) {
//                temp.delete();
//            }
//        } catch (Exception e) {
//            // 重置response
//            response.reset();
//            response.setContentType("application/json");
//            response.setCharacterEncoding("utf-8");
//            e.printStackTrace();
//        }

//        String string = this.getClass().getClassLoader().getResource("templates").getPath();
////        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("templates" + File.separator + "riskSupervision" + File.separator + templateName);
//
//        File file = new File(string+File.separator+templateName);
////        Template template = configuration.getTemplate(templateName);
//        // 响应参数
//        response.setContentType("application/octet-stream");
//        response.setHeader("Access-control-Expose-Headers", "attachment");
//        response.setHeader("attachment", URLEncoder.encode(fileName, "utf-8"));
//        // 文件生成
//        try (FileInputStream fis = new FileInputStream(file); OutputStream out = response.getOutputStream()) {
//            byte[] buffer = new byte[1024];
//            int len = 0;
//            //循环将输入流中的内容读取到缓冲区当中
//            while ((len = fis.read(buffer)) > 0) {
//                //输出缓冲区的内容到浏览器，实现文件下载
//                out.write(buffer, 0, len);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("下载文件错误==>"+e.getMessage());
//            response.reset();
//        }finally {
//            //关闭输出流
//            if (file.exists()) {
//                Files.delete(file.toPath());
//            }
//        }

    }

    /******************************************************************************************************************/

    @GetMapping("/docx")
    public ResponseEntity<byte[]> downloadDocx() throws IOException {
        ClassPathResource resource = new ClassPathResource("/templates/riskSupervision/risk_control_evaluation_form_template.docx");

        byte[] data = IOUtils.toByteArray(resource.getInputStream());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", URLEncoder.encode("评价.docx", "UTF-8"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<>(data, headers, HttpStatus.OK);

    }


    @PostMapping("/download-docx")
    public ResponseEntity<Resource> downloadDocxDocument() throws IOException {

        ClassPathResource resource = new ClassPathResource("/templates/riskSupervision/risk_control_evaluation_form_template.docx");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .body(resource);
    }


}
