package com.bulkupload.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor  // ✅ THIS IS MANDATORY FOR JPA/HIBERNATE
@AllArgsConstructor
@Builder
public class Students {

    @Id
    private int std_id;

    private String name;

    private String email;

    private String location;
}
