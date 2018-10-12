package com.fiberhome.mapps.servicemanager.entity;

import javax.persistence.*;

@Table(name = "MC_UNIT_WEIGHT")
public class McUnitWeight {
    @Id
    private String unit;

    private Long weight;

    /**
     * @return unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * @return weight
     */
    public Long getWeight() {
        return weight;
    }

    /**
     * @param weight
     */
    public void setWeight(Long weight) {
        this.weight = weight;
    }
}