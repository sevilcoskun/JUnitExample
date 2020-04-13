import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import patientintake.ClinicCalendar;
import patientintake.Doctor;
import patientintake.PatientAppointment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClinicCalendarTest {

    private ClinicCalendar calendar;

    @BeforeEach
    void init() { calendar = new ClinicCalendar(LocalDate.now()); }

    @Test
    public void allowEntryOfAnyAppointment(){
        calendar.addAppointment("Sevil", "Coskun", "3/11/1992 5:00 am", "murphy");
        List<PatientAppointment> appointments = calendar.getAppointments();
        assertNotNull(appointments);
        assertEquals(1, appointments.size());
        PatientAppointment enteredAppointment = appointments.get(0);
        assertAll(
                () -> assertEquals("Sevil", enteredAppointment.getPatientFirstName()),
                () -> assertEquals("Coskun", enteredAppointment.getPatientLastName()),
                () -> assertEquals("3/11/1992 05:00 AM", enteredAppointment.getAppointmentDateTime().format(DateTimeFormatter.ofPattern("M/d/yyyy hh:mm a"))),
                () ->  assertSame(Doctor.murphy.getName(), enteredAppointment.getDoctor().getName())
        );
    }

    @Test
    public void notAllowEntryOfAnyAppointment(){
        assertThrows(RuntimeException.class, () -> {
            calendar.addAppointment("Sevil", "Coskun", "40/12/2020 1:00 pm", "murphy");;
        });
        assertThrows(RuntimeException.class, () -> {
            calendar.addAppointment("Sevil", "Coskun", "xxx", "murphy");;
        });
        assertThrows(RuntimeException.class, () -> {
            calendar.addAppointment("Sevil", "Coskun", "yesterday 1:00 pm", "murphy");;
        });
    }

    @Test
    public void returnTrueForHasAppointmentIfThereAreAppointments(){
        calendar.addAppointment("Cem", "Saracoglu", "4/12/2019 2:00 pm", "avery");
        assertTrue(calendar.hasAppointment(LocalDate.of(2019,4,12)));
    }

    @Test
    public void returnFalseHasAppointmentIfThereAreNoAppointments(){
        assertFalse(calendar.hasAppointment(LocalDate.of(2019, 4, 12)));
    }

    @Test
    public void returnCurrentDaysAppointments(){
        calendar.addAppointment("Sevil", "Coskun", "4/13/2020 2:00 am", "murphy");
        calendar.addAppointment("Cem", "Saracoglu", "today 5:00 am", "avery");
        calendar.addAppointment("Ipek", "Lale Baysal", "4/10/2020 3:00 pm", "murphy");

        assertEquals(2, calendar.getTodayAppointments().size());
    }

    @Nested
    @DisplayName("return (today-tomorrow) appointments correctly")
    class AppointmentsForToday{

        @Test
        void returnCurrentDaysAppointments(){
            calendar.addAppointment("Nur", "Dogramaci", "4/13/2020 3:00 am", "murphy");
            calendar.addAppointment("Deniz", "Coskun", "4/13/2020 2:00 am", "murphy");
            calendar.addAppointment("Sefa", "Altay", "today 4:00 am", "avery");
            assertEquals(3, calendar.getTodayAppointments().size());
        }

        @Test
        void returnTomorrowAppointments(){
            calendar.addAppointment("Ece", "Saracoglu Utkan", "4/14/2020 3:00 am", "avery");
            calendar.addAppointment("Utku", "Utkan", "4/14/2020 2:00 am", "avery");
            calendar.addAppointment("Cem", "Saracoglu", "today 4:00 am", "johnson");
            assertEquals(2, calendar.getTomorrowAppointments().size());
        }
    }

    @Nested
    @DisplayName("return upcoming appointments")
    class UpcomingAppointments{

        @Test
        void returnUpcomingAppointments(){
            calendar.addAppointment("Sevil", "Coskun", "4/13/2020 2:00 am", "murphy");
            calendar.addAppointment("Deniz", "Coskun", "4/14/2020 3:00 am", "johnson");
            calendar.addAppointment("Cem", "Saracoglu", "4/15/2020 4:00 am", "avery");
            assertEquals(2, calendar.getUpcomingAppointments().size());
        }

        @Test
        void whenThereAreNoAppointments(){
            List<PatientAppointment> upcoming_appointments = calendar.getUpcomingAppointments();
            assertEquals(0, upcoming_appointments.size());
        }


    }
}