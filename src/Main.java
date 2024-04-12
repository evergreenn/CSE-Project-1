import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static String path = System.getProperty("user.dir") + "\\src\\";
    public static void main(String[] args) {

        List<Block> blockList = readFileAndSaveToList(path+"BlockList.txt", "block");
        List<Todo> todoList = readFileAndSaveToList(path+"TodoList.txt", "todo");

        // 읽어온 데이터 확인
        for (Block block : blockList) {
            System.out.println(block);
        }

        for (Todo todo : todoList) {
            System.out.println(todo);
        }

        Scanner scanner = new Scanner(System.in);


        int userInput;

        do {
            System.out.println("1. File Integrity Check");
            System.out.println("2. Date Input");
            System.out.println("3. Schedule Management");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            userInput = scanner.nextInt();

            switch (userInput) {
                case 1:
                    performFileIntegrityCheck();
                    break;
                case 2:
                    inputDate();
                    break;
                case 3:
                    manageSchedule(scanner);
                    break;
                case 4:
                    System.out.println("Exiting the program...");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        } while (userInput != 4);

        scanner.close();
    }

    private static void performFileIntegrityCheck() {
        System.out.println("Performing File Integrity Check...");
        // Implement file integrity check functionality
    }

    private static void inputDate() {
        System.out.println("Date Input Menu: (To be implemented)");
        // Implement date input functionality
    }

    private static void manageSchedule(Scanner scanner) {
        int subMenuChoice;

        do {
            System.out.println("Schedule Management Menu:");
            System.out.println("1. Add Schedule");
            System.out.println("2. Delete Schedule");
            System.out.println("3. Main Menu");
            System.out.print("Enter your choice: ");
            subMenuChoice = scanner.nextInt();

            switch (subMenuChoice) {
                case 1:
                    addSchedule(scanner);
                    break;
                case 2:
                    deleteSchedule(scanner);
                    break;
                case 3:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        } while (subMenuChoice != 3);
    }

    private static void addSchedule(Scanner scanner) {
        System.out.println("Add Schedule Menu: (To be implemented)");
        // Implement schedule addition functionality
    }

    private static void deleteSchedule(Scanner scanner) {
        System.out.println("Delete Schedule Menu: (To be implemented)");
        // Implement schedule deletion functionality
    }
    // txt 파일을 읽어서 저장하는 기능을 하는 함수입니다 !
    private static List readFileAndSaveToList(String fileName, String type) {
        List list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");

                if (type.equals("block")) {
                    Block block = new Block();
                    block.setScheduleName(parts[0]);
                    block.setStartDate(parts[1]);
                    block.setStartTime(parts[2]);
                    block.setEndDate(parts[3]);
                    block.setEndTime(parts[4]);
                    list.add(block);
                } else if (type.equals("todo")) {
                    Todo todo = new Todo();
                    todo.setScheduleName(parts[0]);
                    todo.setStartDate(parts[1]);
                    list.add(todo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}