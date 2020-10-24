package com.polytech.mtonairserver.model.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "measure", schema = "mtonairserver", catalog = "")
public class MeasureEntity {
    private int idMeasure;
    private String measureName;

    @Id
    @Column(name = "id_measure", nullable = false)
    public int getIdMeasure() {
        return idMeasure;
    }

    public void setIdMeasure(int idMeasure) {
        this.idMeasure = idMeasure;
    }

    @Basic
    @Column(name = "measure_name", nullable = true, length = 25)
    public String getMeasureName() {
        return measureName;
    }

    public void setMeasureName(String measureName) {
        this.measureName = measureName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasureEntity that = (MeasureEntity) o;
        return idMeasure == that.idMeasure &&
                Objects.equals(measureName, that.measureName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMeasure, measureName);
    }
}
