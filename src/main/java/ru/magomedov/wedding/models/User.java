package ru.magomedov.wedding.models;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.magomedov.wedding.models.enums.Role;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table (name = "users")
@Data
public class User implements UserDetails {  //UserDetails - класс-обертка над User, чтобы работать с User в Spring Security
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "name")
    private String name;
    @Column(name = "active")
    private boolean active;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private Image image;
    
    @Column(name = "password", length = 1000)
    private String password;
    
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", //создаем таблицу с именем user_role
    joinColumns = @JoinColumn(name = "user_id")) //добавляем поле user_id
    @Enumerated(EnumType.STRING)  //преобразовываем enum к типу String
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Advertisement> advertisements = new ArrayList<>();

    private LocalDateTime dateOfCreated;

    @PrePersist
    public void init(){ //Время создания пользователя
        dateOfCreated = LocalDateTime.now();
    }

    //security

    public boolean isAdmin(){
        return roles.contains(Role.ROLE_ADMIN);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //Получаем роли из БД и видим список права каждого пользователя
        return roles;
    }

    @Override
    public String getUsername() { //Поле по которому унифицируем пользователя
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {  //Текущий аккаунт активен, т.е. он действительный
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { //Текущий аккаунт не заблокирован
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { //Пароль не просрочен
        return true;
    }

    @Override
    public boolean isEnabled() { //Аккаунт включен и работает
        return active;
    }
}
