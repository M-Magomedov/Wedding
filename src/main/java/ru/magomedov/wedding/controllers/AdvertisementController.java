package ru.magomedov.wedding.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.magomedov.wedding.models.Advertisement;
import ru.magomedov.wedding.service.AdvertisementService;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;


    @GetMapping("/")
    public String showAdvertisements(Principal principal ,Model model){ //Показать все услуги
        model.addAttribute("advertisements", advertisementService.listAdvertisement());
        model.addAttribute("user", advertisementService.getUserByPrincipal(principal));

        return "advertisement";
    }

    @GetMapping("/advertisement/{id}")
    public String InfoAdvertisement(@PathVariable Long id, Principal principal ,Model model){
        model.addAttribute("advertisement", advertisementService.getAdvertisementById(id));
        Advertisement advertisement = advertisementService.getAdvertisementById(id);
        model.addAttribute("images", advertisement.getImages());
        model.addAttribute("user", advertisement.getUser());
        return "advertisement-info";
    }

    @PostMapping("/advertisement/create") //Создать услугу
    public String createAdvertisementService (@RequestParam("file1") MultipartFile file1,
                                              @RequestParam("file2") MultipartFile file2,
                                              @RequestParam("file3") MultipartFile file3,
                                              Advertisement advertisement,
                                              Principal principal) throws IOException {
        advertisementService.saveAdvertisement(principal ,advertisement, file1, file2, file3);
        return "redirect:/";
    }

    @GetMapping("/advertisement/delete/{id}")  //Удалить услугу
    public String deleteAdvertisement(@PathVariable Long id, Model model){
        advertisementService.deleteAdvertisement(id);
        model.addAttribute("advertisement", advertisementService.getAdvertisementById(id));
        return "redirect:/";
    }
}
