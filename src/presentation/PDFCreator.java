package presentation;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.Clients;
import model.Products;
import presentation.reportEntities.OrderReportEntity;

import java.io.FileOutputStream;
import java.util.ArrayList;

/**Outputs the pdf file requested.
 * @author Stefan Ciuprina
 * @version 1.0
 * @since 1.0
 */

public class PDFCreator {

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    private static int clientsReportNo = 1, productsReportNo = 1, ordersReportNo = 1, underStockedNo = 1;

    /**Generates a bill pdf file containing the information given as parameters.
     */

    public static void generateBill(int orderID, String clientName, String productName, int quantity, double pricePerOne, String date) {
        String FILE = "bill_order_no" + orderID + ".pdf";
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            addBillMetaData(document, orderID);
            addBillContent(document, orderID, clientName, productName, quantity, pricePerOne, date);
            document.close();
        } catch (Exception e) {
           printEditError(FILE);
        }
    }

    /**Generates an understocked notice pdf, telling the user which product is understocked and why
     * (which client tried to get more units than existing and what was that amount).
     */

    public static void generateUnderstockedNotice
            (String productName, int productQuantity, String clientName, int requestedQuantity, String date) {
        String FILE = "understocked_" + productName + (underStockedNo++) + ".pdf";
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            addUnderstockedMetaData(document, productName);
            addUnderstockedContent(document, productName, productQuantity, clientName, requestedQuantity, date);
            document.close();
        } catch (Exception e) {
            printEditError(FILE);
        }
    }

    /**Private method used by generateBill method. Adds the meta data to the pdf file.
     */

    private static void addBillMetaData(Document document, int orderID) {
        document.addTitle("Order number " + orderID);
        document.addSubject("Bill");
        document.addAuthor("Order Management App");
        document.addCreator("Order Management App");
    }

    /**Private method used by generateUnderstockedNotice method. Adds the meta data to the pdf file.
     */

    private static void addUnderstockedMetaData(Document document, String productName) {
        document.addTitle("Understocked product");
        document.addSubject(productName);
        document.addAuthor("Order Management App");
        document.addCreator("Order Management App");
    }

    /**Private method used by generateBill method. Adds the content to the pdf file with the information given as parameters.
     * @throws DocumentException
     */

    private static void addBillContent(Document document, int orderID, String clientName, String productName, int quantity, double pricePerOne, String date)
            throws DocumentException{
        Paragraph bill = new Paragraph();
        addEmptyLine(bill);
        bill.add(new Paragraph("Bill", catFont));
        bill.add(new Paragraph("Order number " + orderID, smallBold));
        bill.add(new Paragraph("Date & Time: " + date, smallBold));
        addEmptyLine(bill);
        List list = new List(false, false, 10);
        list.add(new ListItem("Client name: " + clientName));
        list.add(new ListItem("Product: " + productName));
        list.add(new ListItem("Product price: " + pricePerOne));
        list.add(new ListItem("Quantity: " + quantity));
        bill.add(list);
        addEmptyLine(bill);
        bill.add(new Paragraph("TOTAL: " + (quantity * pricePerOne), subFont));
        document.add(bill);
    }

    /**Private method used by generateUnderstockedNotice method. Adds the content to the pdf file with the information given as parameters.
     * @throws DocumentException
     */

    public static void addUnderstockedContent(Document document, String productName, int productQuantity, String clientName, int requestedQuantity, String date)
            throws DocumentException{
        Paragraph understocked = new Paragraph();
        addEmptyLine(understocked);
        understocked.add(new Paragraph("Understocked product", catFont));
        understocked.add(new Paragraph(productName, subFont));
        addEmptyLine(understocked);
        understocked.add(new Paragraph("Client " + clientName + " tried to order " + requestedQuantity
                + " units, but there were only " + productQuantity + " left.", smallBold));
        understocked.add(new Paragraph("Order attempt date & time: " + date, smallBold));
        document.add(understocked);
    }

    /**Adds an empty line to the paragraph given as parameter
     */

    private static void addEmptyLine(Paragraph paragraph) {
        paragraph.add(new Paragraph(" "));
    }

    /**Prints an error message saying the FILE String couldn't be created.
     */

    private static void printEditError(String FILE) {
        System.out.println("ERROR creating " + FILE + ". Please close the file before running the app.");
    }

    ///REPORTS

    /**Generates a pdf file with a table containing the client information given in the ArrayList provided as parameter.
     */

    public static void generateClientsReport(ArrayList<Clients> allClients) {
        String FILE = "report_clients" + (clientsReportNo++) + ".pdf";
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            addReportMetaData(document, "Clients");
            addClientsReportContent(document, allClients);
            document.close();
        } catch (Exception e) {
            printEditError(FILE);
        }
    }

    /**Generates a pdf file with a table containing the product information given in the ArrayList provided as parameter.
     */

    public static void generateProductsReport(ArrayList<Products> allProducts) {
        String FILE = "report_products" + (productsReportNo++) + ".pdf";
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            addReportMetaData(document, "Products");
            addProductsReportContent(document, allProducts);
            document.close();
        } catch (Exception e) {
            printEditError(FILE);
        }
    }

    /**Generates a pdf file with a table containing the order information given in the ArrayList provided as parameter.
     */

    public static void generateOrdersReport(ArrayList<OrderReportEntity> allOrders) {
        String FILE = "report_orders" + (ordersReportNo++) + ".pdf";
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            addReportMetaData(document, "Orders");
            addOrdersReportContent(document, allOrders);
            document.close();
        } catch (Exception e) {
            printEditError(FILE);
        }
    }

    /**Private method used by all generate-report methods. Adds the meta data to the pdf file.
     */

    private static void addReportMetaData(Document document, String reportName) {
        document.addTitle("Report");
        document.addSubject(reportName);
        document.addAuthor("Order Management App");
        document.addCreator("Order Management App");
    }

    /**Creates the table containing the client information and inserts it in the document.
     * @param allClients the client data
     * @throws DocumentException
     */

    public static void addClientsReportContent(Document document, ArrayList<Clients> allClients) throws DocumentException {
        Paragraph report = new Paragraph();
        addEmptyLine(report);
        report.add(new Paragraph("Report", catFont));
        report.add(new Paragraph("Clients", subFont));
        addEmptyLine(report);
        PdfPTable table = new PdfPTable(3);
        String[] fields = {"ID", "Name", "Address"};
        addTableHeaders(table, fields);
        for(Clients client : allClients) {
            PdfPCell c = new PdfPCell(new Phrase(String.valueOf(client.getId())));
            c.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(c);
            table.addCell(client.getName());
            table.addCell(client.getAddress());
        }
        report.add(table);
        document.add(report);
    }

    /**Creates the table containing the product information and inserts it in the document.
     * @param allProducts the product data
     * @throws DocumentException
     */

    public static void addProductsReportContent(Document document, ArrayList<Products> allProducts) throws DocumentException {
        Paragraph report = new Paragraph();
        addEmptyLine(report);
        report.add(new Paragraph("Report", catFont));
        report.add(new Paragraph("Products", subFont));
        addEmptyLine(report);

        PdfPTable table = new PdfPTable(4);
        String[] fields = {"ID", "Name", "Quantity", "Price"};
        addTableHeaders(table, fields);

        for(Products product : allProducts) {
            PdfPCell c = new PdfPCell(new Phrase(String.valueOf(product.getId())));
            c.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(c);
            table.addCell(product.getName());
            table.addCell(String.valueOf(product.getQuantity()));
            table.addCell(String.valueOf(product.getPrice()));
        }

        report.add(table);
        document.add(report);
    }

    /**Creates the table containing the order information and inserts it in the document.
     * @param allOrders the order data
     * @throws DocumentException
     */

    public static void addOrdersReportContent(Document document, ArrayList<OrderReportEntity> allOrders) throws DocumentException {
        Paragraph report = new Paragraph();
        addEmptyLine(report);
        report.add(new Paragraph("Report", catFont));
        report.add(new Paragraph("Orders", subFont));
        addEmptyLine(report);

        PdfPTable table = new PdfPTable(6);
        String[] fields = {"ID", "Client", "Product", "Quantity", "Total price", "Date & Time"};
        addTableHeaders(table, fields);

        for(OrderReportEntity order : allOrders) {
            PdfPCell c = new PdfPCell(new Phrase(String.valueOf(order.getId())));
            c.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(c);
            table.addCell(order.getClientName());
            table.addCell(order.getProductName());
            table.addCell(String.valueOf(order.getQuantity()));
            table.addCell(String.valueOf(order.getTotalPrice()));
            table.addCell(order.getDate());
        }

        report.add(table);
        document.add(report);
    }

    /**Adds to the table the header text for each column.
     * @param fields each header text; will placed from left to right in the table.
     */

    private static void addTableHeaders(PdfPTable table, String[] fields) {
        for(String field : fields) {
            PdfPCell c = new PdfPCell(new Phrase(field));
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c);
        }
        table.setHeaderRows(1);
    }
}
