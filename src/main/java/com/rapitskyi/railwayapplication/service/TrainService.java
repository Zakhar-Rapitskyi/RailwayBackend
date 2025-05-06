package com.rapitskyi.railwayapplication.service;

import com.rapitskyi.railwayapplication.dto.TrainDTO;
import com.rapitskyi.railwayapplication.entity.Train;
import com.rapitskyi.railwayapplication.exception.CustomExceptions.BadRequestException;
import com.rapitskyi.railwayapplication.exception.CustomExceptions.ResourceNotFoundException;
import com.rapitskyi.railwayapplication.repository.TrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainService {

    private final TrainRepository trainRepository;

    public List<TrainDTO> getAllTrains() {
        return trainRepository.findAll().stream()
                .map(TrainDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public TrainDTO getTrainById(Integer id) {
        Train train = trainRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Train not found with id: " + id));
        return TrainDTO.fromEntity(train);
    }

    public TrainDTO createTrain(Train train) {
        validateTrain(train);
        Train savedTrain = trainRepository.save(train);
        return TrainDTO.fromEntity(savedTrain);
    }

    public TrainDTO updateTrain(Integer id, Train trainDetails) {
        Train train = trainRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Train not found with id: " + id));
        
        validateTrain(trainDetails);
        
        train.setName(trainDetails.getName());
        train.setTotalCars(trainDetails.getTotalCars());
        
        Train updatedTrain = trainRepository.save(train);
        return TrainDTO.fromEntity(updatedTrain);
    }

    public void deleteTrain(Integer id) {
        if (!trainRepository.existsById(id)) {
            throw new ResourceNotFoundException("Train not found with id: " + id);
        }
        trainRepository.deleteById(id);
    }
    
    private void validateTrain(Train train) {
        if (train.getTotalCars() <= 0) {
            throw new BadRequestException("Train must have at least one car");
        }
    }
}