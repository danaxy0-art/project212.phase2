package project212.phase2;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;

public class Products {

    private BST<Product> products;
    private String filePath;

    public Products(BST<Product> input_products) {
        products = input_products;
    }

    public Products() {
        products = new BST<>();
    }

    public void setFilePath(String path) {
        this.filePath = path;
    }

    public BST<Product> get_Products() {
        return products;
    }

    //     SEARCH PRODUCTS
    public Product SearchProductById(int id) {
        if (products.empty()) {
            return null;
        }

        boolean found = products.findKey(id);
        if (found)
            return products.retrieve();
        else
            return null;
    }
    
    //       ADD PRODUCT
    public void addProduct1(Product p) {
        if (SearchProductById(p.getProductId()) == null) {
            products.insert(p.getProductId(), p);
            System.out.println("Product added: " + p.getName());
            saveAll();
        } else {
            System.out.println("Product with ID " + p.getProductId() + " already exists!");
        }
    }

    public void addProduct(Product p) {
        boolean inserted = products.insert(p.getProductId(), p);
        if (inserted) {
            System.out.println("Product added: " + p.getName());
            saveAll();
        } else {
            System.out.println("Product with ID " + p.getProductId() + " already exists!");
        }
    }
    
    //      REMOVE PRODUCT
    public void removeProduct(int id) {
        boolean removed = products.removeKey(id);
        if (removed) {
            System.out.println("Product removed: " + id);
            saveAll();
        } else {
            System.out.println("Product ID not found");
        }
    }

    //     UPDATE PRODUCT
    public void updateProduct(int id, Product p) {
        Product old = SearchProductById(id);

        if (old == null) {
            System.out.println("not exist to make update");
        } else {
            old.UpdateProduct(p);
            saveAll();
        }
    }

    //    DISPLAY OUT OF STOCK
    public void displayOutOfStock() {
        System.out.println("Out of stock products:");
        if (products.empty()) {
            System.out.println("no products exist");
        } else {
            inOrderOutOfStock(products.getRoot());
        }
    }

    private void inOrderOutOfStock(BSTNode<Product> p) {
        if (p == null) return;

        inOrderOutOfStock(p.left);

        if (p.data.getStock() == 0) {
            System.out.println("key = " + p.key);
            System.out.println(p.data); // display product
        }

        inOrderOutOfStock(p.right);
    }

    //    DISPLAY ALL PRODUCTS
    public void displayAllProducts() {
        System.out.println("All Products");
        if (products.empty()) {
            System.out.println("no products exist");
            return;
        }
        inOrderAllProducts(products.getRoot());
    }

    private void inOrderAllProducts(BSTNode<Product> p) {
        if (p == null) return;

        inOrderAllProducts(p.left);

        System.out.println(p.data.toString());
        p.data.displayReviews();

        inOrderAllProducts(p.right);
    }

    //     ASSIGN REVIEW
    public void assign(Review r) {
        Product p = SearchProductById(r.getProductID());
        if (p == null) {
            System.out.println("not exist to assign review " + r.getReviewID() + " to it");
        } else {
            p.addReview(r);
        }
    }

    //   STRING TO PRODUCT
    public static Product convert_String_to_product(String Line) {
        String[] a = Line.split(",", 4);
        return new Product(
                Integer.parseInt(a[0].trim()),
                a[1].trim(),
                Double.parseDouble(a[2].trim()),
                Integer.parseInt(a[3].trim())
        );
    }

    //        LOAD FILE
    public void loadProducts(String fileName) {
        try {
            filePath = fileName;
            File f = new File(fileName);
            Scanner read = new Scanner(f);

            System.out.println("Reading file: " + fileName);

            if (read.hasNextLine()) read.nextLine(); // skip header

            while (read.hasNextLine()) {
                String line = read.nextLine().trim();

                if (!line.isEmpty()) {
                    Product p = convert_String_to_product(line);
                    addProduct(p);
                }
            }

            read.close();
            System.out.println("File loaded successfully!\n");

        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    //    SAVE ALL TO CSV
    private void saveAll() {

        if (filePath == null || filePath.isEmpty()) return;

        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

            pw.println("productId,name,price,stock");

            if (!products.empty()) {

                Stack<BSTNode<Product>> stack = new Stack<>();
                BSTNode<Product> current = products.getRoot();

                while (current != null || !stack.isEmpty()) {

                    while (current != null) {
                        stack.push(current);
                        current = current.left;
                    }

                    current = stack.pop();
                    Product p = current.data;

                    pw.println(
                            p.getProductId() + "," +
                                    p.getName() + "," +
                                    p.getPrice() + "," +
                                    p.getStock()
                    );

                    current = current.right;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
