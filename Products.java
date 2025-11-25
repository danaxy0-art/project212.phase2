package project212.phase2;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
public class Products {

    private BST<Product> products;
    private String filePath;

    public Products(BST<Product> input_products) {
        products = input_products;
    }

    public Products() {
        products = new BST_int<>();
    }

    public void setFilePath(String path) { this.filePath = path; }

    public BST<Product> get_Products() { return products; }

    public Product SearchProductById(int id){
        if (products.empty()) {
        	return null;
    }
    boolean found = products.findkey(id);
    	if(found)
    		return products.retrieve();
    	else
    		return null;
    }
    public void addProduct1(Product p) {
        if (SearchProductById(p.getProductId())==null) { //not exist
            products.insert(p.getProductId(),p);
            System.out.println("Product added: " + p.getName());
            saveAll();
        } else {
            System.out.println("Product with ID " + p.getProductId() + " already exists!");
        }
    }

    public void addProduct(Product p) {
        boolean inserted = products.insert(p.getProductId(),p); //not exist
        System.out.println("Product added: " + p.getName());
    }else {
    	System.out.println("Product with ID " + p.getProductId() + " already exists!");
    }


    public void removeProduct(int id) {
        boolean removed = products.removekey(id);
        	if(removed) {
                System.out.println("Product removed: " + id);
                saveAll();
            }else {
        System.out.println("Product ID not found");
            }
    }

    public void updateProduct(int id, Product p) {
        Product old=SearchProductById(id);
        if(old==null)
            System.out.println("not exist to make update");
        else
            old.UpdateProduct(p);
            saveAll();
        }
    

    public void displayOutOfStock() {
        System.out.println("Out of stock products:");
        if (products.empty()){
            System.out.println("no products exist");
        } else {
            inOrderOutOfStock(products.getRoot());
        }
    }
    public void inOrderOutOfStock(BSTNode<Product>p) {
    	if(p == null) return;
    	inOrderOutOfStock(p.left);
    		if(products.retrieve().getStack()==0) {
    			System.out.println("key = "+p.key);
    			products.retrieve().display();
    			
    		}
    		inOrderOutOfStock(p.right);
    }
    public void displayAllProducts() {
        System.out.println("All Products");
        if (products.empty()){
            System.out.println("no products exist");
            return ;
        } else {
        	inOrderAllProducts(products.getRoot());
            }
        }
    public void inOrderAllProducts(BSTNode<Product>p) {
    if(p == null) return;
    	inOrderAllProducts(p.left);
    	p.data.pisplay();
    	p.data.displayReviews();
    	inOrderAllProducts(p.right);
    
    }
    public void assign(Review r) {
    	Product p= searchProductById(r.getProductId());
    	if(p == null)
    		System.out.println("not exist to assign review"+ r.getReviewId()+"to it");
    	else
    		p.addReview(r);
    }

    public static Product convert_String_to_product(String Line) {
        String a[]=Line.split(",",4);
        Product p=new Product(Integer.parseInt(a[0].trim()), a[1].trim(),
                              Double.parseDouble(a[2].trim()),Integer.parseInt(a[3].trim()));
        return p;
    }

    public void loadProducts(String fileName) {
        try {
            filePath = fileName;
            File f = new File(fileName);
            Scanner read = new Scanner(f);
            System.out.println("Reading file: " + fileName);
            read.nextLine().trim();
            while(read.hasNextLine()) {
            	String line = read.nextLine().trim();
            	if(!line.isEmpty()) {
            		Product p = convertStingToProduct(line);
            		addProduct(p);
            read.close();
            System.out.println("File loaded successfully!\n");
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
        }
    }

    private void saveAll() {
        if (filePath == null || filePath.isEmpty()) return;
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println("productId,name,price,stock");
            if (!products.empty()) {
                products.findfirst();
                while (true) {
                    Product p = products.retrieve();
                    pw.println(p.getProductId()+","+p.getName()+","+p.getPrice()+","+p.getStock());
                    if (products.last()) break;
                    products.findenext();
                }
            }
        } catch (Exception e) {
            System.out.println("Error saving products: " + e.getMessage());
        }
    }
}
}
}
