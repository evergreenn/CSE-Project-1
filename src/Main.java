import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String path = System.getProperty("user.dir") + "\\";
    private static final String BlockFilepath = path + "BlockList.txt";
    private static final String TodoFilepath = path + "TodoList.txt";

    private static List<Block> blockList = new ArrayList<>();
    private static List<Todo> todoList = new ArrayList<>();

    public static void main(String[] args) {


        /*
         * try { System.setOut(new PrintStream(System.out, true, "UTF-8")); } catch
         * (UnsupportedEncodingException e) { e.printStackTrace(); }
         */


        // 프로그램 시작하자마자, 파일을 읽어서 객체 배열에 저장.
        blockList = readFileAndSaveToList(BlockFilepath, "block");
        todoList = readFileAndSaveToList(TodoFilepath, "todo");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Console console = System.console();
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;
        boolean validInput = false;
        do {
            clearConsole();
            do {
                try {
                    System.out.println("1. 달력 확인");
                    System.out.println("2. 스케쥴 확인");
                    System.out.println("3. 종료");
                    System.out.print("번호를 입력하세요 : ");
                    userInput = scanner.nextInt();
                    validInput = true;
                } catch (java.util.InputMismatchException e) {
                    System.out.println("올바른 형식의 정수를 입력해주세요.");
                    scanner.nextLine();
                }
            } while (!validInput);

            switch (userInput) {

                case 1:
                    checkCalendar(scanner, console);
                    break;
                case 2:
                    manageSchedule(scanner,console);
                    break;
                case 3:
                    System.out.println("프로그램을 종료합니다.");
                    break;
                default:
                    System.out.println("옳지않는 입력값입니다. 다시 입력해주세요.");
            }
        } while (userInput != 3);

        scanner.close();
    }

    // 메인 메뉴1의 달력확인 메뉴 구현 함수
    private static void checkCalendar(Scanner scanner, Console console) {
        clearConsole();
        System.out.println("달력확인 메뉴입니다");
        scanner.nextLine();
        String date;
        do {
            System.out.print("확인할 날짜를 입력해주세요 (예시 2024년 4월2일 => 240402) : ");
            date = scanner.nextLine();
            date = date.trim();
        } while (!isVaildDate(date));

        printDate(date);
        manageSchedule(scanner, console);
    }

    // 들어온 date의 범위에 포함되는 스케줄을 출력하는 함수
    private static void printDate(String date) {
//        clearConsole();
        int year = Integer.parseInt(date.substring(0, 2)) + 2000;
        int month = Integer.parseInt(date.substring(2, 4));
        int day = Integer.parseInt(date.substring(4));
        int blockCount = 0;
        int todoCount = 0;
        System.out.printf(year + "년" + month + "월" + day + "일, 해야하는 스케줄 정보입니다.\n");
        System.out.println("-------------------------------------------------");
        System.out.println("Block 스케줄 정보");

        for (Block block : blockList) {
            if (block.getStartDate().equals(date) || block.getEndDate().equals(date)
                    || (Integer.parseInt(block.getStartDate()) <= Integer.parseInt(date)
                    && Integer.parseInt(block.getEndDate()) >= Integer.parseInt(date))) {
                System.out.println("-" + block.getScheduleName() + " | " + block.getStartDate() + " "
                        + block.getStartTime() + " ~ " + block.getEndDate() + " " + block.getEndTime());
                blockCount++;
            }
        }
        if(blockCount == 0)
            System.out.println("저장된 Block 스케쥴이 없습니다.");

        System.out.println("-------------------------------------------------");
        System.out.println("Todo 스케줄 정보");
        for (Todo todo : todoList) {
            if (todo.getStartDate().equals(date)) {
                System.out.println("-" + todo.getScheduleName());
                todoCount++;
            }
        }

        if(todoCount == 0)
            System.out.println("저장된 Todo 스케쥴이 없습니다.");

        System.out.println("-------------------------------------------------");
    }

    // 메인 메뉴2의 스케줄 관리 메뉴 구현 함수
    private static void manageSchedule(Scanner scanner, Console console) {
        String subMenuChoice;
//        clearConsole();
        System.out.println("스케쥴 관리 메뉴:");
        System.out.println("1. 스케쥴 추가");
        System.out.println("2. 스케쥴 삭제");
        System.out.println("3. 메인 메뉴로 가기");
        System.out.print("실행할 메뉴를 입력하세요 : ");
        do {
            subMenuChoice = console.readLine();

            switch (subMenuChoice) {
                case "1":
                    addSchedule(scanner, console);
                    break;
                case "2":
                    deleteSchedule(scanner, console);
                    break;
                case "3":
                    System.out.println("매인메뉴로 다시 돌아갑니다.");
                    break;
                default:
                    clearConsole();
                    System.out.println("1~3 중에서 선택해 주세요.");
                    System.out.println("스케쥴 관리 메뉴:");
                    System.out.println("1. 스케쥴 추가");
                    System.out.println("2. 스케쥴 삭제");
                    System.out.println("3. 메인 메뉴로 가기");
                    System.out.print("실행할 메뉴를 입력하세요 : ");
            }
        } while (!subMenuChoice.equals("3"));
    }

    // 스케쥴 추가 함수
    private static void addSchedule(Scanner scanner, Console console) {
        clearConsole();
        System.out.println("스케쥴 추가 기능 시작");

        System.out.print("추가할 스케쥴의 타입을 입력하세요. (1.Block/ 2.Todo): ");
        String scheduleType = scanner.nextLine();
        boolean checkDate;
        boolean checkTime;
        String name, startDate, startTime, endDate, endTime;

        if (scheduleType.equals("1") ) {
            Block block = new Block();
            System.out.println("Block스케줄 입니다.");
            do{
                System.out.print("스케줄 이름을 입력하세요: ");
                name = console.readLine();
                block.setScheduleName(name);
                System.out.println(name);

            }while (!isValidName(name));


            clearConsole();
            do {
                System.out.println("스케줄 이름:" + name);
                System.out.print("스케줄 시작날짜 입력\n년월일을 6자리로 입력하세요(예시 2022년 4월 4일 => 220404): ");
                startDate = scanner.nextLine();
                startDate = startDate.trim();
                block.setStartDate(startDate);
            } while (!isVaildDate(startDate));
            clearConsole();

            do {
                System.out.println("스케줄 이름: " + name + "\n시작날짜: " + startDate);
                System.out.print("스케줄 시작시각 입력\n시간,분 을 4자리로 입력하세요(예시 12시 45분 => 1245): ");
                startTime = console.readLine();
                startTime = startTime.trim();
                block.setStartTime(startTime);
            } while (!isVaildTime(startTime));

            clearConsole();
            do {
                System.out.println("스케줄 이름: " + name + "\n시작날짜: " + startDate + "\n시작시각: " + startTime);
                System.out.print("스케줄 종료날짜 입력\n년월일을 6자리로 입력하세요(예시 2022년 4월 4일 => 220404): ");
                endDate = console.readLine();
                endDate = endDate.trim();
                block.setEndDate(endDate);
            } while (!isVaildDate(endDate));
            clearConsole();
            do {
                System.out.println(
                        "스케줄 이름: " + name + "\n시작날짜: " + startDate + "\n시작시각: " + startTime + "\n종료날짜: " + endDate);
                System.out.print("스케줄 종료시각 입력\n시간,분 을 4자리로 입력하세요(예시 12시 45분 => 1245): ");
                endTime = console.readLine();
                endTime = endTime.trim();
                block.setEndTime(endTime);
            } while (!isVaildTime(endTime));

            if (isDuplicateSchedule(blockList, block)) {
                System.out.println("스케쥴이 중복되었습니다. 다시 입력하세요.");
            } else {
                blockList.add(block);
                System.out.println("스케쥴 추가가 성공적으로 이루어졌습니다.");
                readAndSave(BlockFilepath, blockList);
            }
        } else if (scheduleType.equals("2")) {
            Todo todo = new Todo();
            System.out.println("Todo스케줄 입니다.");

            System.out.print("스케줄 이름을 입력하세요: ");
            name = console.readLine();
            todo.setScheduleName(name);
            System.out.println(name);
            clearConsole();
            do {
                System.out.println("스케줄 이름:" + name);
                System.out.print("스케줄 시작날짜 입력\n년월일을 6자리로 입력하세요(예시 2022년 4월 4일 => 220404): ");
                startDate = console.readLine();
                startDate = startDate.trim();
                todo.setStartDate(startDate);
            } while (!isVaildDate(startDate));
            clearConsole();

            if (isDuplicateSchedule(todoList, todo)) {
                System.out.println("스케쥴이 중복되었습니다. 다시 입력하세요.");
            } else {
                todoList.add(todo);
                System.out.println("스케쥴 추가가 성공적으로 이루어졌습니다.");
                readAndSave(TodoFilepath, todoList);
            }
        } else {
            System.out.println("스케쥴 입력 타입이 올바르지 않습니다.");
        }
    }

    private static void deleteSchedule(Scanner scanner, Console console) {
        clearConsole();
        System.out.println("스케쥴 삭제 기능 시작");
        System.out.print("삭제할 스케쥴의 타입을 입력하세요. (1.Block/ 2.Todo): ");
        String scheduleType = console.readLine();

        if (scheduleType.equals("1")) {
            System.out.println("Block 스케줄 정보");
            System.out.println("-------------------------------------------------");
            for (Block block : blockList) {
                System.out.println("-" + block.getScheduleName() + " | " + block.getStartDate() + " "
                        + block.getStartTime() + " ~ " + block.getEndDate() + " " + block.getEndTime());
            }
            System.out.println("-------------------------------------------------");

            System.out.print("삭제할 스케줄 이름을 입력하세요: ");
            String scheduleName = console.readLine();
            List<Block> matchedBlocks = new ArrayList<>();
            int count = 0;

            for (Block block : blockList) {
                if (block.getScheduleName().equalsIgnoreCase(scheduleName)) {
                    matchedBlocks.add(block);
                    System.out.println(++count + ". " + block);
                }
            }

            if (matchedBlocks.isEmpty()) {
                System.out.println("해당 스케줄을 찾을 수 없습니다!");
                return;
            }

            System.out.print("삭제할 스케줄 번호를 입력하세요: ");
            String deleteInput = scanner.nextLine();

            if (!isNumeric(deleteInput)) {
                System.out.println("숫자를 입력해주세요.");
                return;
            }
           int  deleteIndex=Integer.valueOf(deleteInput);



            if (deleteIndex < 1 || deleteIndex > matchedBlocks.size()) {
                System.out.println("유효하지 않은 스케줄 번호입니다!");
                return;
            }

            Block toDelete = matchedBlocks.get(deleteIndex - 1);
            System.out.print("다음 스케줄을 삭제하시겠습니까? (yes입력 시 삭제, 그외 입력 시 삭제 취소)\n" + toDelete + "\n선택: ");
            String confirmation = console.readLine();

            if (confirmation.equalsIgnoreCase("yes")) {
                blockList.remove(toDelete);
                readAndSave(BlockFilepath, blockList);
                System.out.println("스케줄이 성공적으로 삭제되었습니다!");
            } else {
                System.out.println("스케줄 삭제를 취소합니다.");
            }
        } else if (scheduleType.equals("2")) {

            System.out.println("Todo 스케줄 정보");
            System.out.println("-------------------------------------------------");
            for (Todo todo : todoList) {
                System.out.println("-" + todo.getScheduleName());
            }
            System.out.println("-------------------------------------------------");
            System.out.print("삭제할 스케줄 이름을 입력하세요: ");
            String scheduleName = console.readLine();
            List<Todo> matchedTodos = new ArrayList<>();
            int count = 0;

            for (Todo todo : todoList) {
                if (todo.getScheduleName().equalsIgnoreCase(scheduleName)) {
                    matchedTodos.add(todo);
                    System.out.println(++count + ". " + todo);
                }
            }

            if (matchedTodos.isEmpty()) {
                System.out.println("해당 스케줄을 찾을 수 없습니다!");
                return;
            }

            System.out.print("삭제할 스케줄 번호를 입력하세요: ");
            String deleteInput = scanner.nextLine();

            if (!isNumeric(deleteInput)) {
                System.out.println("숫자를 입력해주세요.");
                return;
            }
            int  deleteIndex=Integer.valueOf(deleteInput);

            if (deleteIndex < 1 || deleteIndex > matchedTodos.size()) {
                System.out.println("유효하지 않은 스케줄 번호입니다!");
                return;
            }

            Todo toDelete = matchedTodos.get(deleteIndex - 1);
            System.out.print("다음 스케줄을 삭제하시겠습니까? (yes입력 시 삭제, 그외 입력 시 삭제 취소)\n" + toDelete + "\n선택: ");
            String confirmation = console.readLine();

            if (confirmation.equalsIgnoreCase("yes")) {
                todoList.remove(toDelete);
                // 삭제 완료후 txt 파일에 반영
                readAndSave(TodoFilepath, todoList);
                System.out.println("스케줄이 성공적으로 삭제되었습니다!");
            } else {
                System.out.println("스케줄 삭제를 취소합니다.");
            }
        } else {
            System.out.println("유효하지 않은 스케줄 유형입니다! 'Block' 또는 'Todo'를 입력하세요.");
        }
    }

    // 만약 스케쥴 저장 성공시 외부파일 수정 해당 내용 txt 파일에 덮어쓰기 & 다시 읽어서 저장하기
    private static void readAndSave(String fileName, List list) {
        saveScheduleToFile(fileName, list);
        blockList = readFileAndSaveToList(BlockFilepath, "block");
        todoList = readFileAndSaveToList(TodoFilepath, "todo");
    }

    private static void saveScheduleToFile(String fileName, List list) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Object obj : list) {
                if (obj instanceof Block) {
                    Block block = (Block) obj;
                    writer.write(block.getScheduleName() + " ");
                    writer.write(block.getStartDate() + " ");
                    writer.write(block.getStartTime() + " ");
                    writer.write(block.getEndDate() + " ");
                    writer.write(block.getEndTime() + " ");
                    writer.newLine();
                } else if (obj instanceof Todo) {
                    Todo todo = (Todo) obj;
                    writer.write(todo.getScheduleName() + " ");
                    writer.write(todo.getStartDate() + " ");
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 해당 스케쥴이 존재하는지 확인하는 함수.
    private static boolean isDuplicateSchedule(List list, Object newSchedule) {
        for (Object obj : list) {
            if (obj instanceof Block && newSchedule instanceof Block) {
                Block existingBlock = (Block) obj;
                Block newBlock = (Block) newSchedule;
                if (existingBlock.getScheduleName().equals(newBlock.getScheduleName())
                        && existingBlock.getStartDate().equals(newBlock.getStartDate())
                        && existingBlock.getStartTime().equals(newBlock.getStartTime())
                        && existingBlock.getEndDate().equals(newBlock.getEndDate())
                        && existingBlock.getEndTime().equals(newBlock.getEndTime())) {
                    return true;
                }
            } else if (obj instanceof Todo && newSchedule instanceof Todo) {
                Todo existingTodo = (Todo) obj;
                Todo newTodo = (Todo) newSchedule;
                if (existingTodo.getScheduleName().equals(newTodo.getScheduleName())
                        && existingTodo.getStartDate().equals(newTodo.getStartDate())) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        // 이름에 공백 또는 영어가 포함되어 있는지 확인
        for (char c : name.toCharArray()) {
            if (Character.isWhitespace(c) || Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }



    private static boolean isVaildDate(String date) {

        if (!(date.length() == 6)) {
            clearConsole();
            System.out.println("올바르지 않은 날짜 형식입니다. 다시 입력해주세요.");
            return false;
        }

        int year, month, day;

        boolean isValidDate = false;
        try {
            year = Integer.parseInt(date.substring(0, 2)) + 2000;
            month = Integer.parseInt(date.substring(2, 4));
            day = Integer.parseInt(date.substring(4));
        } catch (NumberFormatException e) {
            clearConsole();
            System.out.println("올바르지 않은 날짜 형식입니다. 다시 입력해주세요.");
            return false;
        }

        boolean isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);

        if (month >= 1 && month <= 12) {
            if (month == 2) {
                if (isLeapYear) {
                    isValidDate = (day >= 1 && day <= 29);
                } else {
                    isValidDate = (day >= 1 && day <= 28);
                }
            } else if (month == 4 || month == 6 || month == 9 || month == 11) {
                isValidDate = (day >= 1 && day <= 30);
            } else {
                isValidDate = (day >= 1 && day <= 31);
            }
        }

        if (isValidDate == false)
        {
            clearConsole();
            System.out.println("올바르지 않은 날짜 형식입니다. 다시 입력해주세요.");
        }

        return isValidDate;
    }

    private static boolean isVaildTime(String time) {

        if (!(time.length() == 4)) {
            clearConsole();
            System.out.println("올바르지 않은 시간 형식입니다. 다시 입력해주세요.");
            return false;
        }

        int hour, min;
        boolean isValidTime = false;

        try {
            hour = Integer.parseInt(time.substring(0, 2));
            min = Integer.parseInt(time.substring(2));
        } catch (NumberFormatException e) {
            clearConsole();
            System.out.println("올바르지 않은 시간 형식입니다. 다시 입력해주세요.");
            return false;
        }


        if (hour >= 0 && hour <= 23 && min >= 0 && min <= 59)
            isValidTime = true;
        else
            isValidTime = false;

        if (!isValidTime)
        {
            clearConsole();
            System.out.println("올바르지 않은 시간 형식입니다. 다시 입력해주세요.");
        }

        return isValidTime;
    }

    private static void clearConsole() {
        try {

            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else if (osName.contains("linux") || osName.contains("mac")) {
                new ProcessBuilder("bash", "-c", "clear").inheritIO().start().waitFor();
            } else {

                System.out.println("이 운영 체제에서는 콘솔을 비우는 기능을 지원하지 않습니다.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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