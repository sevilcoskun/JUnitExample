import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ClinicMain {

    private static ClinicCalendar calendar;

    public static void main(String[] args) {
        calendar = new ClinicCalendar(LocalDate.now());
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to Patient Intake Computer System\n");
        String lastOption = "";
        while(!lastOption.equalsIgnoreCase("x")){
            lastOption = displayMenu(scan);
        }
        System.out.println("\nExiting from the system!");
    }

    private static String displayMenu(Scanner scan){
        System.out.println("Please select an option");
        System.out.println("1. Enter a Patient Appointment");
        System.out.println("2. View All Appointments");
        System.out.println("X. Exit from the system");
        System.out.println("Option: ");
        String option = scan.next();
        switch (option){
            case "1": performPatientEntry(scan);
                return option;
            case "2": performAllAppointments();
                return option;
            default:
                System.out.println("Invalid option, please re-enter");
                return option;
        }
    }

    private static void performPatientEntry(Scanner scan) {
        scan.nextLine();
        System.out.println("\nPlease Enter Appointment Info");
        System.out.print("Patient Last Name: ");
        String lastName = scan.nextLine();
        System.out.print("Patient First Name: ");
        String firstName = scan.nextLine();
        System.out.print("Appointment Date (M/d/yyyy h:m a): ");
        String when = scan.nextLine();
        System.out.print("Doctor Last Name: ");
        String doctor = scan.nextLine();

        try{
            calendar.addAppointment(firstName, lastName, when, doctor);
        }catch (Throwable t){
            System.out.println("Error - " + t.getMessage());
        }
        System.out.println("Patient entered successfully!\n");
    }

    private static void performAllAppointments() {
        System.out.println("\nAll Appointments in System: ");
        for(PatientAppointment appointment : calendar.getAppointments()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy hh:mm a");
            String appointmentTime = formatter.format(appointment.getAppointmentDateTime());
            System.out.println(String.format("%s: %s, %s\t\tDoctor: %s",
                    appointmentTime, appointment.getPatientLastName(), appointment.getPatientFirstName(), appointment.getDoctor().getName()));
        }
        System.out.println("\nPlease press any key to continue...");
    }
}
