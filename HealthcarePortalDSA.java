import java.util.*;

// Digital Healthcare Appointment Management Portal using DSA concepts
public class HealthcarePortalDSA {

    // Appointment class
    static class Appointment {
        int appointmentId;
        String patientName;
        String doctorName;
        String date;
        String time;
        String paymentStatus;
        int priority; // lower value = higher priority

        Appointment(int appointmentId, String patientName, String doctorName,
                    String date, String time, String paymentStatus, int priority) {
            this.appointmentId = appointmentId;
            this.patientName = patientName;
            this.doctorName = doctorName;
            this.date = date;
            this.time = time;
            this.paymentStatus = paymentStatus;
            this.priority = priority;
        }

        @Override
        public String toString() {
            return "ID: " + appointmentId +
                    ", Patient: " + patientName +
                    ", Doctor: " + doctorName +
                    ", Date: " + date +
                    ", Time: " + time +
                    ", Payment: " + paymentStatus +
                    ", Priority: " + priority;
        }
    }

    // Data structures
    static LinkedList<Appointment> appointmentList = new LinkedList<>();
    static Queue<Appointment> appointmentQueue = new LinkedList<>();
    static PriorityQueue<Appointment> priorityAppointments =
            new PriorityQueue<>(Comparator.comparingInt(a -> a.priority));
    static HashMap<Integer, Appointment> appointmentMap = new HashMap<>();

    static Scanner sc = new Scanner(System.in);

    // Safe integer input
    static int readInt() {
        while (true) {
            try {
                int value = Integer.parseInt(sc.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a number: ");
            }
        }
    }

    // Insert appointment
    static void bookAppointment() {
        System.out.print("Enter Appointment ID: ");
        int id = readInt();

        if (appointmentMap.containsKey(id)) {
            System.out.println("Appointment ID already exists.");
            return;
        }

        System.out.print("Enter Patient Name: ");
        String patient = sc.nextLine().trim();

        System.out.print("Enter Doctor Name: ");
        String doctor = sc.nextLine().trim();

        System.out.print("Enter Date (dd-mm-yyyy): ");
        String date = sc.nextLine().trim();

        System.out.print("Enter Time: ");
        String time = sc.nextLine().trim();

        System.out.print("Enter Payment Status (Pending/Paid): ");
        String payment = sc.nextLine().trim();

        System.out.print("Enter Priority (1 = Emergency, 2 = Normal, 3 = Low): ");
        int priority = readInt();

        if (priority < 1 || priority > 3) {
            System.out.println("Invalid priority. Must be 1, 2, or 3.");
            return;
        }

        Appointment appt = new Appointment(id, patient, doctor, date, time, payment, priority);

        appointmentList.add(appt);
        appointmentQueue.add(appt);
        priorityAppointments.add(appt);
        appointmentMap.put(id, appt);

        System.out.println("Appointment booked successfully.");
    }

    // Display all appointments
    static void displayAppointments() {
        if (appointmentList.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }

        System.out.println("\nAll Appointments:");
        for (Appointment a : appointmentList) {
            System.out.println(a);
        }
    }

    // Linear Search
    static void linearSearchAppointment() {
        System.out.print("Enter Appointment ID to search: ");
        int id = readInt();

        boolean found = false;
        for (Appointment a : appointmentList) {
            if (a.appointmentId == id) {
                System.out.println("Appointment found:");
                System.out.println(a);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Appointment not found.");
        }
    }

    // HashMap Search
    static void hashSearchAppointment() {
        System.out.print("Enter Appointment ID to search quickly: ");
        int id = readInt();

        Appointment a = appointmentMap.get(id);
        if (a != null) {
            System.out.println("Appointment found using HashMap:");
            System.out.println(a);
        } else {
            System.out.println("Appointment not found.");
        }
    }

    // Sort appointments by ID
    static void sortAppointmentsById() {
        if (appointmentList.isEmpty()) {
            System.out.println("No appointments to sort.");
            return;
        }

        appointmentList.sort(Comparator.comparingInt(a -> a.appointmentId));
        System.out.println("Appointments sorted by Appointment ID.");
        displayAppointments();
    }

    // Binary Search after sorting
    static void binarySearchAppointment() {
        if (appointmentList.isEmpty()) {
            System.out.println("No appointments available.");
            return;
        }

        appointmentList.sort(Comparator.comparingInt(a -> a.appointmentId));

        System.out.print("Enter Appointment ID for binary search: ");
        int key = readInt();

        int low = 0, high = appointmentList.size() - 1;
        boolean found = false;

        while (low <= high) {
            int mid = (low + high) / 2;
            int midId = appointmentList.get(mid).appointmentId;

            if (midId == key) {
                System.out.println("Appointment found using Binary Search:");
                System.out.println(appointmentList.get(mid));
                found = true;
                break;
            } else if (midId < key) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        if (!found) {
            System.out.println("Appointment not found.");
        }
    }

    // Delete appointment
    static void cancelAppointment() {
        System.out.print("Enter Appointment ID to cancel: ");
        int id = readInt();

        Appointment a = appointmentMap.remove(id);
        if (a == null) {
            System.out.println("Appointment not found.");
            return;
        }

        appointmentList.removeIf(appt -> appt.appointmentId == id);
        appointmentQueue.remove(a);
        priorityAppointments.remove(a);

        System.out.println("Appointment cancelled successfully.");
    }

    // Process normal appointment queue
    static void processNextAppointment() {
        if (appointmentQueue.isEmpty()) {
            System.out.println("No appointments in normal queue.");
            return;
        }

        Appointment a = appointmentQueue.poll();
        if (a != null) {
            appointmentList.remove(a);
            priorityAppointments.remove(a);
            appointmentMap.remove(a.appointmentId);

            System.out.println("Processing next normal appointment:");
            System.out.println(a);
        }
    }

    // Process highest priority appointment
    static void processPriorityAppointment() {
        if (priorityAppointments.isEmpty()) {
            System.out.println("No priority appointments available.");
            return;
        }

        Appointment a = priorityAppointments.poll();
        if (a != null) {
            appointmentList.remove(a);
            appointmentQueue.remove(a);
            appointmentMap.remove(a.appointmentId);

            System.out.println("Processing highest priority appointment:");
            System.out.println(a);
        }
    }

    // Update payment status
    static void updatePaymentStatus() {
        System.out.print("Enter Appointment ID: ");
        int id = readInt();

        Appointment a = appointmentMap.get(id);
        if (a != null) {
            System.out.print("Enter new payment status (Pending/Paid): ");
            a.paymentStatus = sc.nextLine().trim();
            System.out.println("Payment status updated successfully.");
        } else {
            System.out.println("Appointment not found.");
        }
    }

    public static void main(String[] args) {
        int choice;

        do {
            System.out.println("\n=== Digital Healthcare Appointment Management Portal ===");
            System.out.println("1. Book Appointment");
            System.out.println("2. Display All Appointments");
            System.out.println("3. Search Appointment (Linear Search)");
            System.out.println("4. Search Appointment (HashMap)");
            System.out.println("5. Sort Appointments by ID");
            System.out.println("6. Search Appointment (Binary Search)");
            System.out.println("7. Cancel Appointment");
            System.out.println("8. Process Next Appointment (Queue)");
            System.out.println("9. Process Priority Appointment (Priority Queue)");
            System.out.println("10. Update Payment Status");
            System.out.println("11. Exit");
            System.out.print("Enter your choice: ");

            choice = readInt();

            switch (choice) {
                case 1:
                    bookAppointment();
                    break;
                case 2:
                    displayAppointments();
                    break;
                case 3:
                    linearSearchAppointment();
                    break;
                case 4:
                    hashSearchAppointment();
                    break;
                case 5:
                    sortAppointmentsById();
                    break;
                case 6:
                    binarySearchAppointment();
                    break;
                case 7:
                    cancelAppointment();
                    break;
                case 8:
                    processNextAppointment();
                    break;
                case 9:
                    processPriorityAppointment();
                    break;
                case 10:
                    updatePaymentStatus();
                    break;
                case 11:
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 11);

        sc.close();
    }
}