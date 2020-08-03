package com.qurlapi.qurlapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "urls")
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String url;
}
