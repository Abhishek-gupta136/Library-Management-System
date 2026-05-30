import java.util.*;

abstract class Person {
    private int id;
    private String name;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public abstract void displayDetails();
}

class Student extends Person {
    private String department;
    private List<Book> borrowedBooks;

    public Student(int id, String name, String department) {
        super(id, name);
        this.department = department;
        borrowedBooks = new ArrayList<>();
    }

    public String getDepartment() {
        return department;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    public void returnBook(Book book) {
        borrowedBooks.remove(book);
    }

    @Override
    public void displayDetails() {
        System.out.println("Student ID: " + getId());
        System.out.println("Name: " + getName());
        System.out.println("Department: " + department);
        System.out.println("Borrowed Books:");
        if (borrowedBooks.isEmpty()) {
            System.out.println("None");
        } else {
            for (Book b : borrowedBooks) {
                System.out.println(b.getBookName());
            }
        }
    }
}

class Librarian extends Person {
    public Librarian(int id, String name) {
        super(id, name);
    }

    @Override
    public void displayDetails() {
        System.out.println("Librarian ID: " + getId());
        System.out.println("Name: " + getName());
    }
}

class Book {
    private int bookId;
    private String bookName;
    private String authorName;
    private boolean available;

    public Book(int bookId, String bookName, String authorName) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.authorName = authorName;
        this.available = true;
    }

    public int getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void displayBook() {
        System.out.println("Book ID: " + bookId);
        System.out.println("Book Name: " + bookName);
        System.out.println("Author: " + authorName);
        System.out.println("Status: " + (available ? "Available" : "Issued"));
        System.out.println();
    }
}

interface LibraryOperations {
    void issueBook(int studentId, int bookId) throws Exception;
    void returnBook(int studentId, int bookId) throws Exception;
}

class Library implements LibraryOperations {
    private List<Book> books;
    private List<Student> students;

    public Library() {
        books = new ArrayList<>();
        students = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }

        for (Book book : books) {
            book.displayBook();
        }
    }

    public void viewStudent(int studentId) throws Exception {
        Student student = findStudent(studentId);
        student.displayDetails();
    }

    private Book findBook(int bookId) throws Exception {
        for (Book book : books) {
            if (book.getBookId() == bookId) {
                return book;
            }
        }
        throw new Exception("Book not found.");
    }

    private Student findStudent(int studentId) throws Exception {
        for (Student student : students) {
            if (student.getId() == studentId) {
                return student;
            }
        }
        throw new Exception("Invalid student ID.");
    }

    @Override
    public void issueBook(int studentId, int bookId) throws Exception {
        Student student = findStudent(studentId);
        Book book = findBook(bookId);

        if (!book.isAvailable()) {
            throw new Exception("Book already issued.");
        }

        if (student.getBorrowedBooks().size() >= 3) {
            throw new Exception("Student exceeds borrow limit.");
        }

        book.setAvailable(false);
        student.borrowBook(book);

        System.out.println("Book issued successfully.");
    }

    @Override
    public void returnBook(int studentId, int bookId) throws Exception {
        Student student = findStudent(studentId);
        Book book = findBook(bookId);

        if (!student.getBorrowedBooks().contains(book)) {
            throw new Exception("Student did not borrow this book.");
        }

        student.returnBook(book);
        book.setAvailable(true);

        System.out.println("Book returned successfully.");
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Library library = new Library();
        Librarian librarian = new Librarian(1, "Admin");

        while (true) {
            System.out.println("\n1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Add Student");
            System.out.println("4. Issue Book");
            System.out.println("5. Return Book");
            System.out.println("6. View Student Details");
            System.out.println("7. Exit");
            System.out.print("Enter Choice: ");

            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1:
                        System.out.print("Book ID: ");
                        int bookId = Integer.parseInt(sc.nextLine());

                        System.out.print("Book Name: ");
                        String bookName = sc.nextLine();

                        System.out.print("Author Name: ");
                        String author = sc.nextLine();

                        library.addBook(new Book(bookId, bookName, author));
                        System.out.println("Book added successfully.");
                        break;

                    case 2:
                        library.viewBooks();
                        break;

                    case 3:
                        System.out.print("Student ID: ");
                        int studentId = Integer.parseInt(sc.nextLine());

                        System.out.print("Student Name: ");
                        String studentName = sc.nextLine();

                        System.out.print("Department: ");
                        String dept = sc.nextLine();

                        library.addStudent(new Student(studentId, studentName, dept));
                        System.out.println("Student added successfully.");
                        break;

                    case 4:
                        System.out.print("Student ID: ");
                        int sid = Integer.parseInt(sc.nextLine());

                        System.out.print("Book ID: ");
                        int bid = Integer.parseInt(sc.nextLine());

                        library.issueBook(sid, bid);
                        break;

                    case 5:
                        System.out.print("Student ID: ");
                        int rsid = Integer.parseInt(sc.nextLine());

                        System.out.print("Book ID: ");
                        int rbid = Integer.parseInt(sc.nextLine());

                        library.returnBook(rsid, rbid);
                        break;

                    case 6:
                        System.out.print("Student ID: ");
                        int viewId = Integer.parseInt(sc.nextLine());

                        library.viewStudent(viewId);
                        break;

                    case 7:
                        System.out.println("Exiting...");
                        sc.close();
                        return;

                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}