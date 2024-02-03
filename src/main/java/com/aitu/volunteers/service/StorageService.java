package com.aitu.volunteers.service;

import com.aitu.volunteers.model.User;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {

    @Value("${file.storage.location}")
    private String location;

    @Value("${url}")
    private String url;

    private String storeFile(MultipartFile file, String path) {
        File uploadDir = new File(path);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String filename = UUID.randomUUID().toString();
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String absolutePath = uploadDir + "/" + filename + "." + extension;

        try {
            file.transferTo(new File(absolutePath));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filename + "." + extension;
    }

    private boolean deleteFile(Path path) {
        File file = new File(path.toUri());
        return file.delete();
    }

    public String storePostImage(User user, MultipartFile file) {
        if(!isImage(file.getOriginalFilename())) return null;
        String fullPath = location + "/" + user.getUserSub();
        return url + "/files/" + user.getUserSub() + "/" + storeFile(file, fullPath);
    }
    //application/pdf
    //image/png
    public String storeUserCertificate(User user, MultipartFile file) {
        if(!isCertificate(file)) return null;
        String fullPath = location + "/" + user.getUserSub() + "/certificate";
        return url + "/files/" + user.getUserSub() + "/certificate/" + storeFile(file, fullPath);
    }

    public boolean deleteUserCertificate(User user) {
        return deleteFile(getUserCertificatePath(user));
    }

    private Resource loadFileAsResource(Path path) {
        try {
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Resource loadPostImageAsResource(String userSub, String filename) {
       return loadFileAsResource(Paths.get(location).resolve(userSub).resolve(filename));
    }

    public Resource loadUserCertificateAsResource(User user) {
        return loadFileAsResource(getUserCertificatePath(user));
    }

    private Path getUserCertificatePath(User user) {
        String certificateUrl = user.getCertificate().getCertificateUrl();
        String[] split = certificateUrl.split("/");
        Path path = Paths.get(location).resolve(split[split.length - 3]).resolve(split[split.length - 2]).resolve(split[split.length - 1]);
        return path;
    }

    private boolean isCertificate(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType.equals("image/png") || contentType.equals("application/pdf");
    }

    private static boolean isImage(String str) {
        String regex = "([^\\s]+(\\.(?i)(jpe?g|png|gif))$)";
        Pattern p = Pattern.compile(regex);

        if (str == null) {
            return false;
        }

        Matcher m = p.matcher(str);
        return m.matches();
    }
}
