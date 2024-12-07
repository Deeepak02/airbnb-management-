package com.airbnb.service;

import com.airbnb.entity.Booking;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class PDFService {

    private final EmailService emailService;

    @Autowired
    public PDFService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void generateAndSendPdf(Booking booking) {
        try {
            // Validate recipient email
            if (!isValidEmail(booking.getEmail())) {
                throw new IllegalArgumentException("Invalid email address: " + booking.getEmail());
            }

            // Generate PDF
            Document document = new Document();
            String filePath = "F://bnb_bookings//" + booking.getId() + "_booking_confirmation.pdf";
            File file = new File(filePath);
            file.getParentFile().mkdirs(); // Ensure directory exists

            FileOutputStream fos = new FileOutputStream(file);
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, fos);
            document.open();

            PdfPTable table = new PdfPTable(3);
            addTableHeader(table);
            addRows(table, booking);
            document.add(table);
            document.close();

            // Send Email with PDF attachment
            String subject = "Booking Confirmation - " + booking.getId();
            String body = "Dear " + booking.getGuestName() + ",\n\nPlease file attached your booking confirmation.\n\nRegards,\nAirbnb Team";
            emailService.sendEmailWithAttachment(booking.getEmail(), subject, body, filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Guest Name", "Hotel Name", "City")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, Booking booking) {
        table.addCell(booking.getGuestName());
        table.addCell(booking.getProperty().getName());
        table.addCell(booking.getProperty().getCity().getName());
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return email != null && Pattern.matches(emailRegex, email);
    }


}
