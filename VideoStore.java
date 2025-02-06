import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

class VideoStore {
    private static Node videoHeadSingly; // Head of the Singly Linked List for videos
    private static DNode videoHeadDoubly; // Head of the Doubly Linked List for videos
    private static Node customerHeadSingly; // Head of the Singly Linked List for customers
    private static DNode customerHeadDoubly; // Head of the Doubly Linked List for customers
    private static Queue<Integer> requestQueue = new LinkedList<>();

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        if (args.length == 0) {
            System.out.println("Usage100: java VideoStore <listType> [<numVideos> <numCustomers> <numRequests>]");
            return;
        }

        // Initialize video store with default or provided arguments
        String listType = args[0];
        int numVideos = 0;
        int numCustomers = 0;
        int numRequests = 0;
        if (args.length > 1) {
            numVideos = Integer.parseInt(args[1]);
        }
        if (args.length > 2) {
            numCustomers = Integer.parseInt(args[2]);
        }
        if (args.length > 3) {
            numRequests = Integer.parseInt(args[3]);
        }

        // Initialize videos and customers
        initializeVideos(numVideos, listType);
        initializeCustomers(numCustomers, listType);

        // Generate transaction requests
        Queue<Integer> requestQueue = generateRequests(numRequests); // Call generateRequests once

        // Process requests
        processRequests(requestQueue, listType, scanner, args);
        printTimeElapsed(startTime);
        while (running) {
            System.out.println("===========================");
            System.out.println("Select one of the following");
            System.out.println("1: To add a video");
            System.out.println("2: To delete a video");
            System.out.println("3: To add a customer");
            System.out.println("4: To delete a customer");
            System.out.println("5: To check if a particular video is in store");
            System.out.println("6: To check out a video");
            System.out.println("7: To check in a video");
            System.out.println("8: To print all customers");
            System.out.println("9: To print all videos");
            System.out.println("10: To print in store videos");
            System.out.println("11: To print all rent videos");
            System.out.println("12: To print the videos rent by a customer");
            System.out.println("13: To exit");
            System.out.println("===========================");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    addVideo(scanner, new String[] { args[0] });
                    break;
                case 2:
                    deleteVideo(scanner, new String[] { args[0] });
                    break;
                case 3:
                    addCustomer(scanner, new String[] { args[0] });
                    break;
                case 4:
                    deleteCustomer(scanner, new String[] { args[0] });
                    break;
                case 5:
                    checkVideoInStore(new String[] { args[0] });
                    break;
                case 6:
                    checkOutVideo(scanner, new String[] { args[0] });
                    break;
                case 7:
                    checkInVideo(scanner, new String[] { args[0] });
                    break;
                case 8:
                    printAllCustomers(args[0]);
                    break;
                case 9:
                    printAllVideos(args[0]);
                    break;
                case 10:
                    printInStoreVideos(new String[] { args[0] });
                    break;
                case 11:
                    printAllRentVideos(new String[] { args[0] });
                    break;
                case 12:
                    printRentVideosByCustomer(new String[] { args[0] });
                    break;
                case 13:
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
        scanner.close();

    }

    private static class Node {
        private String element;
        private Node next;
        private String type; // Indicates whether the node is a customer or a video
        private boolean checkedOut;
        private String customerName;

        public Node(String element, Node next, String type) {
            this.element = element;
            this.next = next;
            this.type = type;
            this.checkedOut = false;
        }

        public String getElement() {
            return element;
        }

        public Node getNext() {
            return next;
        }

        public void setElement(String element) {
            this.element = element;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public boolean isCheckedOut() {
            return checkedOut;
        }

        public void setCheckedOut(boolean checkedOut) {
            this.checkedOut = checkedOut;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerName() {
            return customerName;
        }

    }

    private static class DNode {
        private String element;
        private DNode prev;
        private DNode next;
        private String type;
        private boolean checkedOut;
        private String customerName;

        public DNode(String element, DNode prev, DNode next, String type) {
            this.element = element;
            this.prev = prev;
            this.next = next;
            this.type = type;
            this.checkedOut = false;
        }

        public String getElement() {
            return element;
        }

        public DNode getPrev() {
            return prev;
        }

        public DNode getNext() {
            return next;
        }

        public void setElement(String element) {
            this.element = element;
        }

        public void setPrev(DNode prev) {
            this.prev = prev;
        }

        public void setNext(DNode next) {
            this.next = next;
        }

        public boolean isCheckedOut() {
            return checkedOut;
        }

        public void setCheckedOut(boolean checkedOut) {
            this.checkedOut = checkedOut;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerName() {
            return customerName;
        }
    }

    private static void addVideo(Scanner scanner, String[] args) {
        System.out.print("Enter the title of the video to add: ");
        String title = scanner.nextLine();

        if (args != null && args.length > 0) {
            if (args[0].equals("SLL")) {
                addVideoToSinglyLinkedList(title);
            } else if (args[0].equals("DLL")) {
                addVideoToDoublyLinkedList(title);
            }
        } else {
            // Default behavior: add the video to both SLL and DLL
            addVideoToSinglyLinkedList(title);
            addVideoToDoublyLinkedList(title);
        }
    }

    public static void printTimeElapsed(long startTime) {
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Total service time in ms: " + elapsedTime);
    }

    private static void addVideoToSinglyLinkedList(String title) {
        Node newNode = new Node(title, null, "video");
        if (videoHeadSingly == null) {
            videoHeadSingly = newNode;
        } else {
            Node temp = videoHeadSingly;
            while (temp.getNext() != null) {
                temp = temp.getNext();
            }
            temp.setNext(newNode);
        }
        // System.out.println("Video added to Singly Linked List: " + title);
    }

    private static void addVideoToDoublyLinkedList(String title) {
        DNode newDoublyNode = new DNode(title, null, null, "video");
        if (videoHeadDoubly == null) {
            videoHeadDoubly = newDoublyNode;
        } else {
            DNode temp = videoHeadDoubly;
            while (temp.getNext() != null) {
                temp = temp.getNext();
            }
            temp.setNext(newDoublyNode);
            newDoublyNode.setPrev(temp);
        }
        // System.out.println("Video added to Doubly Linked List: " + title);
    }

    private static void addCustomerToSinglyLinkedList(String customerName) {
        Node newCustomer = new Node(customerName, null, "customer");
        if (customerHeadSingly == null) {
            customerHeadSingly = newCustomer;
        } else {
            Node temp = customerHeadSingly;
            while (temp.getNext() != null) {
                temp = temp.getNext();
            }
            temp.setNext(newCustomer);
        }
    }

    private static void addCustomerToDoublyLinkedList(String customerName) {
        DNode newCustomer = new DNode(customerName, null, null, "customer");
        if (customerHeadDoubly == null) {
            customerHeadDoubly = newCustomer;
        } else {
            DNode temp = customerHeadDoubly;
            while (temp.getNext() != null) {
                temp = temp.getNext();
            }
            temp.setNext(newCustomer);
            newCustomer.setPrev(temp);
        }
    }

    private static void deleteVideo(Scanner scanner, String[] args) {
        System.out.print("Enter the title of the video to delete: ");
        String title = scanner.nextLine();

        if (args != null && args.length > 0) {
            if (args[0].equals("SLL")) {
                deleteVideoFromSinglyLinkedList(title);
            } else if (args[0].equals("DLL")) {
                deleteVideoFromDoublyLinkedList(title);
            }
        } else {
            System.out.println("Invalid arguments. Please specify either 'SLL' or 'DLL'.");
        }
    }

    private static void deleteVideoFromSinglyLinkedList(String title) {
        Node temp = videoHeadSingly;
        Node prev = null;

        while (temp != null && !temp.getElement().equals(title)) {
            prev = temp;
            temp = temp.getNext();
        }

        if (temp != null) {
            if (prev == null) {
                videoHeadSingly = temp.getNext();
            } else {
                prev.setNext(temp.getNext());
            }
            System.out.println("Video deleted from Singly Linked List: " + title);
        } else {
            System.out.println("Video not found in Singly Linked List");
        }
    }

    private static void deleteVideoFromDoublyLinkedList(String title) {
        DNode temp = videoHeadDoubly;

        while (temp != null && !temp.getElement().equals(title)) {
            temp = temp.getNext();
        }

        if (temp != null) {
            if (temp.getPrev() != null) {
                temp.getPrev().setNext(temp.getNext());
            } else {
                videoHeadDoubly = temp.getNext();
            }
            if (temp.getNext() != null) {
                temp.getNext().setPrev(temp.getPrev());
            }
            System.out.println("Video deleted from Doubly Linked List: " + title);
        } else {
            System.out.println("Video not found in Doubly Linked List");
        }
    }

    private static void addCustomer(Scanner scanner, String[] args) {
        System.out.print("Enter the name of the customer: ");
        String customerName = scanner.nextLine();

        if (args != null && args.length > 0) {
            if (args[0].equals("SLL")) {
                addCustomerToSinglyLinkedList(customerName);// Add the new customer node to the SLL
                // System.out.println("Customer " + customerName + " added to the Singly Linked
                // List successfully.");
            } else if (args[0].equals("DLL")) {
                addCustomerToDoublyLinkedList(customerName);// Add the new customer node to the DLL
                // System.out.println("Customer " + customerName + " added to the Doubly Linked
                // List successfully.");
            } else {
                System.out.println("Invalid argument. Please specify either 'SLL' or 'DLL'.");
            }
        } else {
            System.out.println("No argument provided. Please specify either 'SLL' or 'DLL'.");
        }
    }

    private static void deleteCustomer(Scanner scanner, String[] args) {
        if (args != null && args.length > 0) {
            System.out.print("Enter the name of the customer to delete: ");
            String customerName = scanner.nextLine();
            if (args[0].equals("SLL")) {
                deleteCustomerFromSinglyLinkedList(customerName); // Delete the customer from the Singly Linked List
            } else if (args[0].equals("DLL")) {
                deleteCustomerFromDoublyLinkedList(customerName); // Delete the customer from the Doubly Linked List
            } else {
                System.out.println("Invalid argument. Please specify either 'SLL' or 'DLL'.");
            }
        } else {
            System.out.println("No argument provided. Please specify either 'SLL' or 'DLL'.");
        }
    }

    private static void deleteCustomerFromSinglyLinkedList(String customerName) {
        Node current = customerHeadSingly;
        Node previous = null;

        // Traverse the singly linked list to find the customer to delete
        while (current != null && !current.getElement().equals(customerName)) {
            previous = current;
            current = current.getNext();
        }

        // If customer is found, delete it
        if (current != null) {
            if (previous == null) {
                customerHeadSingly = current.getNext();
            } else {
                previous.setNext(current.getNext());
            }
            System.out.println("Customer " + customerName + " deleted from Singly Linked List.");
        } else {
            System.out.println("Customer " + customerName + " not found in the Singly Linked List.");
        }
    }

    private static void deleteCustomerFromDoublyLinkedList(String customerName) {
        DNode current = customerHeadDoubly;

        // Traverse the doubly linked list to find the customer to delete
        while (current != null && !current.getElement().equals(customerName)) {
            current = current.getNext();
        }

        // If customer is found, delete it
        if (current != null) {
            if (current.getPrev() == null) {
                customerHeadDoubly = current.getNext();
            } else {
                current.getPrev().setNext(current.getNext());
            }
            if (current.getNext() != null) {
                current.getNext().setPrev(current.getPrev());
            }
            System.out.println("Customer " + customerName + " deleted from Doubly Linked List.");
        } else {
            System.out.println("Customer " + customerName + " not found in the Doubly Linked List.");
        }
    }

    private static void checkVideoInStore(String randomString, String[] args) {
        if (args != null && args.length > 0) {
            if (args[0].equals("SLL")) {
                checkVideoInStoreFromSinglyLinkedList(randomString);
            } else if (args[0].equals("DLL")) {
                checkVideoInStoreFromDoublyLinkedList(randomString);
            } else {
                System.out.println("Invalid argument. Please specify either 'SLL' or 'DLL'.");
            }
        } else {
            System.out.println("No argument provided. Please specify either 'SLL' or 'DLL'.");
        }
    }

    private static void checkVideoInStore(String[] args) {
        if (args != null && args.length > 0) {
            if (args[0].equals("SLL")) {
                checkVideoInStoreFromSinglyLinkedList();
            } else if (args[0].equals("DLL")) {
                checkVideoInStoreFromDoublyLinkedList();
            } else {
                System.out.println("Invalid argument. Please specify either 'SLL' or 'DLL'.");
            }
        } else {
            System.out.println("No argument provided. Please specify either 'SLL' or 'DLL'.");
        }
    }

    private static void checkVideoInStoreFromSinglyLinkedList(String randomString) {
        // System.out.println("Enter the name of the video to check:");
        String videoName = randomString;

        if (videoName == null || videoName.isEmpty()) {
            System.out.println("Invalid video name.");
            return;
        }

        Node current = videoHeadSingly;

        // Traverse the singly linked list to find the video
        while (current != null) {
            if (current.getElement().equalsIgnoreCase(videoName)) {
                // Check if the video is checked out
                if (current.isCheckedOut()) {
                }
                return;
            }
            current = current.getNext();
        }
    }

    private static void checkVideoInStoreFromDoublyLinkedList(String randomString) {
        // System.out.println("Enter the name of the video to check:");
        String videoName = randomString;

        if (videoName == null || videoName.isEmpty()) {
            System.out.println("Invalid video name.");
            return;
        }

        DNode current = videoHeadDoubly;

        // Traverse the doubly linked list to find the video
        while (current != null) {
            if (current.getElement().equalsIgnoreCase(videoName)) {
                // Check if the video is checked out
                return;
            }
            current = current.getNext();
        }
    }

    private static void checkVideoInStoreFromSinglyLinkedList() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the name of the video to check:");
        String videoName = scanner.nextLine();

        if (videoName == null || videoName.isEmpty()) {
            System.out.println("Invalid video name.");
            return;
        }

        Node current = videoHeadSingly;

        // Traverse the singly linked list to find the video
        while (current != null) {
            if (current.getElement().equalsIgnoreCase(videoName)) {
                // Check if the video is checked out
                if (current.isCheckedOut()) {
                    System.out.println("The video '" + videoName + "' is not in the store.");
                } else {
                    System.out.println("The video '" + videoName + "' is in the store.");
                }
                return;
            }
            current = current.getNext();
        }

        System.out.println("The video '" + videoName + "' is not found in the store.");
    }

    private static void checkVideoInStoreFromDoublyLinkedList() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the name of the video to check:");
        String videoName = scanner.nextLine();

        if (videoName == null || videoName.isEmpty()) {
            System.out.println("Invalid video name.");
            return;
        }

        DNode current = videoHeadDoubly;

        // Traverse the doubly linked list to find the video
        while (current != null) {
            if (current.getElement().equalsIgnoreCase(videoName)) {
                // Check if the video is checked out
                if (current.isCheckedOut()) {
                    System.out.println("The video '" + videoName + "' is not in the store.");
                } else {
                    System.out.println("The video '" + videoName + "' is in the store.");
                }
                return;
            }
            current = current.getNext();
        }

        System.out.println("The video '" + videoName + "' is not found in the store.");
    }

    private static void checkOutVideo(String[] args) {
        if (args != null && args.length > 0) {
            String videoTitle = generateRandomString();
            String customerName = generateRandomString();

            if (args[0].equals("SLL")) {
                checkOutVideoFromSinglyLinkedList(videoTitle, customerName, false);
            } else if (args[0].equals("DLL")) {
                checkOutVideoFromDoublyLinkedList(videoTitle, customerName, false);
            } else {
                System.out.println("Invalid argument. Please specify either 'SLL' or 'DLL'.");
            }
        } else {
            System.out.println("No argument provided. Please specify either 'SLL' or 'DLL'.");
        }
    }

    private static void checkOutVideo(Scanner scanner, String[] args) {
        if (args != null && args.length > 0) {
            System.out.print("Enter the title of the video to check out: ");
            String videoTitle = scanner.nextLine();
            System.out.print("Enter the name of the customer: ");
            String customerName = scanner.nextLine();

            if (args[0].equals("SLL")) {
                checkOutVideoFromSinglyLinkedList(videoTitle, customerName, true);
            } else if (args[0].equals("DLL")) {
                checkOutVideoFromDoublyLinkedList(videoTitle, customerName, true);
            } else {
                System.out.println("Invalid argument. Please specify either 'SLL' or 'DLL'.");
            }
        } else {
            System.out.println("No argument provided. Please specify either 'SLL' or 'DLL'.");
        }
    }

    private static void checkOutVideoFromSinglyLinkedList(String videoTitle, String customerName, boolean print) {
        Node current = videoHeadSingly;

        // Traverse the singly linked list to find the video to check out
        while (current != null && !current.getElement().equals(videoTitle)) {
            current = current.getNext();
        }

        // If video is found, mark it as checked out and associate it with the customer
        if (current != null) {
            current.setCheckedOut(true);
            current.setCustomerName(customerName);
            if (print)
                System.out.println("Video " + videoTitle + " checked out by customer " + customerName + ".");
        } else {
            if (print)
                System.out.println("Video " + videoTitle + " not found in the Singly Linked List.");
        }
    }

    private static void checkOutVideoFromDoublyLinkedList(String videoTitle, String customerName, boolean print) {
        DNode current = videoHeadDoubly;

        // Traverse the doubly linked list to find the video to check out
        while (current != null && !current.getElement().equals(videoTitle)) {
            current = current.getNext();
        }

        // If video is found, mark it as checked out and associate it with the customer
        if (current != null) {
            current.setCheckedOut(true);
            current.setCustomerName(customerName);
            if (print)
                System.out.println("Video " + videoTitle + " checked out by customer " + customerName + ".");
        } else {
            if (print)
                System.out.println("Video " + videoTitle + " not found in the Doubly Linked List.");
        }
    }

    private static void checkInVideo(String[] args) {
        if (args != null && args.length > 0) {
            String videoTitle = generateRandomString();

            if (args[0].equals("SLL")) {
                checkInVideoFromSinglyLinkedList(videoTitle, false);
            } else if (args[0].equals("DLL")) {
                checkInVideoFromDoublyLinkedList(videoTitle, false);
            } else {
                System.out.println("Invalid argument. Please specify either 'SLL' or 'DLL'.");
            }
        } else {
            System.out.println("No argument provided. Please specify either 'SLL' or 'DLL'.");
        }
    }

    private static void checkInVideo(Scanner scanner, String[] args) {
        if (args != null && args.length > 0) {
            System.out.print("Enter the title of the video to check in: ");
            String videoTitle = scanner.nextLine();

            if (args[0].equals("SLL")) {
                checkInVideoFromSinglyLinkedList(videoTitle, true);
            } else if (args[0].equals("DLL")) {
                checkInVideoFromDoublyLinkedList(videoTitle, true);
            } else {
                System.out.println("Invalid argument. Please specify either 'SLL' or 'DLL'.");
            }
        } else {
            System.out.println("No argument provided. Please specify either 'SLL' or 'DLL'.");
        }
    }

    private static void checkInVideoFromSinglyLinkedList(String videoTitle, boolean print) {
        Node current = videoHeadSingly;

        // Traverse the singly linked list to find the video to check in
        while (current != null && !current.getElement().equals(videoTitle)) {
            current = current.getNext();
        }

        // If video is found, mark it as checked in and disassociate it from the
        // customer
        if (current != null) {
            current.setCheckedOut(false);
            current.setCustomerName(null);
            if (print)
                System.out.println("Video " + videoTitle + " checked in.");
        } else {
            if (print)
                System.out.println("Video " + videoTitle + " not found in the Singly Linked List.");
        }
    }

    private static void checkInVideoFromDoublyLinkedList(String videoTitle, boolean print) {
        DNode current = videoHeadDoubly;

        // Traverse the doubly linked list to find the video to check in
        while (current != null && !current.getElement().equals(videoTitle)) {
            current = current.getNext();
        }

        // If video is found, mark it as checked in and disassociate it from the
        // customer
        if (current != null) {
            current.setCheckedOut(false);
            current.setCustomerName(null);
            if (print)
                System.out.println("Video " + videoTitle + " checked in.");
        } else {
            if (print)
                System.out.println("Video " + videoTitle + " not found in the Doubly Linked List.");
        }
    }

    private static void printAllCustomers(String listType) {
        System.out.println("List of all customers:");
        if (listType.equals("SLL")) {
            Node current = customerHeadSingly;
            int index = 1;
            while (current != null) {
                System.out.println(index + ". " + current.getElement());
                current = current.getNext();
                index++;
            }
        } else if (listType.equals("DLL")) {
            DNode current = customerHeadDoubly;
            int index = 1;
            while (current != null) {
                System.out.println(index + ". " + current.getElement());
                current = current.getNext();
                index++;
            }
        } else {
            System.out.println("Invalid list type. Please specify either 'SLL' or 'DLL'.");
        }
    }

    private static void printAllVideos(String listType) {
        System.out.println("List of all videos:");
        if (listType.equals("SLL")) {
            Node current = videoHeadSingly;
            int index = 1;
            while (current != null) {
                System.out.println(index + ". " + current.getElement());
                current = current.getNext();
                index++;
            }
        } else if (listType.equals("DLL")) {
            DNode current = videoHeadDoubly;
            int index = 1;
            while (current != null) {
                System.out.println(index + ". " + current.getElement());
                current = current.getNext();
                index++;
            }
        } else {
            System.out.println("Invalid list type. Please specify either 'SLL' or 'DLL'.");
        }
    }

    private static void printInStoreVideos(String[] args) {
        if (args != null && args.length > 0) {
            if (args[0].equals("SLL")) {
                printInStoreVideosFromSinglyLinkedList();
            } else if (args[0].equals("DLL")) {
                printInStoreVideosFromDoublyLinkedList();
            } else {
                System.out.println("Invalid argument. Please specify either 'SLL' or 'DLL'.");
            }
        } else {
            System.out.println("No argument provided. Please specify either 'SLL' or 'DLL'.");
        }
    }

    private static void printInStoreVideosFromSinglyLinkedList() {
        Node current = videoHeadSingly;
        int count = 1;

        // Traverse the singly linked list
        while (current != null) {
            // Check if the video is not checked out
            if (!current.isCheckedOut()) {
                // Print the video title
                System.out.println(count + ". " + current.getElement());
                count++;
            }
            current = current.getNext();
        }
    }

    private static void printInStoreVideosFromDoublyLinkedList() {
        DNode current = videoHeadDoubly;
        int count = 1;

        // Traverse the doubly linked list
        while (current != null) {
            // Check if the video is not checked out
            if (!current.isCheckedOut()) {
                // Print the video title
                System.out.println(count + ". " + current.getElement());
                count++;
            }
            current = current.getNext();
        }
    }

    private static void printAllRentVideos(String[] args) {
        if (args != null && args.length > 0) {
            if (args[0].equals("SLL")) {
                printAllRentVideosFromSinglyLinkedList();
            } else if (args[0].equals("DLL")) {
                printAllRentVideosFromDoublyLinkedList();
            } else {
                System.out.println("Invalid argument. Please specify either 'SLL' or 'DLL'.");
            }
        } else {
            System.out.println("No argument provided. Please specify either 'SLL' or 'DLL'.");
        }
    }

    private static void printAllRentVideosFromSinglyLinkedList() {
        Node current = videoHeadSingly;
        int count = 1;

        // Traverse the singly linked list
        while (current != null) {
            // Check if the video is checked out
            if (current.isCheckedOut()) {
                // Print the video title
                System.out.println(count + ". " + current.getElement());
                count++;
            }
            current = current.getNext();
        }
    }

    private static void printAllRentVideosFromDoublyLinkedList() {
        DNode current = videoHeadDoubly;
        int count = 1;

        // Traverse the doubly linked list
        while (current != null) {
            // Check if the video is checked out
            if (current.isCheckedOut()) {
                // Print the video title
                System.out.println(count + ". " + current.getElement());
                count++;
            }
            current = current.getNext();
        }
    }

    private static void printRentVideosByCustomer(String[] args) {
        if (args != null && args.length > 0) {
            if (args[0].equals("SLL")) {
                printRentVideosByCustomerFromSinglyLinkedList();
            } else if (args[0].equals("DLL")) {
                printRentVideosByCustomerFromDoublyLinkedList();
            } else {
                System.out.println("Invalid argument. Please specify either 'SLL' or 'DLL'.");
            }
        } else {
            System.out.println("No argument provided. Please specify either 'SLL' or 'DLL'.");
        }
    }

    private static void printRentVideosByCustomerFromSinglyLinkedList() {
        Node current = videoHeadSingly;
        int count = 1;

        // Traverse the singly linked list
        while (current != null) {
            // Check if the video is checked out
            if (current.isCheckedOut()) {
                // Print the video title and associated customer name
                System.out.println(
                        count + ". " + current.getElement() + " (Checked out by: " + current.getCustomerName() + ")");
                count++;
            }
            current = current.getNext();
        }
    }

    private static void printRentVideosByCustomerFromDoublyLinkedList() {
        DNode current = videoHeadDoubly;
        int count = 1;

        // Traverse the doubly linked list
        while (current != null) {
            // Check if the video is checked out
            if (current.isCheckedOut()) {
                // Print the video title and associated customer name
                System.out.println(
                        count + ". " + current.getElement() + " (Checked out by: " + current.getCustomerName() + ")");
                count++;
            }
            current = current.getNext();
        }
    }

    private static void initializeVideos(int numVideos, String listType) {
        Scanner scanner = new Scanner(System.in);
        for (int i = 1; i <= numVideos; i++) {
            String title = "Video" + i;
            if (listType.equalsIgnoreCase("SLL")) {
                addVideoToSinglyLinkedList(title);
            } else if (listType.equalsIgnoreCase("DLL")) {
                addVideoToDoublyLinkedList(title);
            } else {
                System.out.println("Invalid list type. Please specify either 'SLL' or 'DLL'.");
                break;
            }
        }
    }

    private static void initializeCustomers(int numCustomers, String listType) {
        Scanner scanner = new Scanner(System.in);
        for (int i = 1; i <= numCustomers; i++) {
            String customerName = "Customer" + i;
            if (listType.equalsIgnoreCase("SLL")) {
                addCustomerToSinglyLinkedList(customerName);
                // System.out.println("Customer " + customerName + " added to the Singly Linked
                // List successfully.");
            } else if (listType.equalsIgnoreCase("DLL")) {
                addCustomerToDoublyLinkedList(customerName);
                // System.out.println("Customer " + customerName + " added to the Doubly Linked
                // List successfully.");
            } else {
                System.out.println("Invalid list type. Please specify either 'SLL' or 'DLL'.");
                break;
            }
        }
    }

    private static Queue<Integer> generateRequests(int numRequests) {
        Random random = new Random();
        Queue<Integer> requestQueue = new LinkedList<>();
        for (int i = 0; i < numRequests; i++) {
            // Generate a random request type (5, 6, or 7) and add it to the queue
            int requestType = random.nextInt(3) + 5; // Generates a random number between 5 and 7 (inclusive)
            requestQueue.add(requestType);
        }
        return requestQueue;
    }

    private static void processRequests(Queue<Integer> requestQueue, String listType, Scanner scanner, String[] args) {
        while (!requestQueue.isEmpty()) {
            int requestType = requestQueue.poll();
            switch (requestType) {
                case 5:
                    checkVideoInStore(generateRandomString(), args);
                    break;
                case 6:
                    checkOutVideo(args);
                    break;
                case 7:
                    checkInVideo(args);
                    break;
                default:
                    System.out.println("Invalid request type: " + requestType);
            }
        }
    }

    // Method to generate a random string (replace with your own logic)
    private static String generateRandomString() {
        Random random = new Random();
        int length = 10; // Specify the length of the random string
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char randomChar = (char) (random.nextInt(26) + 'a'); // Generate a random lowercase letter
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
