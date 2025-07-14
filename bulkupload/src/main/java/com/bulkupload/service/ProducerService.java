package com.bulkupload.service;
import com.bulkupload.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProducerService {

    private final KafkaTemplate<String,Student> kafkaTemplate;

    @Value("${spring.kafka.topic.driver-location}")
    private String topic;
    public void sendStudent(Student student,int orgId)
    {
        System.out.println("Student inside ProducerService" + student.toString());
        Integer partition = orgId%10;
        String key = String.valueOf(orgId);
        System.out.println("Producing student to partition [" + partition + "], key [" + key + "]");

        kafkaTemplate.send(topic,student);
    }


}
