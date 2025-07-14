package com.bulkupload.consumer;

import com.bulkupload.model.Student;
import com.bulkupload.model.Students;
import com.bulkupload.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ConsumerService {
    private final StudentRepository studentRepository;

    @KafkaListener(
            topics="${spring.kafka.topic.driver-location}",
            groupId="student-group",
            containerFactory="kafkaListenerContainerFactory"
    )
    public void consumerService(Student student, Acknowledgment ack, ConsumerRecord<String,Student> record)
    {
        log.info("Received student from Kafka: {}", student);
        log.info("Received on partition {}, offset {}: {}",
                record.partition(), record.offset(), record.value());
        Students students = Students.builder()
                .std_id(student.getStd_id())
                .name(student.getName())
                .email(student.getEmail())
                .location(student.getLocation())
                .build();
        log.info("Thread: {}, Received student: {}", Thread.currentThread().getName(), student);
        studentRepository.save(students);

         log.info(" Sved to DB: {}", students);
        ack.acknowledge(); // ✅ MUST be called for MANUAL_IMMEDIATE

        System.out.println("Saved Student repository " + student);

    }
}
