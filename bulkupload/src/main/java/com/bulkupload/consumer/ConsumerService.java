package com.bulkupload.consumer;

import com.bulkupload.model.Student;
import com.bulkupload.model.Students;
import com.bulkupload.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsumerService {
    private final StudentRepository studentRepository;

    @KafkaListener(
            topics="${spring.kafka.topic.driver-location}",
            groupId = "student_group",
            concurrency = "4"
    )
    public void consumerService(Student student)
    {
        Students students = Students.builder()
                .std_id(student.getStd_id())
                .name(student.getName())
                .email(student.getEmail())
                .location(student.getLocation())
                .build();
        studentRepository.save(students);
        System.out.println("Saved Student repository " + student);

    }
}
