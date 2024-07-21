package bg.duosoft.uniapplicationdemo.repositories.custom.impl;

import bg.duosoft.uniapplicationdemo.models.dtos.FilterStudentApplicationsDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.ApplicationStatusDTO;
import bg.duosoft.uniapplicationdemo.models.entities.StudentApplicationEntity;
import bg.duosoft.uniapplicationdemo.repositories.custom.StudentApplicationRepositoryCustom;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StudentApplicationRepositoryCustomImpl extends BaseRepositoryCustomImpl implements StudentApplicationRepositoryCustom {

    @Override
    public List<StudentApplicationEntity> searchRecords(FilterStudentApplicationsDTO filter) {
        StringBuilder queryBuilder = new StringBuilder("select sa from StudentApplicationEntity sa where 1=1");

        String username = filter.getUsername();
        Long specialtyId = filter.getSpecialtyId();
        Long facultyId = filter.getFacultyId();
        Date applicationSentDate = filter.getApplicationSentDate();
        ApplicationStatusDTO applicationStatus = filter.getApplicationStatus();
        String applicationDescription = filter.getApplicationDescription();
        Double avgGrade = filter.getAvgGrade();
        Double languageProficiencyTestResult = filter.getLanguageProficiencyTestResult();
        Double standardizedTestResult = filter.getStandardizedTestResult();
        String personalStatement = filter.getPersonalStatement();

        Map<String, Object> queryParams = new HashMap<>();

        if (StringUtils.hasText(username)) {
            queryBuilder.append(" and sa.username = :username");
            queryParams.put("username", username);
        }
        if (specialtyId != null) {
            queryBuilder.append(" and sa.specialty.id = :specialtyId");
            queryParams.put("specialtyId", specialtyId);
        }
        if (facultyId != null) {
            queryBuilder.append(" and sa.faculty.id = :facultyId");
            queryParams.put("facultyId", facultyId);
        }
        if (applicationSentDate != null) {
            queryBuilder.append(" and sa.applicationSentDate = :applicationSentDate");
            queryParams.put("applicationSentDate", applicationSentDate);
        }
        if (applicationStatus != null) {
            queryBuilder.append(" and sa.applicationStatus.applicationStatus = :applicationStatus");
            queryParams.put("applicationStatus", applicationStatus.getApplicationStatus());
        }
        if (StringUtils.hasText(applicationDescription)) {
            queryBuilder.append(" and sa.applicationDescription = :applicationDescription");
            queryParams.put("applicationDescription", applicationDescription);
        }
        if (avgGrade != null) {
            queryBuilder.append(" and sa.avgGrade = :avgGrade");
            queryParams.put("avgGrade", avgGrade);
        }
        if (languageProficiencyTestResult != null) {
            queryBuilder.append(" and sa.languageProficiencyTestResult = :languageProficiencyTestResult");
            queryParams.put("languageProficiencyTestResult", languageProficiencyTestResult);
        }
        if (standardizedTestResult != null) {
            queryBuilder.append(" and sa.standardizedTestResult = :standardizedTestResult");
            queryParams.put("standardizedTestResult", standardizedTestResult);
        }
        if (StringUtils.hasText(personalStatement)) {
            queryBuilder.append(" and sa.personalStatement = :personalStatement");
            queryParams.put("personalStatement", personalStatement);
        }
        if (StringUtils.hasText(filter.getFacultyName())) {
            queryBuilder.append(" and sa.faculty.facultyName = :facultyName");
            queryParams.put("facultyName", filter.getFacultyName());
        }
        if (StringUtils.hasText(filter.getSpecialtyName())) {
            queryBuilder.append(" and sa.specialty.specialtyName = :specialtyName");
            queryParams.put("specialtyName", filter.getSpecialtyName());
        }

        TypedQuery<StudentApplicationEntity> query = em.createQuery(queryBuilder.toString(), StudentApplicationEntity.class);

        for (Map.Entry<String, Object> param : queryParams.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }

        int maxResults = filter.getMaxResults();
        if (maxResults >= 1) {
            query.setMaxResults(maxResults);
        }

        return query.getResultList();
    }

}
