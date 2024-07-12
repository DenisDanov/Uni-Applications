<!DOCTYPE html>
<html>
<head>
    <title>Application Details</title>
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

        .file-item {
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
<#setting url_escaping_charset='UTF-8'>
<h1>Application Details</h1>
<table>
    <tr>
        <th>Username</th>
        <td>${application.username!"N/A"?html}</td>
    </tr>
    <tr>
        <th>Specialty</th>
        <td>${application.specialtyName!"N/A"?html}</td>
    </tr>
    <tr>
        <th>Faculty</th>
        <td>${application.facultyName!"N/A"?html}</td>
    </tr>
    <tr>
        <th>Application Sent Date</th>
        <td>${application.applicationSentDate?date?string("yyyy-MM-dd")!"N/A"?html}</td>
    </tr>
    <tr>
        <th>Application Status</th>
        <td>${application.applicationStatus.applicationStatus!"N/A"?html}</td>
    </tr>
    <tr>
        <th>Application Description</th>
        <td>${application.applicationDescription!"N/A"?html}</td>
    </tr>
    <tr>
        <th>Average Grade</th>
        <td>${application.avgGrade!"N/A"?html}</td>
    </tr>
    <tr>
        <th>Language Proficiency Test Result</th>
        <td>${application.languageProficiencyTestResult!"N/A"?html}</td>
    </tr>
    <tr>
        <th>Standardized Test Result</th>
        <td>${application.standardizedTestResult!"N/A"?html}</td>
    </tr>
    <tr>
        <th>Letter of Recommendation</th>
        <td>${application.letterOfRecommendation?has_content?then(application.letterOfRecommendation, "N/A")?html}</td>
    </tr>
    <tr>
        <th>Personal Statement</th>
        <td>${application.personalStatement!"N/A"?html}</td>
    </tr>
    <#if application.applicationFiles?has_content>
        <tr>
            <th>Attached Files</th>
            <td>
                <#list application.applicationFiles as file>
                    <div class="file-item">
                        ${file.fileName!"N/A"?html}
                    </div>
                </#list>
            </td>
        </tr>
    </#if>
</table>
</body>
</html>
