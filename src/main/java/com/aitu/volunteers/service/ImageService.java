package com.aitu.volunteers.service;

import com.aitu.volunteers.model.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

@Service
public class ImageService {

    @Value("${image.storage.location}")
    private String location;

    @Value("${url}")
    private String url;

    private String store(MultipartFile file, String path) {
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

    public String storePostImage(MultipartFile file, User user) {
        if(!isImage(file.getOriginalFilename())) return null;
        String fullPath = location + "/" + user.getUserSub();
        return url + "/files/" + user.getUserSub() + "/" + store(file, fullPath);
    }

    public Resource loadPostImageAsResource(String userSub, String filename) {
        try {
            Path file = Paths.get(location).resolve(userSub).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isImage(String str)
    {
        String regex = "([^\\s]+(\\.(?i)(jpe?g|png|gif))$)";
        Pattern p = Pattern.compile(regex);

        if (str == null) {
            return false;
        }

        Matcher m = p.matcher(str);
        return m.matches();
    }
}
