package com.david.RoomReservation.dto.room;

import com.david.RoomReservation.model.constans.RoomType;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomPatchRequest {
    @Min(1)
    private Integer roomNumber;
    @Min(1)
    private Integer capacity;
    private RoomType roomType;

    public RoomPatchRequest() {
    }

    public RoomPatchRequest(Integer roomNumber, Integer capacity, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.roomType = roomType;
    }
}
