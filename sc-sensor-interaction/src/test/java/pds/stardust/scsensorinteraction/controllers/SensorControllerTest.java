package pds.stardust.scsensorinteraction.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import pds.stardust.scsensorinteraction.config.JasyptConfig;
import pds.stardust.scsensorinteraction.entities.SensorEntity;
import pds.stardust.scsensorinteraction.entities.TopicEntity;
import pds.stardust.scsensorinteraction.exceptions.BadRequestException;
import pds.stardust.scsensorinteraction.exceptions.ServiceException;
import pds.stardust.scsensorinteraction.services.SensorService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = {JasyptConfig.class})
class SensorControllerTest {

    @Mock
    private SensorService sensorService;

    @InjectMocks
    private SensorController sensorController;

    @Test
    void givenSensor_whenCreate_thenSucceed() {
        TopicEntity topic = new TopicEntity("label");
        SensorEntity sensor = new SensorEntity(topic, "message");

        TopicEntity insertedTopic = new TopicEntity("topic-1", "label");
        SensorEntity insertedSensor = new SensorEntity("sensor-1", insertedTopic, "message");

        when(sensorService.save(any())).thenReturn(insertedSensor);

        SensorEntity actual = sensorController.create(sensor);

        assertNotNull(actual);
        assertEquals("sensor-1", actual.getId());
        assertEquals("topic-1", actual.getTopic().getId());
    }

    @Test
    void givenServiceException_whenCreate_thenFail() {
        when(sensorService.save(any())).thenThrow(new BadRequestException("sensor is null"));

        BadRequestException thrown = assertThrows(BadRequestException.class, () -> sensorController.create(null));
        assertEquals("sensor is null", thrown.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideTestCasesForGetSensors")
    void givenExistingSensor_whenGetSensors_thenSucceed(List<SensorEntity> entities) {
        when(sensorService.list()).thenReturn(entities);

        List<SensorEntity> actual = sensorService.list();

        assertEquals(entities.size(), actual.size());
    }

    @Test
    void givenServiceException_whenGetSensors_thenFail() {
        when(sensorService.list()).thenThrow(new ServiceException("error when retreiving sensors"));

        ServiceException thrown = assertThrows(ServiceException.class, () -> sensorController.getSensors());
        assertEquals("error when retreiving sensors", thrown.getMessage());
    }

    private static Stream<Arguments> provideTestCasesForGetSensors() {
        List<SensorEntity> data = new ArrayList<>();
        TopicEntity topic1 = new TopicEntity("topic-1", "label");
        SensorEntity sensor1 = new SensorEntity("sensor-1", topic1, "message");
        TopicEntity topic2 = new TopicEntity("topic-2", "label");
        SensorEntity sensor2 = new SensorEntity("sensor-2", topic2, "message");
        TopicEntity topic3 = new TopicEntity("topic-3", "label");
        SensorEntity sensor3 = new SensorEntity("sensor-3", topic3, "message");
        data.add(sensor1);
        data.add(sensor2);
        data.add(sensor3);

        return Stream.of(
                Arguments.of(data),
                Arguments.of(new ArrayList<>())
        );
    }

}