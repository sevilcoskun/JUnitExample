import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClinicCalendarTest {

    private ClinicCalendar calendar;
    @BeforeEach
    void init(){
        calendar = new ClinicCalendar(LocalDate.of(2020, 4, 12));
    }
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
        calendar.addAppointment("Sevil", "Coskun", "4/12/2020 2:00 am", "murphy");
        calendar.addAppointment("Cem", "Saracoglu", "today 5:00 am", "avery");
        calendar.addAppointment("Ipek", "Lale Baysal", "4/10/2020 3:00 pm", "murphy");

        assertEquals(2, calendar.getTodayAppointments().size());
    }

}