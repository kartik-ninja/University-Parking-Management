package com.university.parking.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import com.university.parking.model.ParkingAssignment;
import com.university.parking.model.ParkingViolation;
import com.university.parking.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private static final Font TITLE = new Font(Font.HELVETICA, 16, Font.BOLD);
    private static final Font HEADER = new Font(Font.HELVETICA, 12, Font.BOLD);
    private static final Font BODY = new Font(Font.HELVETICA, 12);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public void generateBookingReport(List<ParkingAssignment> bookings, HttpServletResponse resp) throws Exception {
        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition", "attachment; filename=booking-report.pdf");

        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, resp.getOutputStream());
        doc.open();

        doc.add(new Paragraph("🅿️ Booking Report", TITLE));
        doc.add(new Paragraph("Generated: " + FMT.format(java.time.LocalDateTime.now()), BODY));
        doc.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        addHeaderRow(table, "Slot", "User", "Role", "Start", "End");

        for (ParkingAssignment a : bookings) {
            table.addCell(new Phrase(a.getSlot().getSlotNumber(), BODY));
            table.addCell(new Phrase(a.getUser().getFirstName() + " " + a.getUser().getLastName(), BODY));
            table.addCell(new Phrase(a.getUser().getRoles().stream().findFirst().get().getName().replace("ROLE_", ""), BODY));
            table.addCell(new Phrase(FMT.format(a.getStartTime()), BODY));
            table.addCell(new Phrase(FMT.format(a.getEndTime()), BODY));
        }

        doc.add(table);
        doc.close();
    }

    @Override
    public void generateViolationReport(List<ParkingViolation> vios, HttpServletResponse resp) throws Exception {
        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition", "attachment; filename=violation-report.pdf");

        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, resp.getOutputStream());
        doc.open();

        doc.add(new Paragraph("🚨 Violation Report", TITLE));
        doc.add(new Paragraph("Generated: " + FMT.format(java.time.LocalDateTime.now()), BODY));
        doc.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        addHeaderRow(table, "Slot", "Vehicle No.", "Reported By", "Time", "Description");

        for (ParkingViolation v : vios) {
            table.addCell(new Phrase(v.getSlot().getSlotNumber(), BODY));
            table.addCell(new Phrase(v.getVehicleNumber(), BODY));
            table.addCell(new Phrase(v.getReportedBy(), BODY));
            table.addCell(new Phrase(FMT.format(v.getReportedAt()), BODY));
            table.addCell(new Phrase(v.getDescription(), BODY));
        }

        doc.add(table);
        doc.close();
    }

    private void addHeaderRow(PdfPTable table, String... headers) {
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, HEADER));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(cell);
        }
    }
}
