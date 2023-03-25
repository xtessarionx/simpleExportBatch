package com.sat.demo.BatchDemoAgain.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sat.demo.BatchDemoAgain.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Service
public class BatchDemoExportFileServiceImpl implements BatchDemoExportFileService{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void exportDBtoCSV() {
        log.info("Exporting---CSV");
        String sql = "select customer_id,contact,country,dob,email,first_name,last_name,gender from customers_info";
        List<Customer> customersList = jdbcTemplate.query(sql,
                (rs, rowNum) -> new Customer(rs.getInt("customer_id"),
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getString("email"),
                                rs.getString("gender"),
                                rs.getString("contact"),
                                rs.getString("country"),
                                rs.getString("dob")
                            )
        );
        String targetPath = "D://work/customer.csv"; //Target path that you want
        try(
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(targetPath));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("id","firstName","lastName","email","gender","contactNo","country","dob"));
        ) {
            for (Customer c:customersList ) {
                csvPrinter.printRecord(c.getId(),c.getFirstName(),c.getLastName(),c.getEmail(),
                                                c.getGender(),c.getContactNo(),c.getCountry(),c.getDob());
            }
            csvPrinter.close();
            log.info("Export---CSV successfully");
        }catch (IOException e) {
            log.error("Error while writing csv ", e);
            System.exit(1);
        }
    }

    @Override
    public void exportDBtoTxt() {
        String targetPath = "D://work/customer.txt"; //Target path that you want
        log.info("Exporting---TXT");
        String sql = "select customer_id,contact,country,dob,email,first_name,last_name,gender from customers_info";
        List<Customer> customersList = jdbcTemplate.query(sql,
                (rs, rowNum) -> new Customer(rs.getInt("customer_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("gender"),
                        rs.getString("contact"),
                        rs.getString("country"),
                        rs.getString("dob")
                )
        );
        try {
            FileWriter myWriter = new FileWriter(targetPath);
            for (Customer c:customersList) {
                String customerData = "id:"+c.getId()+" Firstname:"+c.getFirstName()+" LastName:"+c.getLastName()
                        +" Email:"+c.getEmail()+" Gender: "+c.getGender()+" ContectNo:"+c.getContactNo()
                        +" Country: "+c.getCountry()+" Date-of-Birth:"+c.getDob();
                myWriter.write(customerData+"\n");
            }
            myWriter.close();
            log.info("Export---TXT successfully");
        } catch (IOException e) {
            log.error("Error while writing csv ", e);
            System.exit(1);
        }
    }

    @Override
    public void exportDBtoPDF() throws IOException{

        String targetPath = "D://work/customer.pdf"; //Target path that you want
        log.info("Exporting---PDF");
        String sql = "select customer_id,contact,country,dob,email,first_name,last_name,gender from customers_info";
        List<Customer> customersList = jdbcTemplate.query(sql,
                (rs, rowNum) -> new Customer(rs.getInt("customer_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("gender"),
                        rs.getString("contact"),
                        rs.getString("country"),
                        rs.getString("dob")
                )
        );
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(targetPath);
            Document document = new Document(PageSize.A4);

            PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);
            document.open();

            Font font = FontFactory.getFont(FontFactory.HELVETICA);
            font.setSize(9);

            PdfPTable table = new PdfPTable(8);

            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(Color.getHSBColor(198,58,70));
            cell.setPadding(5);

            cell.setPhrase(new Phrase("User ID", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Firstname", font));
            table.addCell(cell);

            cell.setPhrase(new Phrase("Lastname", font));
            table.addCell(cell);
            
            cell.setPhrase(new Phrase("Email", font));
            table.addCell(cell);

            cell.setPhrase(new Phrase("Gender", font));
            table.addCell(cell);

            cell.setPhrase(new Phrase("Contact", font));
            table.addCell(cell);

            cell.setPhrase(new Phrase("Country", font));
            table.addCell(cell);

            cell.setPhrase(new Phrase("Date-of-Birth", font));
            table.addCell(cell);
            for (Customer customer : customersList) {
                table.addCell(new Phrase(String.valueOf(customer.getId()),font));
                table.addCell(new Phrase(customer.getFirstName(),font));
                table.addCell(new Phrase(customer.getLastName(),font));
                table.addCell(new Phrase(customer.getEmail(),font));
                table.addCell(new Phrase(customer.getGender(),font));
                table.addCell(new Phrase(customer.getContactNo(),font));
                table.addCell(new Phrase(customer.getCountry(),font));
                table.addCell(new Phrase(customer.getDob(),font));
            }
            document.add(table);
            document.close();
            writer.close();
            log.info("Export---PDF successfully");
        }catch (IOException e){
            log.error("Error while exporting PDF file");
            System.exit(1);
        }
    }

}
