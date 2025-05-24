package org.lib.trainingservice.service;

import org.lib.trainingservice.dto.CreateTrainingDto;
import org.lib.trainingservice.dto.SignUpForTrainingDto;
import org.lib.trainingservice.dto.TrainingDTO;
import org.lib.trainingservice.entity.Training;
import org.lib.trainingservice.entity.TrainingType;
import org.lib.trainingservice.exception.TrainingNotFoundException;
import org.lib.trainingservice.mapper.TrainingMapper;
import org.lib.trainingservice.repository.TrainingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TrainingService implements ITrainingService {

    private final TrainingRepository trainingRepository;

    public TrainingService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    public Page<TrainingDTO> getAllTrainings(Pageable pageable) {
        Page<Training> allTrainings = trainingRepository.findAll(pageable);
        return allTrainings.map(TrainingMapper::toTrainingDTO);
    }

    @Override
    public Page<TrainingDTO> getAllTrainingsByUserId(UUID userId, Pageable pageable) {
        Page<Training> allTrainingByUserId = trainingRepository.findAllByUsersId(userId, pageable);
        return allTrainingByUserId.map(TrainingMapper::toTrainingDTO);
    }

    @Override
    public Page<TrainingDTO> getAllGroupTrainings(Pageable pageable) {
        Page<Training> allGroupTrainings = trainingRepository.findAllByType(TrainingType.GROUP, pageable);
        return allGroupTrainings.map(TrainingMapper::toTrainingDTO);
    }

    @Override
    public Page<TrainingDTO> getAllGroupTrainingsByUserId(UUID userId, Pageable pageable) {
        Page<Training> groupTrainingsByUserId = trainingRepository.findAllByUsersIdAndType(userId, TrainingType.GROUP, pageable);
        return groupTrainingsByUserId.map(TrainingMapper::toTrainingDTO);
    }

    @Override
    public TrainingDTO getTrainingById(UUID trainingId) {
        Training training = trainingRepository.findById(trainingId).orElseThrow(
                () -> new TrainingNotFoundException(String.format("Тренировка с Id: %s не найдена!", trainingId))
        );
        return TrainingMapper.toTrainingDTO(training);
    }


    //TODO: Продумать на счет залов?, ограничения кол-ва групповых тренировок в одно и тоже время
    @Override
    public TrainingDTO createGroupTraining(CreateTrainingDto createTrainingDto) {
        Training training = new Training();
        training.setType(TrainingType.GROUP);
        training.setTitle(createTrainingDto.getTitle());
        training.setTrainingId(createTrainingDto.getTrainingId());
        training.setStartDate(createTrainingDto.getStartTime());
        training.setDuration(createTrainingDto.getDuration());
        return null;
    }

    //TODO: продумать логику проверки доступности тренера!
    @Override
    public TrainingDTO createPersonalTraining(CreateTrainingDto createTrainingDto) {
        Training training = new Training();
        training.setType(TrainingType.PERSONAL);
        //TODO: добавить возможность получения имени тренера!
        training.setTitle("Персональная тренировка с тренером" + createTrainingDto.getTitle() );
        training.setTrainingId(createTrainingDto.getTrainingId());
        training.setStartDate(createTrainingDto.getStartTime());
        training.setDuration(createTrainingDto.getDuration());

        return null;
    }

    @Override
    public void deleteTraining(UUID trainingId) {

    }

    @Override
    public TrainingDTO signUpForTraining(SignUpForTrainingDto signUpForTrainingDto) {
        return null;
    }
}
