package com.example.lab_4_rest.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="elements", schema = "for_labs", catalog = "web")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TableElement {

    @Column(name = "x", nullable = false, precision = 0)
    private double x;

    @Column(name = "y", nullable = false, precision = 0)
    private double y;

    @Column(name = "r", nullable = false, precision = 0)
    private double r;

    @Column(name = "result", nullable = false)
    private boolean result;

    @Column(name = "username", nullable = false)
    private String username;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableElement elements = (TableElement) o;

        if (Double.compare(elements.x, x) != 0) return false;
        if (Double.compare(elements.y, y) != 0) return false;
        if (Double.compare(elements.r, r) != 0) return false;
        if (result != elements.result) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result1;
        long temp;
        temp = Double.doubleToLongBits(x);
        result1 = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result1 = 31 * result1 + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(r);
        result1 = 31 * result1 + (int) (temp ^ (temp >>> 32));
        return result1;
    }
}
