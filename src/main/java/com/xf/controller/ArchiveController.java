package com.xf.controller;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;

@RestController
public class ArchiveController {

    @PostMapping("/upload-archive")
    public String handleArchiveUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "上传失败：文件为空";
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return "上传失败：文件名异常";
        }

        File tempFile = null;
        File tempExtractDir = null;

        try {
            // 1. 保存上传的压缩包为临时文件
            tempFile = File.createTempFile("archive_", ".tmp");
            try (InputStream in = file.getInputStream();
                 FileOutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            // 2. 创建临时解压目录
            String tempDirPath = System.getProperty("java.io.tmpdir") +
                    File.separator + "extract_" + System.currentTimeMillis();
            tempExtractDir = new File(tempDirPath);
            if (!tempExtractDir.exists()) {
                tempExtractDir.mkdirs();
            }

            // 3. 解压并上传每个文件
            System.out.println("\n===== 开始解压并上传文件 =====");

            String filenameLower = originalFilename.toLowerCase();
            if (filenameLower.endsWith(".zip")) {
                unzipAndUploadZip(tempFile, tempExtractDir);

            } else if (filenameLower.endsWith(".7z")) {
                unzipAndUpload7z(tempFile, tempExtractDir);

            } else {
                return "不支持的文件格式。请上传 ZIP 或 7Z 文件";
            }

            System.out.println("===== 所有文件上传完成 =====\n");

            return "解压并上传成功！每个文件都已单独上传";

        } catch (Exception e) {
            e.printStackTrace();
            return "操作失败: " + e.getMessage();
        } finally {
            // 4. 清理临时文件和目录
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
            if (tempExtractDir != null && tempExtractDir.exists()) {
                deleteDirectory(tempExtractDir); // 递归删除
            }
        }
    }

    /**
     * 解压 ZIP 并逐个上传文件
     */
    private void unzipAndUploadZip(File zipFile, File extractDir) throws Exception {
        try (ZipFile zf = new ZipFile(zipFile)) {
            Enumeration<ZipArchiveEntry> entries = zf.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();

                // 打印结构
                printEntryStructure(entry.getName(), entry.isDirectory());

                // 如果是目录，跳过（只上传文件）
                if (entry.isDirectory()) {
                    continue;
                }

                // 解压单个文件
                File outputFile = new File(extractDir, entry.getName());
                outputFile.getParentFile().mkdirs(); // 创建父目录

                try (InputStream in = zf.getInputStream(entry);
                     FileOutputStream out = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }

                // 上传这个文件
                System.out.println("  → 上传文件: " + entry.getName());
                uploadLocalFile(outputFile);
            }
        }
    }

    /**
     * 解压 7Z 并逐个上传文件
     */
    private void unzipAndUpload7z(File sevenZFile, File extractDir) throws Exception {
        try (SevenZFile sevenZFileReader = new SevenZFile(sevenZFile)) {
            SevenZArchiveEntry entry;
            while ((entry = sevenZFileReader.getNextEntry()) != null) {
                // 打印结构
                printEntryStructure(entry.getName(), entry.isDirectory());

                // 如果是目录，跳过（只上传文件）
                if (entry.isDirectory()) {
                    continue;
                }

                // 解压单个文件
                File outputFile = new File(extractDir, entry.getName());
                outputFile.getParentFile().mkdirs(); // 创建父目录

                try (FileOutputStream out = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = sevenZFileReader.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }

                // 上传这个文件
                System.out.println("  → 上传文件: " + entry.getName());
                uploadLocalFile(outputFile);
            }
        }
    }

    /**
     * 打印条目结构（层级缩进）
     */
    private void printEntryStructure(String fullPath, boolean isDirectory) {
        String[] pathParts = fullPath.split("[/\\\\]");
        int depth = pathParts.length - 1;

        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            indent.append("  ");
        }

        String name = pathParts.length > 0 ? pathParts[pathParts.length - 1] : fullPath;
        String type = isDirectory ? "[DIR] " : "[FILE] ";
        System.out.println(indent + type + name);
    }

    /**
     * 递归删除目录
     */
    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }

    /**
     * 你的文件上传方法 - 针对每个文件单独调用
     */
    private void uploadLocalFile(File localFile) {
        // 这里实现你的上传逻辑
        // 每个文件都会单独调用这个方法
        System.out.println("    [执行上传] " + localFile.getName() + " (" + localFile.length() + " bytes)");

        // 实际使用时，这里应该是你的具体上传实现
        // 比如：
        // ossClient.putObject(bucketName, remotePath + localFile.getName(), localFile);
        // 或者
        // ftpClient.storeFile(localFile.getName(), new FileInputStream(localFile));
    }
}
