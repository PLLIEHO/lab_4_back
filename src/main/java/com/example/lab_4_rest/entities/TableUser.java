package com.example.lab_4_rest.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users", schema = "for_labs", catalog = "web")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TableUser {
    @Column(name = "point_user", columnDefinition = "TEXT", nullable = false)
    private String point_user;

    @Column(name = "password", columnDefinition="TEXT", nullable = false)
    private String password;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableUser elements = (TableUser) o;

        return elements.point_user.equals(point_user);
    }

    @Override
    public int hashCode() {
        int result1;
        long temp;
        temp = point_user.hashCode();
        result1 = (int) (temp ^ (temp >>> 32));
        temp = password.hashCode();
        result1 = 31 * result1 + (int) (temp ^ (temp >>> 32));
        return result1;
    }
}
