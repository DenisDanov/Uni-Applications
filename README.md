# University Applications - Built with React TS and Spring REST API

<div align="center">
  <span>Deployed: <a href="https://uni-application.ddns.net/">HERE</a></span>
</div>

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
- **Full-Access Admins**: Have comprehensive control, including the ability to accept or decline applications, remove applications, block user access, and modify user roles. Full-Access Admins also benefit from a robust tracking system that logs every action, such as application submissions and status changes.

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
  - [Profile Page](#profile-page)
  - [Manage Applications Page](#manage-applications-page)
  - [Manage Users Page](#manage-users-page)
  - [Admin Dashboard Page](#admin-dashboard-page)
  - [Evaluate Applications Page](#evaluate-applications-page)
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

![Register Page Screenshot](#)  <!-- Replace with actual screenshot URL -->

- Users are prompted to enter their full name, email address, and a secure password.
- Input validation ensures:
  - Names must be properly formatted and cannot be left blank.
  - Email addresses must be valid and unique; duplicate or improperly formatted emails will be rejected.
  - Passwords must be at least 8 characters long and meet complexity requirements.
- After completing registration, users are redirected to the login page to access their account.

</details>

<details id="login-page">
<summary><h4>Login Page</h4></summary>

The login page provides a secure entry point for returning users.

![Login Page Screenshot](#)  <!-- Replace with actual screenshot URL -->

- Users need to enter their registered email address and password.
- For users who forget their password, a password reset option is available:
  - An email with a password reset link is sent if the provided email matches a registered account.
  - The link expires after 24 hours or once the password has been successfully reset.
- Anti-bot measures are in place to prevent unauthorized access:
  - After 5 failed login attempts, users are temporarily locked out for 15 minutes.
  - Further failed attempts result in a longer lockout period.
- Successful login redirects users to their dashboard or home page.

</details>

<details id="home-page">
<summary><h4>Home Page</h4></summary>

The home page serves as the main entry point to the application.

![Home Page Screenshot](#)  <!-- Replace with actual screenshot URL -->

- Features a navigation bar with links to other sections of the site.
- Displays a welcome banner and highlights key features or announcements.
- Includes a section with quick links to popular pages or actions.

</details>

<details id="specialties-page">
<summary><h4>Specialties Page</h4></summary>

The specialties page showcases various specializations or areas of focus.

![Specialties Page Screenshot](#)  <!-- Replace with actual screenshot URL -->

- Lists all available specialties with brief descriptions and links to detailed information.
- Provides filters to narrow down the list based on user preferences or requirements.
- Each specialty entry includes a link to more detailed content or application options.

</details>

<details id="faculties-page">
<summary><h4>Faculties Page</h4></summary>

The faculties page provides information about different academic faculties or departments.

![Faculties Page Screenshot](#)  <!-- Replace with actual screenshot URL -->

- Displays a list of faculties with names, brief descriptions, and key contact information.
- Users can click on each faculty to view more details, including faculty members, research areas, and academic programs.

</details>

<details id="apply-page">
<summary><h4>Apply Page</h4></summary>

The apply page allows users to submit applications for programs or positions.

![Apply Page Screenshot](#)  <!-- Replace with actual screenshot URL -->

- Users can fill out an application form, providing necessary details such as personal information, qualifications, and supporting documents.
- Includes validation to ensure all required fields are completed accurately.
- Provides confirmation and status updates upon submission.

</details>

<details id="profile-page">
<summary><h4>Profile Page</h4></summary>

The profile page offers account management features for logged-in users.

![Profile Page Screenshot](#)  <!-- Replace with actual screenshot URL -->

- Users can update their personal information, such as name, email, and password.
- Displays a summary of the user's activity, including applications and interactions.
- Includes options to manage account settings and view recent activity.

</details>

<details id="manage-applications-page">
<summary><h4>Manage Applications Page</h4></summary>

The manage applications page allows administrators or users to review and manage applications.

![Manage Applications Page Screenshot](#)  <!-- Replace with actual screenshot URL -->

- Lists all applications with filtering options based on status, date, or other criteria.
- Provides functionality to view, approve, reject, or request additional information for each application.

</details>

<details id="manage-users-page">
<summary><h4>Manage Users Page</h4></summary>

The manage users page is used for administrative tasks related to user management.

![Manage Users Page Screenshot](#)  <!-- Replace with actual screenshot URL -->

- Displays a list of users with options to view their profiles, modify user roles, or deactivate accounts.
- Includes search and filter functionalities to efficiently manage user data.

</details>

<details id="admin-dashboard-page">
<summary><h4>Admin Dashboard Page</h4></summary>

The admin dashboard provides an overview of key metrics and system status.

![Admin Dashboard Page Screenshot](#)  <!-- Replace with actual screenshot URL -->

- Displays summary statistics and charts related to user activity, application statuses, and system performance.
- Offers quick access to management tools and system settings.

</details>

<details id="evaluate-applications-page">
<summary><h4>Evaluate Applications Page</h4></summary>

The evaluate applications page is used for assessing and making decisions on applications.

![Evaluate Applications Page Screenshot](#)  <!-- Replace with actual screenshot URL -->

- Lists applications awaiting evaluation with details for each application.
- Provides tools for scoring, commenting, and making decisions on applications.

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
