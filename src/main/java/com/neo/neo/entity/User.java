package com.neo.neo.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @NotBlank(message = "Nome é obrigatorio")
    String name;

    @NotBlank(message = "Cpf é obrigatorio")
    String cpf;

    @NotBlank(message = "Email é obrigatorio")
    @Email(message = "Email nao é valido")
    String email;

    @NotBlank(message = "Data de nascimento é obrigatorio")
    String dateOfBirth;

    @NotBlank(message = "Senha é obrigatorio")
    String password;

    @CreatedDate
    @Column(updatable = false)
    LocalDateTime createdAt;

    @LastModifiedDate
    LocalDateTime updatedAt;

    public int getAge() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate birthDate = LocalDate.parse(this.dateOfBirth, formatter);
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

}
