import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String path = System.getProperty("user.dir") + "\\src\\";
    private static final String BlockFilepath=path+"BlockList.txt";
    private static final String TodoFilepath=path+"TodoList.txt";

    private static List<Block> blockList = new ArrayList<>();
    private static List<Todo> todoList = new ArrayList<>();

    public static void main(String[] args) {
        //프로그램 시작하자마자, 파일을 읽어서 객체 배열에 저장.
        blockList = readFileAndSaveToList(BlockFilepath, "block");
         todoList = readFileAndSaveToList(TodoFilepath, "todo");

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
            System.out.println("1. 파일 무결성 체크");
            System.out.println("2. 날짜 추가");
            System.out.println("3. 스케쥴 관리");
            System.out.println("4. Exit");
            System.out.print("번호를 입력하세요 : ");
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
                    System.out.println("프로그램을 종료합니다...");
                    break;
                default:
                    System.out.println("옳지않는 입력값입니다. 다시 입력해주세요.");
            }
        } while (userInput != 4);

        scanner.close();
    }

    private static void performFileIntegrityCheck() {
        System.out.println("파일 무결성 체크 기능");
        // Implement file integrity check functionality
    }

    private static void inputDate() {
        System.out.println("날짜추가 메뉴: (To be implemented)");
        // Implement date input functionality
    }

    private static void manageSchedule(Scanner scanner) {
        int subMenuChoice;

        do {
            System.out.println("스케쥴 관리 메뉴:");
            System.out.println("1. 스케쥴 추가");
            System.out.println("2. 스케쥴 삭제");
            System.out.println("3. 메인 메뉴로 가기");
            System.out.print("실행할 메뉴를 입력하세요 : ");
            subMenuChoice = scanner.nextInt();
            //개행문자 제거(nextInt)는 개행문자를 읽어오지 않음.
            scanner.nextLine();


            switch (subMenuChoice) {
                case 1:
                    addSchedule(scanner);
                    break;
                case 2:
                    deleteSchedule(scanner);
                    break;
                case 3:
                    System.out.println("매인메뉴로 다시 돌아갑니다...");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        } while (subMenuChoice != 3);
    }

    //스케쥴 추가 함수
    private static void addSchedule(Scanner scanner) {
        System.out.println("스케쥴 추가 기능 시작");

        System.out.print("스케쥴의 타입을 입력하세요. (Block/Todo): ");
        String scheduleType = scanner.nextLine().trim();

        if (scheduleType.equalsIgnoreCase("Block")) {
            Block block = new Block();
            System.out.print("Block 스케쥴 입력 하세요 (팀프로젝트회의 220412 0900 220412 1030): ");
            String[] parts = scanner.nextLine().split(" ");
            block.setScheduleName(parts[0]);
            block.setStartDate(parts[1]);
            block.setStartTime(parts[2]);
            block.setEndDate(parts[3]);
            block.setEndTime(parts[4]);

            if (isDuplicateSchedule(blockList, block)) {
                System.out.println("스케쥴이 중복되었습니다. 다시 입력하세요.");
            } else {
                blockList.add(block);
                System.out.println("스케쥴 추가가 성공적으로 이루어졌습니다.");
                readAndSave(BlockFilepath,blockList);
            }
        } else if (scheduleType.equalsIgnoreCase("Todo")) {
            Todo todo = new Todo();
            System.out.print("Todo 스케쥴을 입력하세요 (보고서작성완료 220412): ");
            String[] parts = scanner.nextLine().split(" ");
            todo.setScheduleName(parts[0]);
            todo.setStartDate(parts[1]);

            if (isDuplicateSchedule(todoList, todo)) {
                System.out.println("스케쥴이 중복되었습니다. 다시 입력하세요.");
            } else {
                todoList.add(todo);
                System.out.println("스케쥴 추가가 성공적으로 이루어졌습니다.");
                readAndSave(TodoFilepath,todoList);
            }
        } else {
            System.out.println("스케쥴 입력 타입이 올바르지 않습니다. 'Block' or 'Todo'.");
        }
    }

    //만약 스케쥴 저장 성공시 외부파일 수정 해당 내용 txt 파일에 덮어쓰기 & 다시 읽어서 저장하기
    private static void readAndSave(String fileName, List list) {
        saveScheduleToFile(fileName,list);
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
                if (existingBlock.getScheduleName().equals(newBlock.getScheduleName()) &&
                        existingBlock.getStartDate().equals(newBlock.getStartDate()) &&
                        existingBlock.getStartTime().equals(newBlock.getStartTime()) &&
                        existingBlock.getEndDate().equals(newBlock.getEndDate()) &&
                        existingBlock.getEndTime().equals(newBlock.getEndTime())) {
                    return true;
                }
            } else if (obj instanceof Todo && newSchedule instanceof Todo) {
                Todo existingTodo = (Todo) obj;
                Todo newTodo = (Todo) newSchedule;
                if (existingTodo.getScheduleName().equals(newTodo.getScheduleName()) &&
                        existingTodo.getStartDate().equals(newTodo.getStartDate())) {
                    return true;
                }
            }
        }
        return false;
    }


    private static void deleteSchedule(Scanner scanner) {
        System.out.println("스케줄 삭제 기능을 시작합니다.");
        System.out.print("삭제할 스케줄 유형을 입력하세요 (Block/Todo): ");
        String scheduleType = scanner.nextLine();

        if (scheduleType.equalsIgnoreCase("Block")) {
            System.out.print("삭제할 스케줄 이름을 입력하세요: ");
            String scheduleName = scanner.nextLine();
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
            int deleteIndex = scanner.nextInt();
            scanner.nextLine(); // 버퍼 비우기

            if (deleteIndex < 1 || deleteIndex > matchedBlocks.size()) {
                System.out.println("유효하지 않은 스케줄 번호입니다!");
                return;
            }

            Block toDelete = matchedBlocks.get(deleteIndex - 1);
            System.out.print("다음 스케줄을 삭제하시겠습니까? (yes/no)\n" + toDelete + "\n선택: ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("yes")) {
                blockList.remove(toDelete);
                readAndSave(BlockFilepath,blockList);
                System.out.println("스케줄이 성공적으로 삭제되었습니다!");
            } else {
                System.out.println("스케줄 삭제를 취소합니다.");
            }
        } else if (scheduleType.equalsIgnoreCase("Todo")) {
            System.out.print("삭제할 스케줄 이름을 입력하세요: ");
            String scheduleName = scanner.nextLine();
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
            int deleteIndex = scanner.nextInt();
            scanner.nextLine(); // 버퍼 비우기

            if (deleteIndex < 1 || deleteIndex > matchedTodos.size()) {
                System.out.println("유효하지 않은 스케줄 번호입니다!");
                return;
            }

            Todo toDelete = matchedTodos.get(deleteIndex - 1);
            System.out.print("다음 스케줄을 삭제하시겠습니까? (yes/no)\n" + toDelete + "\n선택: ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("yes")) {
                todoList.remove(toDelete);
                //삭제 완료후 txt 파일에 반영
                readAndSave(TodoFilepath,todoList);
                System.out.println("스케줄이 성공적으로 삭제되었습니다!");
            } else {
                System.out.println("스케줄 삭제를 취소합니다.");
            }
        } else {
            System.out.println("유효하지 않은 스케줄 유형입니다! 'Block' 또는 'Todo'를 입력하세요.");
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