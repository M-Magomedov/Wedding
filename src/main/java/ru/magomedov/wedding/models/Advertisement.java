package ru.magomedov.wedding.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Advertisement")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "price")
    private int price;

    @Column(name = "city")
    private String city;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "advertisement")
    private List<Image> images = new ArrayList<>();

    private Long previewImageId; //Id изображения, которая показывается вначале

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn
    private User user;

    private LocalDate dateOfCreated;

    @PrePersist
    private void init(){  //Время создания услуги
        dateOfCreated = LocalDate.now();
    }

    public void addImageToAdvertisement(Image image){
        image.setAdvertisement(this);
        images.add(image);
    }
}
