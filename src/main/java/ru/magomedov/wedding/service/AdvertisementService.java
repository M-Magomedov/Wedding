package ru.magomedov.wedding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.magomedov.wedding.models.Advertisement;
import ru.magomedov.wedding.models.Image;
import ru.magomedov.wedding.models.User;
import ru.magomedov.wedding.repositories.AdvertisementRepository;
import ru.magomedov.wedding.repositories.UserRepository;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;

    public List<Advertisement> listAdvertisement() {
        return advertisementRepository.findAll();
    }

    public void saveAdvertisement(Principal principal, Advertisement advertisement, //Principal - Обертка состояния user
                                  MultipartFile file1, MultipartFile file2,
                                  MultipartFile file3) throws IOException {
        advertisement.setUser(getUserByPrincipal(principal));
        Image image1;
        Image image2;
        Image image3;
        if (file1.getSize() != 0){
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            advertisement.addImageToAdvertisement(image1);
        }
        if (file2.getSize() != 0){
            image2 = toImageEntity(file2);
            advertisement.addImageToAdvertisement(image2);
        }
        if (file3.getSize() != 0){
            image3 = toImageEntity(file3);
            advertisement.addImageToAdvertisement(image3);
        }

        log.info("Saving new Advertisement. Title: {}; Author email: {}",
                advertisement.getTitle(), advertisement.getUser().getEmail());

        //Получим Id превьюшней фотографии
        Advertisement advertisementDb = advertisementRepository.save(advertisement);
        advertisementDb.setPreviewImageId(advertisementDb.getImages().get(0).getId());
        advertisementRepository.save(advertisement);
    }

    public User getUserByPrincipal(Principal principal) {
        if(principal==null)
            return new User(); //Возвращаем ново созданного user, чтобы неавторизованным пользователям
                                // не показывать форма для добавления товара
        return userRepository.findByEmail(principal.getName());
    }

    private Image toImageEntity(MultipartFile file1) throws IOException { //Преобразование файла в фотографию
        Image image = new Image();
        image.setName(file1.getName());
        image.setOriginalFileName(file1.getOriginalFilename());
        image.setContentType(file1.getContentType());
        image.setSize(file1.getSize());
        image.setBytes(file1.getBytes());
        return image;
    }

    public void deleteAdvertisement(Long id) {
        advertisementRepository.deleteById(id);
    }

    public Advertisement getAdvertisementById(Long id) {
        return advertisementRepository.findById(id).orElse(null);
    }
}
