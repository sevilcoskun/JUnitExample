package notifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import patientintake.ClinicCalendar;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UpcomingAppointmentsNotifierTest {
    private EmailNotifierTestDouble emailDouble;

    @BeforeEach
    void init(){
        emailDouble = new EmailNotifierTestDouble();
    }

    @Test
    void sendNotificationWithCorrectFormat(){
        /*ClinicCalendar calendar = new ClinicCalendar(LocalDate.of(2020, 4, 12));
        PatientAppointment appt = new PatientAppointment("Sevil", "Coskun", LocalDateTime.of(2020,4,13,14,30), Doctor.murphy);
        calendar.addAppointment("Sevil", "Coskun", "4/13/2020 2:00 pm", "murphy");
        UpcomingAppointmentsNotifier notifier = new UpcomingAppointmentsNotifier(calendar);
        notifier.run();*/
        ClinicCalendar calendar = new ClinicCalendar(LocalDate.of(2020, 4, 14));
        calendar.addAppointment("Sevil", "Coskun", "04/14/2020 3:00 pm", "avery");
        UpcomingAppointmentsNotifier notifier = new UpcomingAppointmentsNotifier(calendar, emailDouble);

        notifier.run();

        assertEquals(1, emailDouble.receivedMessages.size());
        EmailNotifierTestDouble.Message expectedMessage = emailDouble.receivedMessages.get(0);
        assertAll(
                () -> assertEquals("coskunsevil@mail.com", expectedMessage.toAddress),
                () -> assertEquals("Appointment Reminder", expectedMessage.subject),
                () -> assertEquals("You have an appointment tomorrow at 3:00 PM with Dr. Ralph Avery.", expectedMessage.body)
        );
    }
}