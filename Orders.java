package project212.phase2;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.Stack;

public class Orders {

    private BST<Order> all_orders;      // BST لجميع الطلبات
    private Customers all_Customers;    // العملاء
    private String filePath;            // مسار ملف الحفظ

    static DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Constructor مع بيانات موجودة
    public Orders(BST<Customer> input_customers, BST<Order> ordersInput) {
        all_Customers = new Customers(input_customers);
        this.all_orders = ordersInput;
    }

    // Constructor فارغ
    public Orders() {
        all_Customers = new Customers();
        all_orders = new BST<>();
    }

    public void setFilePath(String path) {
        this.filePath = path;
    }

    public BST<Order> get_Orders() {
        return all_orders;
    }

    // البحث عن الطلب حسب ID
    public Order searchOrderById(int id) {
        if (all_orders.empty()) return null;
        boolean found = all_orders.findKey(id);
        return found ? all_orders.retrieve() : null;
    }

    // إزالة طلب حسب ID
    public void removeOrder(int id) {
        boolean removed = all_orders.removeKey(id);
        if (removed)
            System.out.println("Order removed: " + id);
        else
            System.out.println("Order ID not found!");
    }

    // تعيين الطلب للعميل
    public void assign(Order ord) {
        Customer p = all_Customers.searchById(ord.getCustomerId());
        if (p == null)
            System.out.println("Customer " + ord.getCustomerId() + " does not exist to assign the order.");
        else
            p.addOrder(ord);
    }

    // إضافة طلب مع التحقق من وجوده
    public void addOrder(Order ord) {
        boolean inserted = all_orders.insert(ord.getOrderId(), ord);
        if (inserted) {
            System.out.println("Order added: " + ord.getOrderId());
            assign(ord);
            saveAll(); // حفظ تلقائي بعد الإضافة
        } else {
            System.out.println("Order with ID " + ord.getOrderId() + " already exists!");
        }
    }

    // تحويل من CSV إلى Order
    public static Order convert_String_to_order(String line) {
        String[] a = line.split(",", 6);

        int orderId     = Integer.parseInt(a[0].trim().replace("\"",""));
        int customerId  = Integer.parseInt(a[1].trim().replace("\"",""));
        String prodIds  = a[2].trim().replace("\"","");
        double total    = Double.parseDouble(a[3].trim().replace("\"",""));
        LocalDate date  = LocalDate.parse(a[4].trim().replace("\"",""), df);
        String status   = a[5].trim().replace("\"","");

        return new Order(orderId, customerId, prodIds, total, date, status);
    }

    // تحميل الطلبات من ملف CSV
    public void loadOrders(String fileName) {
        try {
            filePath = fileName;
            File f = new File(fileName);
            Scanner read = new Scanner(f);

            System.out.println("Reading file: " + fileName);
            if (read.hasNextLine()) read.nextLine(); // تخطي الهيدر

            while (read.hasNextLine()) {
                String line = read.nextLine().trim();
                if (line.isEmpty()) continue;

                Order ord = convert_String_to_order(line);

                // إدراج في BST
                all_orders.insert(ord.getOrderId(), ord);
            }

            read.close();
            System.out.println("File loaded successfully!\n");

        } catch (Exception e) {
            System.out.println("Error loading orders: " + e.getMessage());
        }
    }

    // عرض جميع الطلبات (ترتيب تصاعدي حسب المفتاح)
    public void displayAllOrders() {
        if (all_orders.empty()) {
            System.out.println("No orders found!");
            return;
        }

        System.out.println("OrderID\tCustomerID\tProductIDs\tTotalPrice\tDate\t\tStatus");
        System.out.println("--------------------------------------------------------------------------");

        inOrderTraversal(all_orders.getRoot());

        System.out.println("--------------------------------------------------------------------------");
    }

    // Traversal inorder للعرض
    private void inOrderTraversal(BSTNode<Order> node) {
        if (node == null) return;

        inOrderTraversal(node.left);

        Order o = node.data;
        System.out.println(o);

        inOrderTraversal(node.right);
    }

    // حفظ الطلبات في CSV
    private void saveAll() {
        if (filePath == null || filePath.isEmpty()) return;

        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

            pw.println("orderId,customerId,productIds,totalPrice,date,status");

            if (!all_orders.empty()) {
                Stack<BSTNode<Order>> stack = new Stack<>();
                BSTNode<Order> current = all_orders.getRoot();

                while (current != null || !stack.isEmpty()) {

                    // الذهاب لليسار
                    while (current != null) {
                        stack.push(current);
                        current = current.left;
                    }

                    // زيارة العقدة
                    current = stack.pop();
                    Order o = current.data;

                    pw.println(
                        o.getOrderId() + "," +
                        o.getCustomerId() + "," +
                        o.getProd_Ids() + "," +
                        o.getTotalPrice() + "," +
                        o.getOrderDate().toString() + "," +
                        o.getStatus()
                    );

                    // الانتقال لليمين
                    current = current.right;
                }
            }

        } catch (Exception e) {
            System.out.println("Error saving orders: " + e.getMessage());
        }
    }

    // عرض الطلبات بين تاريخين
    public void displayAllOrders_between2dates(LocalDate d1, LocalDate d2) {
        if (all_orders.empty()) {
            System.out.println("No orders found.");
            return;
        }

        System.out.println("Orders between " + d1 + " and " + d2 + ":");

        boolean any = false;

        Stack<BSTNode<Order>> stack = new Stack<>();
        BSTNode<Order> current = all_orders.getRoot();

        while (current != null || !stack.isEmpty()) {

            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            current = stack.pop();
            Order o = current.data;

            if (!o.getOrderDate().isBefore(d1) && !o.getOrderDate().isAfter(d2)) {
                System.out.println(o.getOrderId());
                any = true;
            }

            current = current.right;
        }

        if (!any) System.out.println("No results.");
        System.out.println("-----------------------------------");
    }
}
