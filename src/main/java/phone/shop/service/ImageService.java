package phone.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import phone.shop.dto.ImageDTO;
import phone.shop.entity.ImageEntity;
import phone.shop.exp.ItemNotFoundException;
import phone.shop.repository.ImageRepository;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;
    @Value("${image.folder.url}")
    private String imageFolderUrl;
    @Value("${image.url}")
    private String imageUrl;

    private Path root = Paths.get(imageFolderUrl);

    public ImageDTO saveToSystem(MultipartFile file) {
        try {
            String filePath = getYmDString(); // 2021/07/13
            String fileType = file.getContentType().split("/")[1]; // png, jpg, jpeg
            String fileToken = UUID.randomUUID().toString();
            String fileUrl = filePath + fileToken + "." + fileType; // sdasdasdasdasdas.png

            File folder = new File(imageFolderUrl + filePath); //  uploads/2021/07/13
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // save to system
            Path path = this.root.resolve(fileUrl);
            Files.copy(file.getInputStream(), path);

            ImageDTO dto = this.createImage(file, filePath, fileType, fileToken);

            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public Resource load(String token) {
        try {
            ImageEntity image = this.getImage(token);
            Path file = root.resolve(image.getToke() + "." + image.getType());
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public byte[] getImg(String token) {
        try {
            ImageEntity image = this.getImage(token);
            String path = imageFolderUrl + image.getToke() + "." + image.getType();

            byte[] imageInByte;

            BufferedImage originalImage;
            try {
                originalImage = ImageIO.read(new File(path));
            } catch (Exception e) {
                return new byte[0];
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(originalImage, "png", baos);

            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);

        return year + "/" + month + "/" + day + "/";
    }

    public void deleteAttach(Integer id) {
//        if (id == null) return;
//
//        Attach attach = this.getById(id);
//
//        String attachName = attach.getName();
//        String path = attach.getPath();
//
//        File file = new File(attachFolder + path + attachName);
//
//        if (file.exists()) {
//            file.delete();
//        }
    }

    private ImageDTO createImage(MultipartFile file, String filePath, String fileType, String fileToken) {
        long size = file.getSize();
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setType(fileType);
        imageEntity.setSize(size);
        imageEntity.setPath(filePath);
        imageEntity.setToke(fileToken);
        imageEntity.setCreatedDate(LocalDateTime.now());

        this.imageRepository.save(imageEntity);

        ImageDTO dto = new ImageDTO();
        dto.setPath(filePath);
        dto.setType(fileType);
        dto.setSize(size);
        dto.setToken(fileToken);
        dto.setUrl(imageUrl + fileToken);
        dto.setId(imageEntity.getId());
        return dto;

    }

    public ImageEntity getImage(String token) {
        Optional<ImageEntity> optional = this.imageRepository.findByToken(token);
        return optional.orElseThrow(() -> new ItemNotFoundException("Item Not Found"));

    }
}
