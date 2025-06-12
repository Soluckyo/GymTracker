package org.lib.trainingservice.service;

import org.lib.trainingservice.dto.CreateTrainingDto;
import org.lib.trainingservice.dto.SignUpForTrainingDto;
import org.lib.trainingservice.dto.TrainingDTO;
import org.lib.trainingservice.entity.Room;
import org.lib.trainingservice.entity.Training;
import org.lib.trainingservice.entity.TrainingType;
import org.lib.trainingservice.exception.TrainingNotFoundException;
import org.lib.trainingservice.exception.TrainingOverlapException;
import org.lib.trainingservice.kafka.KafkaProducer;
import org.lib.trainingservice.mapper.TrainingMapper;
import org.lib.trainingservice.repository.TrainingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TrainingService implements ITrainingService {

    private final TrainingRepository trainingRepository;
    private final KafkaProducer kafkaProducer;

    public TrainingService(TrainingRepository trainingRepository, KafkaProducer kafkaProducer) {
        this.trainingRepository = trainingRepository;
        this.kafkaProducer = kafkaProducer;
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


    @Override
    @Transactional
    public TrainingDTO createGroupTraining(CreateTrainingDto createTrainingDto) {

        LocalDateTime startTime = createTrainingDto.getStartTime();
        LocalDateTime endTime = startTime.plus(createTrainingDto.getDuration());
        Room room = createTrainingDto.getRoom();

        List<Training> overlappingFromTrainer = trainingRepository.findOverlappingTrainingsForTrainer(
                createTrainingDto.getTrainingId(), startTime, endTime);
        if(!overlappingFromTrainer.isEmpty()) {
            throw new TrainingOverlapException("У тренера в это время уже есть тренировка!");
        }

        List<Training> overlapping = trainingRepository.findOverlappingTrainings(
                startTime, endTime, TrainingType.GROUP, room);
        if(!overlapping.isEmpty()){
            throw new TrainingOverlapException("В это время уже есть групповая тренировка!");
        }

        Training training = new Training();
        training.setType(TrainingType.GROUP);
        training.setTitle(createTrainingDto.getTitle());
        training.setTrainingId(createTrainingDto.getTrainingId());
        training.setRoom(createTrainingDto.getRoom());
        training.setStartTime(createTrainingDto.getStartTime());
        training.setEndTime(training.getStartTime().plus(training.getDuration()));
        training.setDuration(createTrainingDto.getDuration());

        Training savedTraining = trainingRepository.save(training);
        return TrainingMapper.toTrainingDTO(savedTraining);
    }

    @Override
    public TrainingDTO createPersonalTraining(CreateTrainingDto createTrainingDto) {
        LocalDateTime startTime = createTrainingDto.getStartTime();
        LocalDateTime endTime = startTime.plus(createTrainingDto.getDuration());

        List<Training> overlappingFromTrainer = trainingRepository.findOverlappingTrainingsForTrainer(
                createTrainingDto.getTrainingId(), startTime, endTime);
        if(!overlappingFromTrainer.isEmpty()) {
            throw new TrainingOverlapException("У тренера в это время уже есть тренировка!");
        }

        Training training = new Training();
        training.setType(TrainingType.PERSONAL);
        //TODO: добавить возможность получения имени тренера!
        training.setTitle("Персональная тренировка с тренером" + createTrainingDto.getTitle() );
        training.setTrainingId(createTrainingDto.getTrainingId());
        training.setStartTime(createTrainingDto.getStartTime());
        training.setEndTime(training.getStartTime().plus(training.getDuration()));
        training.setDuration(createTrainingDto.getDuration());
        training.setRoom(Room.COMMON_ROOM);

        Training savedTraining = trainingRepository.save(training);
        kafkaProducer.sendTrainerInfoRequest(savedTraining.getTrainerId(), savedTraining.getTrainingId());

        return TrainingMapper.toTrainingDTO(savedTraining);
    }

    @Override
    public void deleteTraining(UUID trainingId) {

    }

    @Override
    public TrainingDTO signUpForTraining(SignUpForTrainingDto signUpForTrainingDto) {
        return null;
    }

    @Override
    public Training findTrainingById(UUID trainingId) {
        return null;
    }
}
