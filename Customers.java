package project212.phase2;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;

public class Customers {
    private BST<Customer> customers;
    private String filePath; // <--- أضفنا المتغير هنا

    public Customers() {
        customers = new BST<>();
    }

    public Customers(BST<Customer> input_customers) {
        customers = input_customers;
    }

    public void setFilePath(String path) {
        this.filePath = path;
    }

    public BST<Customer> get_customers() {
        return customers;
    }

    public Customer searchById(int id) {
        if (customers.empty())
            return null;
        boolean found = customers.findKey(id);
        if (found)
            return customers.retrieve();
        else
            return null;
    }

    public void addCustomer(Customer c) {
        boolean inserted = customers.insert(c.getCustomerId(), c);
        if (inserted) {
            System.out.println("Customer added: " + c.getName());
            saveAll(); // حفظ تلقائي بعد الإضافة
        } else {
            System.out.println("Customer ID already exists!");
        }
    }

    public void displayAll() {
        System.out.println("=== All customers ===");
        if (customers.empty()) {
            System.out.println("No customers exist");
            return;
        } else {
            inOrder_all_customers(customers.getRoot());
        }
    }

    private void inOrder_all_customers(BSTNode<Customer> c) {
        if (c == null) return;
        inOrder_all_customers(c.left);
        c.data.display();
        inOrder_all_customers(c.right);
    }

    public static Customer convert_String_to_Customer(String Line) {
        String a[] = Line.split(",");
        Customer p = new Customer(Integer.parseInt(a[0].trim()), a[1].trim(), a[2].trim());
        return p;
    }

    public void loadCustomers(String fileName) {
        try {
            setFilePath(fileName); // <-- تحديد المسار
            File f = new File(fileName);
            Scanner read = new Scanner(f);
            System.out.println("Reading file: " + fileName);
            System.out.println("-----------------------------------");
            if (read.hasNextLine()) read.nextLine(); // skip header

            while (read.hasNextLine()) {
                String line = read.nextLine().trim();
                if (line.isEmpty()) continue;
                Customer c = convert_String_to_Customer(line);
                addCustomer(c); // سيحفظ تلقائياً بعد الإضافة
            }

            read.close();
            System.out.println("-----------------------------------");
            System.out.println("Customers loaded successfully!\n");
        } catch (Exception e) {
            System.out.println("Error loading customers: " + e.getMessage());
        }
    }

    private void saveAll() {
        if (filePath == null || filePath.isEmpty()) return;
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println("customerId,name,email"); // header
            if (!customers.empty()) {
                Stack<BSTNode<Customer>> stack = new Stack<>();
                BSTNode<Customer> current = customers.getRoot();
                while (current != null || !stack.isEmpty()) {
                    while (current != null) {
                        stack.push(current);
                        current = current.left;
                    }
                    current = stack.pop();
                    Customer c = current.data;
                    pw.println(c.getCustomerId() + "," + c.getName() + "," + c.getEmail());
                    current = current.right;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
