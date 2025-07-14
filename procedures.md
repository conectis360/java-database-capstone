DELIMITER $$

CREATE PROCEDURE GetDailyAppointmentReportByDoctor(IN report_date DATE)
BEGIN
SELECT
d.full_name AS doctor_name,
p.full_name AS patient_name,
a.appointment_time,
a.status
FROM
appointments AS a
JOIN
doctors AS d ON a.doctor_id = d.id
JOIN
patients AS p ON a.patient_id = p.id
WHERE
-- Extrai apenas a data da coluna DATETIME para comparação
DATE(a.appointment_time) = report_date
AND a.status != 'CANCELLED'
ORDER BY
doctor_name, a.appointment_time;
END$$

DELIMITER ;

CALL GetDailyAppointmentReportByDoctor('2025-07-15');

DELIMITER $$

CREATE PROCEDURE GetDoctorWithMostPatientsByMonth(IN report_year INT, IN report_month INT)
BEGIN
SELECT
d.full_name AS doctor_name,
COUNT(DISTINCT a.patient_id) AS unique_patient_count
FROM
appointments AS a
JOIN
doctors AS d ON a.doctor_id = d.id
WHERE
YEAR(a.appointment_time) = report_year
AND MONTH(a.appointment_time) = report_month
AND a.status = 'COMPLETED'
GROUP BY
d.id, d.full_name
ORDER BY
unique_patient_count DESC
LIMIT 1;
END$$

DELIMITER ;

CALL GetDoctorWithMostPatientsByMonth(2025, 6);

DELIMITER $$

CREATE PROCEDURE GetDoctorWithMostPatientsByYear(IN report_year INT)
BEGIN
SELECT
d.full_name AS doctor_name,
COUNT(DISTINCT a.patient_id) AS unique_patient_count
FROM
appointments AS a
JOIN
doctors AS d ON a.doctor_id = d.id
WHERE
YEAR(a.appointment_time) = report_year
AND a.status = 'COMPLETED'
GROUP BY
d.id, d.full_name
ORDER BY
unique_patient_count DESC
LIMIT 1;
END$$

DELIMITER ;

CALL GetDoctorWithMostPatientsByYear(2025);