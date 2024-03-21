package org.example.multiplelogin.domain.owner.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "owners")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    private boolean storeStatus;
    private int point = 10000;

    @Builder
    public Owner(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public void hasStore() {
        this.storeStatus = true;
    }

    public void deleteStore() {
        this.storeStatus = false;
    }

}
