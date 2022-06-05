package ru.magomedov.wedding.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magomedov.wedding.models.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
