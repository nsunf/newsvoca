package com.nsunf.newsvoca.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {
    public String uploadFile(String uploadPath, String oriFilename, byte[] fileData) throws IOException {
        UUID uuid = UUID.randomUUID();

        String ext = oriFilename.substring(oriFilename.lastIndexOf("."));
        String savedFilename = uuid.toString() + ext;
        String fileUploadFullUrl = uploadPath + "/" + savedFilename;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);

        fos.write(fileData);
        fos.flush();;
        fos.close();

        return savedFilename;
    }

    public void deleteFile(String fileUrl) {
        File file = new File(fileUrl);
        if (file.exists())
            file.delete();
    }
}
