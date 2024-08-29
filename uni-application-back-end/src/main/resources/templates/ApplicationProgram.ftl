<!DOCTYPE html>
<html>
<head>
    <title>Specialty Details</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        th, td {
            border: 1px solid #dddddd;
            text-align: left;
            padding: 8px;
            word-wrap: break-word;
        }

        th {
            background-color: #f2f2f2;
        }

        body, html {
            margin: 0;
            padding: 20px;
        }

        table {
            table-layout: fixed;
        }
    </style>
</head>
<body>
<#setting url_escaping_charset='UTF-8'>
<h1>${specialtyDTO.specialtyName!}</h1>

<table>
    <tr>
        <th>Total Credits Required</th>
        <td>${specialtyDTO.totalCreditsRequired!"N/A"?html}</td>
    </tr>
    <tr>
        <th>Employment Rate</th>
        <td>${specialtyDTO.employmentRate!"N/A"?html}</td>
    </tr>
</table>

<h2>Specialty Program</h2>
<table>
    <tr>
        <th>Program Name</th>
        <td>${specialtyDTO.specialtyProgram.programName!"N/A"?html}</td>
    </tr>
    <tr>
        <th>Starts On</th>
        <td>${specialtyDTO.specialtyProgram.startsOn?date?string("yyyy-MM-dd")!"N/A"?html}</td>
    </tr>
    <tr>
        <th>Ends On</th>
        <td>${specialtyDTO.specialtyProgram.endsOn?date?string("yyyy-MM-dd")!"N/A"?html}</td>
    </tr>
    <tr>
        <th>Duration (Days)</th>
        <td>${specialtyDTO.specialtyProgram.durationDays!"N/A"?html}</td>
    </tr>
    <tr>
        <th>Description</th>
        <td>${specialtyDTO.specialtyProgram.description!"N/A"?html}</td>
    </tr>
    <tr>
        <th>Degree Type</th>
        <td>${specialtyDTO.specialtyProgram.degreeType.degreeType!"N/A"?html}</td>
    </tr>
    <tr>
        <th>Degree Description</th>
        <td>${specialtyDTO.specialtyProgram.degreeType.degreeDescription!"N/A"?html}</td>
    </tr>
    <tr>
        <th>Accreditation Type</th>
        <td>${specialtyDTO.specialtyProgram.accreditationStatus.accreditationType!"N/A"?html}</td>
    </tr>
    <tr>
        <th>Accreditation Description</th>
        <td>${specialtyDTO.specialtyProgram.accreditationStatus.accreditationDescription!"N/A"?html}</td>
    </tr>
</table>

<h2>Subjects</h2>
<table>
    <tr>
        <th>Subject Name</th>
        <th>Description</th>
        <th>Duration Hours</th>
        <th>Teacher Name</th>
    </tr>
    <#list specialtyDTO.subjects as subject>
        <tr>
            <td>${subject.subjectName!"N/A"?html}</td>
            <td>${subject.subjectDescription!"N/A"?html}</td>
            <td>
                <#list subjectInfo as info>
                    <#if subject.id == info.subjectId>
                        ${info.durationHours!"N/A"?html}
                    </#if>
                </#list>
            </td>
            <td>
                <#list subject.teachers as info>
                    ${info.teacherName!"N/A"?html}
                </#list>
            </td>
        </tr>
    </#list>
</table>

</body>
</html>
