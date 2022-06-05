package ru.magomedov.wedding.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magomedov.wedding.models.Advertisement;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
}
