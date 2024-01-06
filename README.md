
# Navchetna Android APP

Navchetna is an Android application developed for SPYM (Society for Promotion of Youth and Masses), an organization under the Ministry of Social Justice and Empowerment. This app serves as a comprehensive training management tool focused on combating drug addiction. The Android app is built using Kotlin, while the server-side infrastructure is implemented using Django with API calls. Apache is utilized to host and manage the server.

## Tech Stack
### Mobile Application (Android)
- **Language:** Kotlin, Python
- **Authentication:** Email Verification
- **UI/UX:** Android XML, Material Design Components

### Server-Side (Django)
- **Framework:** Django
- **API Calls:** Django REST Framework
- **Database:** Mysql

### Server Hosting
- **Web Server:** Apache
- **Deployment:** Docker, Docker Compose
- **Continuous Integration:** GitHub Actions

### Additional Technologies
- **OTP:** Verification of valid Email through OTP
- **Certificate Generation:** Automatice Certificate generation and sending it to the user
- **Locking Mechanism:** Locking different stages of section according to the user progress 






## Features
### 1. User Authentication
The app includes a secure sign-in and sign-up page to manage user access.

### 2. Training Sections
- **Pre-Test:** Participants, typically school teachers, are required to undergo a pre-test before accessing the training content. The pre-test consists of 20 multiple-choice questions (MCQs) with no time limitation. The participant can choose the language for the test.

- **Training Session:** Upon successful completion of the pre-test, participants can join training sessions scheduled in their preferred slot. This training is essential for educating school teachers on handling drug addiction cases among students.

- **Post-Test:** After the training session, participants must take a post-test, similar to the pre-test. The post-test also comprises 20 MCQs, and the participant selects the language of their choice.

- **Feedback Section:** Following the post-test, participants provide valuable feedback on their training experience.


### 3. Certification
Upon completing the feedback section, participants can generate a training certificate through the app's certificate section. This certificate marks their successful completion of the training program. Subsequently, the participant is elevated to the role of a trainer.

### 4. Trainer Role
As a certified trainer, the user gains the ability to organize training sessions in their state. Trainers can use the app to create training session slots, facilitating the dissemination of knowledge on drug education and addiction prevention.

## ScreenShots
![Frame 7](https://github.com/IIITD-BroCoders/Navchetan-APK/assets/108355333/8ad7d080-650e-464d-b77b-52a9211724e5)
![rIbKHW_bbd61d6c90e82899e8c34802e5bca1a4_00-00-00_00-00-54_2](https://github.com/IIITD-BroCoders/Navchetan-APK/assets/108355333/754c61cb-2cfa-4074-bb33-e48532de7ad3)



## Usage Instructions
- **User Registration:** Participants register for training sessions announced by SPYM in their respective states.

- **Pre-Test:** After successful registration, participants take a pre-test to assess their knowledge of drug addiction.

- **Training Session:** Participants attend the training session, which unlocks the post-test and feedback sections.

- **Post-Test and Feedback:** Participants complete the post-test and provide feedback on the training.

- **Certificate Generation:** Successful completion of all stages allows participants to generate a training certificate.

- **Trainer Role:** Certified participants transition to trainers, gaining the ability to organize and conduct training sessions in their state.
