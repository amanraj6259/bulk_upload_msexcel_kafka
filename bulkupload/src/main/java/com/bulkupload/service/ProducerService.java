package com.bulkupload.service;
import com.bulkupload.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProducerService {

    private final KafkaTemplate<String,Student> kafkaTemplate;

    @Value("${spring.kafka.topic.driver-location}")
    private String topic;
    public void sendStudent(Student student)
    {
        System.out.println("Student inside ProducerService" + student.toString());
        kafkaTemplate.send(topic,student);
    }


}
