package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.mappers.StudentsRequirementsResultsMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.StudentsRequirementsResultsDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.TestStateDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.TestStateDTOFinal;
import bg.duosoft.uniapplicationdemo.models.entities.StudentsRequirementsResultsEntity;
import bg.duosoft.uniapplicationdemo.repositories.StudentsRequirementsResultsRepository;
import bg.duosoft.uniapplicationdemo.services.StudentsRequirementsResultsService;
import bg.duosoft.uniapplicationdemo.services.base.BaseServiceImpl;
import bg.duosoft.uniapplicationdemo.validators.StudentsRequirementsResultsValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentsRequirementsResultsServiceImpl extends BaseServiceImpl<String, StudentsRequirementsResultsDTO, StudentsRequirementsResultsEntity, StudentsRequirementsResultsMapper, StudentsRequirementsResultsValidator, StudentsRequirementsResultsRepository> implements StudentsRequirementsResultsService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(StudentsRequirementsResultsServiceImpl.class);

    @Override
    public boolean isCachingEnabled() {
        return false;
    }

    @Override
    public void processTestResults(String jsonString) {
        try {
            int[] correctAnswersLanguageTest = {1, 1, 0, 2, 1, 1, 1, 1, 1, 2};
            int[] correctAnswersStandardTest = {1, 1, 3, 0, 0, 3, 0, 1, 0, 1};
            TestStateDTO dto = objectMapper.readValue(jsonString, TestStateDTOFinal.class);
            String username = dto.getUsername();
            String testName = dto.getTestName();
            List<Integer> answers = dto.getAnswers();

            if (testName.equals("language")) {
                processTest(correctAnswersLanguageTest, answers, username, testName);
            } else {
                processTest(correctAnswersStandardTest, answers, username, testName);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void processTest(int[] correctAnswers, List<Integer> answers, String username, String testName) {
        int correctCount = 0;
        for (int i = 0; i < correctAnswers.length; i++) {
            if (answers.get(i) == correctAnswers[i]) {
                correctCount++;
            }
        }

        double result = correctCount * 10.0;

        StudentsRequirementsResultsDTO resultsDTO = super.getById(username);

        if (resultsDTO != null) {
            if (testName.equals("language")) {
                resultsDTO.setLanguageProficiencyTestResult(result);
            } else {
                resultsDTO.setStandardizedTestResult(result);
            }

            super.update(resultsDTO);
        } else {
            super.create(new StudentsRequirementsResultsDTO(username,
                    testName.equals("language") ? result : null,
                    testName.equals("standard") ? result : null));
        }
    }
}
