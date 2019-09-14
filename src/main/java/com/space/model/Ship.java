package com.space.model;

import com.space.validator.YearRange;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.awt.*;
import java.util.Date;

@Entity
@Table(name = "ship")
public class Ship {

    public interface Create {}
    public interface Update {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(groups = {Create.class})
    @Size(min = 1, max = 50, groups = {Create.class, Update.class})
    private String name;

    @NotNull(groups = {Create.class})
    @Size(min = 1, max = 50, groups = {Create.class, Update.class})
    private String planet;

    @NotNull(groups = {Create.class})
    private String shipType;

    @NotNull(groups = {Create.class})
    @YearRange(min = 2800, max = 3019, groups = {Create.class, Update.class})
    private Date prodDate;

    private Boolean isUsed;

    @NotNull(groups = {Create.class})
    @DecimalMin(value = "0.01", groups = {Create.class, Update.class})
    @DecimalMax(value = "0.99", groups = {Create.class, Update.class})
    private Double speed;

    @NotNull(groups = {Create.class})
    @Min(value = 1, groups = {Create.class, Update.class})
    @Max(value = 9999, groups = {Create.class, Update.class})
    private Integer crewSize;

    private Double rating;

    public Ship() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlanet() {
        return planet;
    }

    public ShipType getShipType() {
        return shipType == null ? null : ShipType.valueOf(shipType);
    }

    public Date getProdDate() {
        return prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public Double getSpeed() {
        return speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", planet='" + planet + '\'' +
                ", shipType='" + shipType + '\'' +
                ", prodDate=" + prodDate +
                ", isUsed=" + isUsed +
                ", speed=" + speed +
                ", crewSize=" + crewSize +
                ", rating=" + rating +
                '}';
    }
}
