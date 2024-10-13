# University Applications - Built with React TS and Spring REST API

## Description

Welcome to the **Student Application System**, a dynamic platform designed to streamline the process for students applying to various specialties across different faculties. Our system is crafted to simplify and enhance the application process, making it more accessible and manageable for students while providing robust control for administrators.

### For Students

Our platform offers a user-friendly experience where students can:

- **Browse Specialties**: Explore a wide range of specialties offered by different faculties.
- **Submit Applications**: Apply to their desired specialties with ease.
- **Complete Required Tests**: Take necessary tests, such as language proficiency exams, which are prerequisites for certain applications.
- **Upload Files**: Attach relevant documents or any files necessary for their application, such as resumes or certificates.
- **Track Application Status**: Stay informed about their application progress with real-time updates.
  - Students will also receive email notifications when their application status changes, ensuring they are always up-to-date.
- **Edit and Update Details**: Users can manage and update their personal details from their Profile page to keep their information accurate and current.
### For Administrators

We provide two types of administrative access to manage the platform effectively:

- **Read-Only Admins**: View data, including user profiles, applications, and status updates without making any changes.
- **Full-Access Admins**: Have comprehensive control, including the ability to accept or decline applications, remove applications, block user access, and modify user roles. All Admins also benefit from a robust tracking system that logs every action, such as application submissions and status changes.

### Key Features

- **Application Tracking System**: Monitors and logs each action taken within the platform, including application submissions and status adjustments.
- **Test Requirements**: Students may need to complete specific tests to meet application criteria, ensuring that only qualified candidates apply for each specialty.
- **Secure Application Process**: The application is strongly secured with the help of Spring Security and Keycloak, ensuring proper security and role management.
- **File Management**: Uploading and retrieving files is implemented with Minio, providing fast and efficient file storage and management.

## Table of Contents

- [Description](#description)
- [Built With](#built-with)
- <details>
  <summary><b>Front-End</b></summary>

  - [<a href="#register-page">Register Page</a>](#register-page)
  - [<a href="#login-page">Login Page</a>](#login-page)
  - [Home Page](#home-page)
  - [Specialties Page](#specialties-page)
  - [Faculties Page](#faculties-page)
  - [Apply Page](#apply-page)
  - [Requirements Tests](#requirements-tests)
  - [Profile Page](#profile-page)
  - [Manage Applications Page](#manage-applications-page)
  - [Evaluate Applications Page](#evaluate-applications-page)
  - [Manage Users Page](#manage-users-page)
  - [Admin Dashboard Page](#admin-dashboard-page)
  - [News Page](#news)
- </details>
  <details>
  <summary><b>Backend</b></summary>
</details>

- [License](#license)


## Built With

<ul dir="auto">
  <li><img src="https://img.shields.io/badge/Java-ED4236?style=for-the-badge&logo=java&logoColor=white" alt="Java"></li>
  <li><img src="https://img.shields.io/badge/Spring%20Boot-%236BB13D?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot"></li>
  <li><img src="https://img.shields.io/badge/Spring%20Data-%236BB13D?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Data"></li>
  <li><img src="https://img.shields.io/badge/Spring%20Security-%236BB13D?style=for-the-badge&logo=springsecurity&logoColor=white" alt="Spring Security"></li>
  <li><img src="https://img.shields.io/badge/Spring%20Web-%236BB13D?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Web"></li>
  <li><img src="https://img.shields.io/badge/Keycloak-%233B3F6C?style=for-the-badge&logo=keycloak&logoColor=white" alt="Keycloak"></li>
  <li><img src="https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL"></li>
  <li><img src="https://img.shields.io/badge/Liquibase-0D4F8C?style=for-the-badge&logo=liquibase&logoColor=white" alt="Liquibase"></li>
  <li><img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white" alt="Hibernate"></li>
  <li><img src="https://img.shields.io/badge/Redis-%23D82C20?style=for-the-badge&logo=redis&logoColor=white" alt="Redis"></li>
  <li><img src="https://img.shields.io/badge/Apache%20Kafka-%23D2302C?style=for-the-badge&logo=apachekafka&logoColor=white" alt="Apache Kafka"></li>
  <li><img src="https://img.shields.io/badge/Freemarker-%23E06F2D?style=for-the-badge&logo=freemarker&logoColor=white" alt="Freemarker"></li>
  <li><img src="https://img.shields.io/badge/Minio-%23D5A9F2?style=for-the-badge&logo=minio&logoColor=white" alt="Minio"></li>
  <li><img src="https://img.shields.io/badge/JUnit-25A162?style=for-the-badge&logo=junit&logoColor=white" alt="JUnit"></li>
  <li><img src="https://img.shields.io/badge/Mockito-8C8C8C?style=for-the-badge&logo=mockito&logoColor=white" alt="Mockito"></li>
  <li><img src="https://img.shields.io/badge/React-%23282C34?style=for-the-badge&logo=react&logoColor=61DAFB" alt="React"></li>
  <li><img src="https://img.shields.io/badge/TypeScript-%232B8AB6?style=for-the-badge&logo=typescript&logoColor=white" alt="TypeScript"></li>
  <li><img src="https://img.shields.io/badge/Redux-%23593d88?style=for-the-badge&logo=redux&logoColor=white" alt="Redux"></li>
  <li><img src="https://img.shields.io/badge/MUI-%230081CB?style=for-the-badge&logo=mui&logoColor=white" alt="MUI"></li>
  <li><img src="https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Apache Maven"></li>
</ul>


## Front-End

<details id="register-page">
<summary><h4>Register Page</h4></summary>

The registration page is designed to be user-friendly and efficient for new users looking to create an account.

![Screenshot_518](https://github.com/user-attachments/assets/7b7f8f52-53ac-4fd4-ab96-bad1b73a1dab)

- **Customized Keycloak Page**: The registration page is modified from the default Keycloak page using **keycloakify**, which upgrades it to a React-based interface.
- **Additional Fields**: Includes extra fields required by the application:
  - Username
  - Email
  - First Name
  - Middle Name
  - Last Name
  - Password (minimum 8 characters, with at least one letter and one digit)
  - Confirm Password
  - Birth Date
  - Phone Number
- **Frontend Validation**: Prevents form submission if fields are invalid, ensuring users meet all criteria before registering.
- **Backend Validation**: Mirrors the frontend checks and applies them server-side for data integrity and security.

</details>

<details id="login-page">
<summary><h4>Login Page</h4></summary>

The login page provides a secure entry point for returning users.

![Screenshot_505](https://github.com/user-attachments/assets/56c40784-389c-4c6f-add6-b66b7ed01c5e)

- **Customized Keycloak Page**: The login page is modified using **keycloakify**, upgrading the default Keycloak login page to a React-based interface.
- **Login Credentials**: Users can log in with their **username** and **password**.
- **Secure Authentication**: Ensures secure login using Keycloak’s authentication mechanisms, with the custom frontend integrated seamlessly.

</details>

<details id="home-page">
<summary><h4>Home Page</h4></summary>

The home page serves as the main entry point to the application.

![Screenshot_515](https://github.com/user-attachments/assets/a617ea10-7028-4a55-b6e4-bb82ed1b38b9)

- **Minimalistic Design**: A simple, clean interface that only displays essential elements for easy navigation.
- **Explore Prompt**: Text encourages users to explore the available faculties and specialties.
- **Navigation Buttons**: Includes buttons for exploring different sections of the site.
- **Apply Button**: Provides a button that redirects users to the application page, where they can submit an application (only accessible if logged in with a student account).

</details>

<details id="specialties-page">
<summary><h4>Specialties Page</h4></summary>

The specialties page showcases various specializations or areas of focus.

![Screenshot_512](https://github.com/user-attachments/assets/f04a6bc0-9924-4ed2-987d-e1746d565891)

- **Public Access**: Available to all users, regardless of their role or login status.
- **Specialty Information**: Displays detailed information about each specialty offered by the university, including:
  - Program details
  - Admission requirements
  - Subjects
  - List of teachers associated with each specialty
- **User-Friendly Display**: Allows users to browse and learn about all the available specialties without needing to log in.

</details>

<details id="faculties-page">
<summary><h4>Faculties Page</h4></summary>

The faculties page provides information about different academic faculties.

![Screenshot_511](https://github.com/user-attachments/assets/cbdd4dfa-1eea-4157-9eb1-9c011f16a552)

- **Faculty Overview**: Displays all available faculties in the university, along with detailed information about each one.
- **Specialties within Faculties**: Shows the specialties offered by each faculty, providing a structured view for easy navigation.
- **Specialty Links**: Each specialty is clickable, redirecting users to the **Specialties Page** and automatically filtering the list to show only the specialties from the selected faculty.
- **Public Access**: Accessible to all users, regardless of their login status.

</details>

<details id="apply-page">
<summary><h4>Apply Page</h4></summary>

The apply page allows users to submit applications for selected specialty.

![Screenshot_507](https://github.com/user-attachments/assets/ef4d3e90-68b1-4b0e-bffa-fa79e11efb1a)

- **Student-Only Access**: This page is only accessible to users who are logged in with a student account.
- **Specialty Selection**: Allows students to select a specialty from a dropdown menu. Upon selection, the specific requirements for that specialty are displayed.
- **Dynamic Requirements**: Requirements vary based on the selected specialty and are shown dynamically on the page.
- **Application Fields**: Students can enter their data, including:
  - Application description
  - Letter of recommendation
  - Average grade
  - Personal statement
  - Uploading necessary documents (supported file types: `.txt`, `.pdf`, `.doc`, `.docx`, `.png`, `.jpeg`, `.jpg`)
- **File Upload**: Supports file uploads for required documents with clear validation for file type.
- **Frontend & Backend Validation**: The page won't allow submission unless all fields meet the required rules, with validation checks on both the frontend and backend.
- **Test Requirements**: Some specialties require specific tests (e.g., Standard Test, Language Proficiency Test). Students must complete these tests before they can submit their application if the tests are listed as part of the specialty's requirements.
- **Smooth Submission**: After meeting all requirements, students can successfully submit their application for review.

</details>

<details id="requirements-tests">
<summary><h4>Requirements Tests Page</h4></summary>

The Requirements Tests page allows students to complete the necessary tests required for certain specialties.

![Screenshot_514](https://github.com/user-attachments/assets/d4b91c62-96d0-4f9a-b152-54c969b41232)

- **Student-Only Access**: This page is exclusively accessible to logged-in students.
- **Available Tests**: Presents two tests for students to complete:
  - **Standard Test**
  - **Language Proficiency Test**
- **Time Limit**: Students have a total of **1 hour** to complete each test. Once started, the tests cannot be stopped or reset.
- **Question Format**: Each test consists of **10 multiple-choice questions**, with only one correct answer per question.
- **Automatic Results**: Upon completion, the test results are automatically calculated and saved for the student.
- **Application Integration**: When students apply for a specialty that requires the test results, their scores will be provided automatically.
- **One-Time Completion**: Each test can only be completed once; students cannot retake the tests.
- **One Test at a Time**: Only one test can be started at a time.

</details>

<details id="profile-page">
<summary><h4>Profile Page</h4></summary>

The profile page offers account management features for logged-in users.

![Screenshot_510](https://github.com/user-attachments/assets/b20f2ed5-1afa-4d26-adea-ba725ac050f7)

- **User Access**: Only accessible to logged-in users.
- **Editable Profile Data**: Users can update their personal information such as:
  - First Name
  - Middle Name
  - Last Name
  - Phone Number
- **Non-Editable Fields**: Users cannot change their role, email, or username.
- **Student Applications**: Students can view all the applications they’ve submitted, along with all the data and documents they provided for each application.
  - **File Download**: Uploaded files are displayed as links. Students can click on the file names to download them.
- **Accepted Applications**: For applications that have been accepted, students will see a button. Clicking it will show the study program for the selected specialty.
  - **Program in PDF Format**: The study program is available as a downloadable PDF file for easy access and offline viewing.
    - ![Screenshot_557](https://github.com/user-attachments/assets/a6a80348-4de6-4544-864e-bf88ae1b7cc0)
 
</details>

<details id="manage-applications-page">
<summary><h4>Manage Applications Page</h4></summary>

The manage applications page allows administrators review and manage applications.

![Screenshot_516](https://github.com/user-attachments/assets/4e1da1c8-b3a8-4d80-a99c-f6a474e32cb1)

- **Admin-Only Access**: This page is only accessible to users with admin privileges.
- **Application Overview**: Displays all submitted student applications, along with the data provided for each, including uploaded files.
  - **File Download**: Files uploaded by students are available as clickable links for admins to download.
- **Filtering Options**: A filter menu allows admins to easily sort and search through applications based on various criteria.
- **Admin Actions**:
  - **Full Access Admins**: Admins with full access can take actions such as:
    - **Accept**, **Decline**, or **Delete** applications using dedicated buttons.
  - **All Admins**:
    - **Receipt Generator**: Generates a PDF format of the selected application and downloads it to the admin’s device.
      - ![Screenshot_558](https://github.com/user-attachments/assets/8ebc46cc-3884-4e53-be0a-bb842242643d)
    - **Evaluate Application**: Redirects the admin to the **Evaluate Application Page** for further review and evaluation of the application.

</details>

<details id="evaluate-applications-page">
<summary><h4>Evaluate Applications Page</h4></summary>

The evaluate applications page is used for assessing and making decisions on applications.

![Screenshot_517](https://github.com/user-attachments/assets/43da38a2-54ed-4899-8911-0b3270cc9e59)

- **Admin-Only Access**: This page is exclusively accessible to users with admin privileges.
- **Application Evaluation**: Displays the application selected by the admin for evaluation.
- **Application Information**: Shows detailed information about the application being reviewed.
- **Specialty Requirements**: Presents the requirements for the specialty related to the application and indicates whether they have been met.

</details>

<details id="manage-users-page">
<summary><h4>Manage Users Page</h4></summary>

The manage users page is used for administrative tasks related to user management.

![Screenshot_506](https://github.com/user-attachments/assets/5cddaf51-4df9-459a-b722-dda425a98478)

- **Admin-Only Access**: This page is exclusively accessible to users with admin privileges.
- **User Overview**: Displays a list of all users along with their relevant data (excluding passwords for security).
- **Filtering Options**: A filter menu allows admins to easily search and sort through users based on various criteria.
- **Full Access Admin Functions**: Admins with full access can perform the following actions:
  - **Change Role and Access Level**: Admins can modify the role and access level of users using dropdown menus. After selecting the desired options, clicking the **Update** button will apply the changes.
  - **Block User**: An option to block users, which restricts their access to the site. If the user is logged in, they will be logged out immediately and cannot log back in until they are unblocked.
- **Account Protection**: Accounts of full access admins cannot be altered. Their roles and access levels cannot be changed, nor can their accounts be blocked.

</details>

<details id="admin-dashboard-page">
<summary><h4>Admin Dashboard Page</h4></summary>

The admin dashboard provides an overview of all actions made regarding student applications.

![Screenshot_504](https://github.com/user-attachments/assets/bcaa5bcb-25d9-4925-9bf2-69a8eb9bc924)

- **Admin-Only Access**: This page is exclusively accessible to users with admin privileges.
- **Activity Tracking System**: The dashboard serves as a tracking system that logs and monitors all activities related to student applications.
- **Application Submissions**: Each time a student submits an application, it is recorded with details including:
  - The student who made the application
  - The faculty and specialty applied for
  - Timestamp of the action
- **Modification Tracking**: Logs any changes made to existing applications, such as:
  - Accepted
  - Declined
  - Deleted
- **Action Details**: Each log entry includes the timestamp of the action and additional information about which user performed the action.
- **Filtering Options**: Admins can filter the activity logs to display specific actions, such as:
  - Creation of applications
  - Deletion of applications
  - Updated statuses of existing applications

</details>

<details id="news">
<summary><h4>News Page</h4></summary>

The news page shows important information and news about the platform.

![Screenshot_509](https://github.com/user-attachments/assets/61984d36-260e-473f-8f41-f621d127a633)

- **Public Access**: This page is available for all users to view.
- **Important Updates**: Displays important information and news related to the site.
- **Admin Functions**: Admins with full access levels can add or delete news items, ensuring that the information remains relevant and up-to-date.
- **Adding News**: When admins click to add news, a menu appears where they can provide:
  - ![Screenshot_559](https://github.com/user-attachments/assets/6a37123f-2530-429a-9950-4be558b2646f)
  - **Header**: The title of the news item.
  - **Content**: The main body of the news, which supports HTML, Markdown, or plain text.
  - The format of the text added will be preserved, ensuring that it displays the same way when published, maintaining the integrity of the content.
- **Admin Oversight**: Only admins with full access levels can audit and manage the content on this page.

</details>

## Backend



<details id="license">
<summary><h4>License</h4></summary>
<h4>MIT License</h4>
Copyright (c) 2024 Denis Danov

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
</details>
