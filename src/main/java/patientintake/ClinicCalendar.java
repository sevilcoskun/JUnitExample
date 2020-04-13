package patientintake;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ClinicCalendar {
    private List<PatientAppointment> appointments;
    private LocalDate today;

    public ClinicCalendar(){
        this.appointments = new ArrayList<PatientAppointment>();
    }

    public ClinicCalendar(LocalDate today) {
        this.today = today;
        this.appointments = new ArrayList<>();
    }

    public void addAppointment(String patientFirstName, String patientLastName, String dateTime, String doctorKey){
        Doctor doctor = Doctor.valueOf(doctorKey.toLowerCase());
        LocalDateTime localDateTime;
        try{
            if(dateTime.toLowerCase().startsWith("today")){
                String[] parts = dateTime.split(" ", 2);
                LocalTime time = LocalTime.parse(parts[1].toUpperCase(),DateTimeFormatter.ofPattern("h:m a", Locale.getDefault()));
                localDateTime = LocalDateTime.of(today, time);
            }
            else{
                localDateTime = LocalDateTime.parse(dateTime.toUpperCase(),
                        DateTimeFormatter.ofPattern("M/d/yyyy h:mm a", Locale.getDefault()));
            }
        }catch (Throwable t){
            throw new RuntimeException("Unable to create date time from: [" +
                    dateTime.toUpperCase() + "], please enter with format [M/d/yyyy h:mm a]");
        }
        PatientAppointment appointment = new PatientAppointment(patientFirstName, patientLastName, localDateTime, doctor);
        appointments.add(appointment);
    }

    public List<PatientAppointment> getAppointments(){
        return this.appointments;
    }

    public List<PatientAppointment> getTodayAppointments(){
        LocalDate today = LocalDate.now();
        return appointments.stream()
                .filter(appt -> appt.getAppointmentDateTime().toLocalDate().equals(today))
                .collect(Collectors.toList());
    }

    public List<PatientAppointment> getTomorrowAppointments() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        return appointments.stream()
                .filter(appt -> appt.getAppointmentDateTime().toLocalDate().equals(tomorrow))
                .collect(Collectors.toList());
    }

    public boolean hasAppointment(LocalDate date){
        return appointments.stream()
                .anyMatch(appt -> appt.getAppointmentDateTime().toLocalDate().equals(date));
    }

    public List<PatientAppointment> getUpcomingAppointments() {
        return appointments.stream()
                .filter(appt -> appt.getAppointmentDateTime().toLocalDate().isAfter(today))
                .collect(Collectors.toList());
    }
}
