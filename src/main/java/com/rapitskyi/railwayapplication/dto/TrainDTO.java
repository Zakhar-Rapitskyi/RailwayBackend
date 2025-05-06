package com.rapitskyi.railwayapplication.dto;

import com.rapitskyi.railwayapplication.entity.Train;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainDTO {
    private Integer id;
    private String name;
    private Integer totalCars;

    public static TrainDTO fromEntity(Train train) {
        if (train == null) return null;
        
        TrainDTO dto = new TrainDTO();
        dto.setId(train.getId());
        dto.setName(train.getName());
        dto.setTotalCars(train.getTotalCars());
        return dto;
    }

    public Train toEntity() {
        Train train = new Train();
        train.setId(this.id);
        train.setName(this.name);
        train.setTotalCars(this.totalCars);
        return train;
    }
}