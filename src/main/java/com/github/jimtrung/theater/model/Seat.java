package com.github.jimtrung.theater.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "seats")
public class Seat {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  
  @Column(name = "auditorium_id")
  private UUID auditoriumId;
  
  @Column(name = "row")
  private String row; 
  
  private Integer number;
  
  @Column(name = "is_booked")
  private Boolean isBooked = false;

  private OffsetDateTime updatedAt;
  private OffsetDateTime createdAt;

  public Seat() {}
  public Seat(UUID id, UUID auditoriumId, String row, Integer number, Boolean isBooked, OffsetDateTime updatedAt, OffsetDateTime createdAt) {
    this.id = id;
    this.auditoriumId = auditoriumId;
    this.row = row;
    this.number = number;
    this.isBooked = isBooked;
    this.updatedAt = updatedAt;
    this.createdAt = createdAt;
  }

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }

  public UUID getAuditoriumId() { return auditoriumId; }
  public void setAuditoriumId(UUID auditoriumId) { this.auditoriumId = auditoriumId; }

  public String getRow() { return row; }
  public void setRow(String row) { this.row = row; }

  public Integer getNumber() { return number; }
  public void setNumber(Integer number) { this.number = number; }

  public Boolean getIsBooked() { return isBooked; }
  public void setIsBooked(Boolean isBooked) { this.isBooked = isBooked; }

  public OffsetDateTime getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

  public OffsetDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
