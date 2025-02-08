package me.shae;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String FILE_NAME = "tasks.txt";
    private static List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        loadTasks();
        clearConsole();

        Runtime.getRuntime().addShutdownHook(new Thread(Main::saveTasks));

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nTo-Do List Application");
            System.out.println("1. Add Task");
            System.out.println("2. Delete Task");
            System.out.println("3. Mark Task as Completed");
            System.out.println("4. View Tasks");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            clearConsole();

            switch (choice) {
                case 1:
                    System.out.print("Enter task description: ");
                    String description = scanner.nextLine();
                    addTask(description);
                    break;
                case 2:
                    System.out.print("Enter task ID to delete: ");
                    int deleteId = scanner.nextInt();
                    deleteTask(deleteId);
                    break;
                case 3:
                    System.out.print("Enter task ID to mark as completed: ");
                    int completeId = scanner.nextInt();
                    markTaskAsCompleted(completeId);
                    break;
                case 4:
                    viewTasks();
                    break;
                case 5:
                    saveTasks();
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please input a valid option.");
            }

            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            clearConsole();
        }
    }

    public static void clearConsole() {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Could not clear the console: " + e.getMessage());
        }
    }

    private static void addTask(String description) {
        Task task = new Task(description);
        tasks.add(task);
        System.out.println("Task added: " + task);
    }

    private static void deleteTask(int id) {
        Task task = findTaskById(id);
        if (task != null) {
            tasks.remove(task);
            System.out.println("Task deleted: " + task);
        } else {
            System.out.println("Task not found with ID: " + id);
        }
    }

    private static void markTaskAsCompleted(int id) {
        Task task = findTaskById(id);
        if (task != null) {
            task.setCompleted(true);
            System.out.println("Task marked as completed: " + task);
        } else {
            System.out.println("Task not found with ID: " + id);
        }
    }

    private static void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
        } else {
            for (Task task : tasks) {
                System.out.println(task);
            }
        }
    }

    private static Task findTaskById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    private static void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(FILE_NAME)))) {
            oos.writeObject(tasks);
            System.out.println("Tasks saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving tasks to file: " + e.getMessage());
        }
    }

    private static void loadTasks() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(FILE_NAME)))) {
                tasks = (List<Task>) ois.readObject();
                System.out.println("Tasks loaded from file.");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading tasks from file: " + e.getMessage());
            }
        } else {
            System.out.println("No saved tasks found. Starting with an empty list.");
        }
    }
}