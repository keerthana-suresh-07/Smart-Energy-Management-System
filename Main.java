import java.util.*;

//  Device Abstraction
interface Device {
    String getName();
    int powerUsage();
}

//  Concrete Devices
class Fan implements Device {
    public String getName() { return "Fan"; }
    public int powerUsage() { return 1; }
}

class AC implements Device {
    public String getName() { return "AC"; }
    public int powerUsage() { return 5; }
}

class Light implements Device {
    public String getName() { return "Light"; }
    public int powerUsage() { return 2; }
}

interface DeviceFactory {
    Device createDevice(String type);
}

class DeviceFactoryImpl implements DeviceFactory {
    public Device createDevice(String type) {
        switch(type.toLowerCase()) {
            case "fan": return new Fan();
            case "ac": return new AC();
            case "light": return new Light();
            default: return null;
        }
    }
}

// User Entity (Encapsulation)
class User {
    private String name;
    private int totalUnits;

    public User(String name) {
        this.name = name;
        this.totalUnits = 0;
    }

    public void addUnits(int units) {
        totalUnits += units;
    }

    public int getTotalUnits() {
        return totalUnits;
    }

    public String getName() {
        return name;
    }
}

//  Service Layer
interface EnergyService {
    int calculateUsage(Device device, int hours);
}

class EnergyServiceImpl implements EnergyService {
    public int calculateUsage(Device device, int hours) {
        return device.powerUsage() * hours;
    }
}

//  Bill Service
class BillService {
    private double costPerUnit = 5.0;

    public double calculateBill(int units) {
        return units * costPerUnit;
    }
}

//  AI Suggestion Service
interface SuggestionService {
    String suggest(int units);
}

class EnergyAI implements SuggestionService {
    public String suggest(int units) {
        if (units == 0) return " No usage detected";
        else if (units <= 10) return "Optimal usage";
        else if (units <= 20) return "Moderate usage";
        else return " High usage! Reduce consumption";
    }
}

//  Alert Strategy
interface Alert {
    void send(String msg);
}

class MobileAlert implements Alert {
    public void send(String msg) {
        System.out.println(" Mobile Alert: " + msg);
    }
}

class VoiceAlert implements Alert {
    public void send(String msg) {
        System.out.println("Voice Alert: " + msg);
    }
}

//  MAIN (Orchestrator)
public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Dependencies
        DeviceFactory factory = new DeviceFactoryImpl();
        EnergyService service = new EnergyServiceImpl();
        SuggestionService ai = new EnergyAI();
        BillService billService = new BillService();

        System.out.print("Enter user name: ");
        String name = sc.nextLine();

        User user = new User(name);

        System.out.print("Enter number of devices: ");
        int n = sc.nextInt();

        int total = 0;

        for(int i = 0; i < n; i++) {

            System.out.print("Enter device (fan/ac/light): ");
            String type = sc.next();

            Device device = factory.createDevice(type);

            if(device == null) {
                System.out.println("Invalid device, skipping...");
                continue;
            }

            System.out.print("Enter hours: ");
            int hours = sc.nextInt();

            if(hours < 0) {
                System.out.println(" Invalid hours, skipping...");
                continue;
            }

            int units = service.calculateUsage(device, hours);
            total += units;
        }

        user.addUnits(total);

        // OUTPUT
        System.out.println("\n===== RESULT =====");
        System.out.println("User: " + user.getName());
        System.out.println("Total Units: " + user.getTotalUnits());

        double bill = billService.calculateBill(user.getTotalUnits());
        System.out.println("Total Bill: ₹" + bill);

        System.out.println("AI Suggestion: " + ai.suggest(user.getTotalUnits()));

        // Alert
        Alert alert;
        if(user.getTotalUnits() > 20)
            alert = new VoiceAlert();
        else
            alert = new MobileAlert();

        alert.send("Energy usage updated!");

        sc.close();
    }
}