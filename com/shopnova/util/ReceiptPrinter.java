package com.shopnova.util;

import com.shopnova.model.CartItem;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.io.*;
import java.time.LocalDate;
import java.util.List;

/**
 * ReceiptPrinter — generates a PDF receipt using Java's built-in
 * PrinterJob → PDF workflow (no external library required).
 * On most systems "Print to PDF" / "Microsoft Print to PDF" is available.
 * We additionally offer a plain-text file fallback.
 */
public class ReceiptPrinter {

    /**
     * Builds the receipt text lines.
     */
    public static String buildReceiptText(
            String customerName, String cashierUsername,
            List<CartItem> cart, double subtotal,
            int pointsRedeemed, double pointsValue,
            double total, double cashPaid, double change,
            int pointsEarned, int newPointsBalance) {

        StringBuilder sb = new StringBuilder();
        sb.append("==============================\n");
        sb.append("       ShopNova RECEIPT        \n");
        sb.append("==============================\n");
        sb.append("Customer : ").append(customerName).append("\n");
        sb.append("Date     : ").append(LocalDate.now()).append("\n");
        sb.append("Cashier  : ").append(cashierUsername).append("\n");
        sb.append("------------------------------\n");
        for (CartItem ci : cart)
            sb.append(String.format("%-14s x%-3d Rs%.2f%n",
                    ci.product.name, ci.qty, ci.total()));
        sb.append("------------------------------\n");
        sb.append(String.format("Subtotal       : Rs %.2f%n", subtotal));
        if (pointsRedeemed > 0)
            sb.append(String.format("Points Redeemed: %d pts (-Rs %.2f)%n",
                    pointsRedeemed, pointsValue));
        sb.append(String.format("TOTAL          : Rs %.2f%n", total));
        sb.append(String.format("Cash Paid      : Rs %.2f%n", cashPaid));
        sb.append(String.format("Change         : Rs %.2f%n", change));
        sb.append("------------------------------\n");
        sb.append(String.format("Points Earned  : +%d pts%n", pointsEarned));
        sb.append(String.format("Points Balance : %d pts%n", newPointsBalance));
        sb.append("  (1 pt = Rs 1 on next purchase)\n");
        sb.append("==============================\n");
        sb.append("    Thank you for shopping!    \n");
        sb.append("==============================\n");
        return sb.toString();
    }

    /**
     * Shows a PDF-save dialog backed by Java2D printing to a PDF file.
     * Uses a JFileChooser so the user picks where to save.
     */
    public static void printToPDF(Component parent, String receiptText) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Save Receipt as PDF");
        fc.setSelectedFile(new File("ShopNova_Receipt_" + LocalDate.now() + ".pdf"));
        javax.swing.filechooser.FileNameExtensionFilter filter =
                new javax.swing.filechooser.FileNameExtensionFilter("PDF Files (*.pdf)", "pdf");
        fc.setFileFilter(filter);

        if (fc.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) return;

        File outFile = fc.getSelectedFile();
        if (!outFile.getName().toLowerCase().endsWith(".pdf"))
            outFile = new File(outFile.getAbsolutePath() + ".pdf");

        // Use Java2D PrinterJob targeting a PDF graphics stream
        // We render with Graphics2D inside a Printable
        final File pdfFile = outFile;
        final String[] lines = receiptText.split("\n");

        PrinterJob job = PrinterJob.getPrinterJob();

        // Set paper to narrow receipt-like size
        PageFormat pf = job.defaultPage();
        Paper paper = new Paper();
        double w = 72 * 3.5;   // 3.5 inch wide
        double h = 72 * (4 + lines.length * 0.22); // dynamic height
        paper.setSize(w, h);
        paper.setImageableArea(10, 10, w - 20, h - 20);
        pf.setPaper(paper);
        pf.setOrientation(PageFormat.PORTRAIT);

        job.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) return Printable.NO_SUCH_PAGE;
            Graphics2D g2 = (Graphics2D) graphics;
            g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            Font receiptFont = new Font("Monospaced", Font.PLAIN, 9);
            Font boldFont    = new Font("Monospaced", Font.BOLD, 10);
            g2.setColor(Color.BLACK);

            float y = 14f;
            float lineH = 13f;
            for (String line : lines) {
                boolean isBold = line.startsWith("===") || line.startsWith("TOTAL")
                        || line.contains("ShopNova");
                g2.setFont(isBold ? boldFont : receiptFont);
                g2.drawString(line, 4, y);
                y += lineH;
            }
            return Printable.PAGE_EXISTS;
        }, pf);

        // Redirect output to file using PDF print service if available,
        // otherwise fall back to saving a .txt with a .pdf extension (still opens in notepad)
        try {
            // Try to find a PDF print service
            javax.print.PrintService[] services =
                    javax.print.PrintServiceLookup.lookupPrintServices(
                            javax.print.DocFlavor.SERVICE_FORMATTED.PRINTABLE, null);

            boolean foundPDF = false;
            for (javax.print.PrintService svc : services) {
                String svcName = svc.getName().toLowerCase();
                if (svcName.contains("pdf") || svcName.contains("xps")) {
                    job.setPrintService(svc);
                    foundPDF = true;
                    break;
                }
            }

            if (foundPDF) {
                job.print();
                JOptionPane.showMessageDialog(parent,
                        "Receipt sent to PDF printer:\n" + pdfFile.getName(),
                        "Receipt Saved", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Fallback: write styled HTML that looks like a receipt and save as .pdf
                // (will render in browser as HTML, still useful)
                saveAsHtmlReceipt(parent, pdfFile, receiptText);
            }
        } catch (Exception ex) {
            // Last resort: save plain text
            try (FileWriter fw = new FileWriter(pdfFile)) {
                fw.write(receiptText);
                JOptionPane.showMessageDialog(parent,
                        "Saved as text receipt:\n" + pdfFile.getAbsolutePath(),
                        "Receipt Saved", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ioEx) {
                JOptionPane.showMessageDialog(parent,
                        "Could not save receipt: " + ioEx.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Saves a nicely styled HTML file that mimics a thermal receipt.
     * Named .html so the browser opens it correctly.
     */
    private static void saveAsHtmlReceipt(Component parent, File targetFile, String receiptText) {
        // Change extension to .html for proper rendering
        File htmlFile = new File(targetFile.getAbsolutePath().replace(".pdf", ".html"));

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
        html.append("<title>ShopNova Receipt</title>");
        html.append("<style>");
        html.append("body{font-family:monospace;background:#f0f0f0;display:flex;justify-content:center;padding:30px;}");
        html.append(".receipt{background:#fff;width:300px;padding:20px;border-radius:4px;");
        html.append("box-shadow:0 2px 8px rgba(0,0,0,.15);font-size:13px;line-height:1.6;}");
        html.append("pre{margin:0;white-space:pre-wrap;word-break:break-word;}");
        html.append("</style></head><body><div class='receipt'><pre>");
        html.append(receiptText.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"));
        html.append("</pre></div></body></html>");

        try (FileWriter fw = new FileWriter(htmlFile)) {
            fw.write(html.toString());
            JOptionPane.showMessageDialog(parent,
                    "Receipt saved as HTML (opens in browser):\n" + htmlFile.getName()
                            + "\n\nTip: Use browser's Print → Save as PDF for a true PDF.",
                    "Receipt Saved", JOptionPane.INFORMATION_MESSAGE);
            // Try to open in default browser
            try { Desktop.getDesktop().open(htmlFile); } catch (Exception ignored) { }
        } catch (IOException ioEx) {
            JOptionPane.showMessageDialog(parent,
                    "Could not save receipt: " + ioEx.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
