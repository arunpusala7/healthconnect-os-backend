package com.example.appointment_booking_system.service;

import com.example.appointment_booking_system.entity.Appointment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

@Service
public class PdfGeneratorService {

    public byte[] generateAppointmentTicket(Appointment appointment) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // ===========================================
            // 0. STYLING & COLORS
            // ===========================================
            Color primaryColor = new Color(0, 102, 204); // Medical Blue
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 28, primaryColor);
            Font subHeaderFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.DARK_GRAY);
            Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.GRAY);
            Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 14, Color.BLACK);

            // ===========================================
            // 1. COMPANY HEADER
            // ===========================================
            Paragraph companyName = new Paragraph("HEALTH CONNECT", headerFont);
            companyName.setAlignment(Element.ALIGN_CENTER);
            document.add(companyName);

            Paragraph slogan = new Paragraph("Your Health, Our Priority", subHeaderFont);
            slogan.setAlignment(Element.ALIGN_CENTER);
            document.add(slogan);

            // Add a line separator
            document.add(new Paragraph("\n"));

            // ===========================================
            // 2. BIG QR CODE
            // ===========================================
            PdfPTable qrTable = new PdfPTable(1);
            qrTable.setWidthPercentage(100);

            try {
                // Generate Higher Quality QR (400x400)
                byte[] qrBytes = generateQrCodeImage(appointment.getTicketId(), 400, 400);
                Image qrImage = Image.getInstance(qrBytes);

                // SCALE IT UP: Make it 200x200 pixels on the PDF
                qrImage.scaleAbsolute(200, 200);

                PdfPCell qrCell = new PdfPCell(qrImage);
                qrCell.setBorder(Rectangle.NO_BORDER);
                qrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                qrCell.setPaddingTop(20f);
                qrCell.setPaddingBottom(20f);

                qrTable.addCell(qrCell);
                document.add(qrTable);

            } catch (Exception e) {
                e.printStackTrace();
            }

            // ===========================================
            // 3. APPOINTMENT DETAILS (Boxed Layout)
            // ===========================================
            PdfPTable detailsTable = new PdfPTable(2);
            detailsTable.setWidthPercentage(90); // Use 90% of page width
            detailsTable.setWidths(new float[]{1, 2}); // Label column smaller
            detailsTable.setSpacingBefore(10f);

            // Add Rows with padding for a cleaner look
            addTableRow(detailsTable, "Ticket ID:", appointment.getTicketId(), labelFont, valueFont);
            addTableRow(detailsTable, "Patient Name:", appointment.getUser().getName(), labelFont, valueFont);
            addTableRow(detailsTable, "Doctor:", "Dr. " + appointment.getDoctor().getName(), labelFont, valueFont); // Added "Dr."
            addTableRow(detailsTable, "Specialization:", appointment.getDoctor().getSpecialization(), labelFont, valueFont);
            addTableRow(detailsTable, "Date:", appointment.getAppointmentDate().toString(), labelFont, valueFont);
            addTableRow(detailsTable, "Time:", appointment.getStartTime() + " - " + appointment.getEndTime(), labelFont, valueFont);

            document.add(detailsTable);

            // ===========================================
            // 4. FOOTER
            // ===========================================
            document.add(new Paragraph("\n\n"));
            Paragraph footer = new Paragraph("Please arrive 15 minutes before your scheduled time.", subHeaderFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }

    private byte[] generateQrCodeImage(String text, int width, int height) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        MatrixToImageConfig config = new MatrixToImageConfig(0xFF000000, 0xFFFFFFFF);
        MatrixToImageWriter.writeToStream(bitMatrix, "JPG", stream, config);
        return stream.toByteArray();
    }

    private void addTableRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        // Label Cell
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(Rectangle.BOTTOM); // Only bottom border for modern look
        labelCell.setBorderColor(Color.LIGHT_GRAY);
        labelCell.setPadding(10f);
        labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(labelCell);

        // Value Cell
        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(Rectangle.BOTTOM);
        valueCell.setBorderColor(Color.LIGHT_GRAY);
        valueCell.setPadding(10f);
        valueCell.setPaddingLeft(20f); // Indent value
        valueCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(valueCell);
    }
}