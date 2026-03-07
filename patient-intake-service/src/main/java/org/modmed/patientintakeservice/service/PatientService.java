package org.modmed.patientintakeservice.service;

import org.modmed.patientintakeservice.dto.PatientDTO;
import org.modmed.patientintakeservice.model.Patient;
import org.modmed.patientintakeservice.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
public class PatientService {
    Logger logger = LoggerFactory.getLogger(PatientService.class);

    private final PatientRepository patientRepository;
    private final SqsClient sqsClient;

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;
    @Autowired
    public PatientService(PatientRepository patientRepository, SqsClient sqsClient) {
        this.patientRepository = patientRepository;
        this.sqsClient = sqsClient;
    }


    public Patient createPatient(PatientDTO patientDTO) {
        Patient patient = new Patient();
        patient.setFirstName(patientDTO.getFirstName());
        patient.setLastName(patientDTO.getLastName());
        patient.setDateOfBirth(patientDTO.getDateOfBirth());
        patient.setGender(patientDTO.getGender());
        patient.setContactNumber(patientDTO.getContactNumber());
        patient.setEmail(patientDTO.getEmail());
        Patient saved= patientRepository.save(patient);
        String event = """
{
  "eventType": "PATIENT_INTAKE_SUBMITTED",
  "patientId": "%s"
}
""".formatted(saved.getId());

        logger.info("Publishing event to SQS: {}", event);
        logger.info("Queue URL used: {}", queueUrl);

        sqsClient.sendMessage(
                SendMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .messageBody(event)
                        .build()
        );

        logger.info("Event successfully published for patient {}", saved.getId());

        return saved;
    }
}
