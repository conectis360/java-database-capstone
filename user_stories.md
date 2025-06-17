### 1. Patient Appointment Cancellation

**Title:**
*As a patient, I want to cancel an upcoming appointment, so that I can free up the time slot and manage my schedule flexibility.*

**Acceptance Criteria:**
1.  Given I am logged in, I can see a "Cancel" button next to my upcoming appointments.
2.  When I click "Cancel," the system asks for confirmation before proceeding.
3.  I can only cancel appointments that are scheduled more than 24 hours in the future. The "Cancel" button should be disabled for appointments within this window.

**Priority:** High
**Story Points:** 3
**Notes:**
* An email notification should be sent to both the patient and the doctor upon successful cancellation.

***

### 2. Doctor Consultation Notes

**Title:**
*As a doctor, I want to add clinical notes to a completed appointment, so that I can maintain a detailed medical history for the patient.*

**Acceptance Criteria:**
1.  Given I am viewing a past appointment in my calendar, I have an option to "Add/View Notes."
2.  When I add a note, it is saved and associated with the specific patient and appointment date.
3.  I can view all previous notes for a patient when I open the notes section for a new appointment.

**Priority:** High
**Story Points:** 8
**Notes:**
* These notes will be stored in the MongoDB database for flexibility. Access must be secure and restricted to authorized healthcare providers.

***

### 3. Patient Doctor Search and Filter

**Title:**
*As a patient, I want to search and filter the list of doctors by specialization, so that I can quickly find the right type of doctor for my needs.*

**Acceptance Criteria:**
1.  Given I am on the "Find a Doctor" page, I can see a search bar and a dropdown menu for specializations.
2.  When I select a specialization from the dropdown, the list of doctors automatically updates to show only those matching the criteria.
3.  When I type a name in the search bar, the list filters to show doctors whose names contain the typed text.

**Priority:** Medium
**Story Points:** 5
**Notes:**
* The search and filter functionalities should work together.

***

### 4. Admin Dashboard Analytics

**Title:**
*As an admin, I want to view a dashboard with key metrics, so that I can monitor the portal's activity at a glance.*

**Acceptance Criteria:**
1.  Given I am logged in as an admin, the main page is a dashboard displaying a count of total registered patients and doctors.
2.  The dashboard shows a chart of new patient registrations over the last 30 days.
3.  The dashboard displays a list of the most booked doctors for the current month.

**Priority:** Medium
**Story Points:** 8
**Notes:**
* The data should be visually represented using charts and graphs for easy interpretation.

***

###5. Patient Password Reset

**Title:**
*As a patient, I want to reset my password, so that I can regain access to my account if I forget my login credentials.*

**Acceptance Criteria:**
1.  Given I am on the login page, I can see and click a "Forgot Password?" link.
2.  When I enter my registered email address, I receive an email containing a secure link to reset my password.
3.  When I click the link in the email, I am taken to a page where I can enter and confirm my new password.

**Priority:** High
**Story Points:** 5
**Notes:**
* The password reset link sent via email must be single-use and expire after a set period (e.g., 60 minutes) to maintain account security.