package com.fiberhome.mapps.servicemanager.entity;

import javax.persistence.*;

@Table(name = "mc_value_field")
public class McValueField {
    @Id
    private String id;

    @Column(name = "measurement_id")
    private String measurementId;

    private String field;

    private String name;

    private String unit;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return measurement_id
     */
    public String getMeasurementId() {
        return measurementId;
    }

    /**
     * @param measurementId
     */
    public void setMeasurementId(String measurementId) {
        this.measurementId = measurementId;
    }

    /**
     * @return field
     */
    public String getField() {
        return field;
    }

    /**
     * @param field
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

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
}