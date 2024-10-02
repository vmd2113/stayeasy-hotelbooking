package com.duongw.stayeasy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "room_categories")
public class RoomCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name", unique = true)
    private String categoryName;

    @JsonIgnore
    @OneToMany(mappedBy = "roomCategory", cascade = CascadeType.ALL)
    private List<Room> roomList;


}
