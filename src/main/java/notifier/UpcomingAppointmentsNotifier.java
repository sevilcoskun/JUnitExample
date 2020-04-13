package notifier;


import patientintake.ClinicCalendar;
import patientintake.PatientAppointment;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UpcomingAppointmentsNotifier {
    private ClinicCalendar calendar;
    private EmailNotifier notifier;

    public UpcomingAppointmentsNotifier(ClinicCalendar calendar, EmailNotifier notifier){
        this.calendar = calendar;
        this.notifier = notifier;
    }

    public void run(){
        for(PatientAppointment appt : calendar.getTomorrowAppointments()){
            //SMTPMessageSender notifier = new SMTPMessageSender();
            String email = appt.getPatientLastName().toLowerCase()+appt.getPatientFirstName().toLowerCase()+"@mail.com";
            System.out.println("Sending body: " + buildMessageBody(appt));
            notifier.sendNotification("Appointment Reminder", buildMessageBody(appt), email);
        }
    }

    private String buildMessageBody(PatientAppointment appt){
        return "You have an appointment tomorrow at "
                + appt.getAppointmentDateTime().format(DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault()))
                + " with Dr. "
                + appt.getDoctor().getName() + ".";
    }
}
