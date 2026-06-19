package shared;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import shared.Model.Bill;
import shared.Model.BillInfor;

public class PrintInvoice {

    private static final String INVOICE_FOLDER =
            "C:\\Users\\admin\\OneDrive\\Desktop\\QuanLyCafe-ManagerGui\\Invoice";

    
    public static void saveInvoice(
            Bill bill,
            List<BillInfor> details) {

        try {

            File folder = new File(INVOICE_FOLDER);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            String fileName =
                    "Invoice_" + bill.getId() + ".txt";

            File file =
                    new File(folder, fileName);

            StringBuilder sb = new StringBuilder();

            sb.append("================================\n");
            sb.append("          HOA DON\n");
            sb.append("================================\n\n");

            sb.append("Ma hoa don: ")
              .append(bill.getId())
              .append("\n\n");

            for (BillInfor item : details) {

                BigDecimal lineTotal =
                        item.getPrice().multiply(
                                BigDecimal.valueOf(
                                        item.getQuantity()));

                sb.append(item.getFoodName())
                  .append(" x")
                  .append(item.getQuantity())
                  .append(" = ")
                  .append(lineTotal)
                  .append(" VNĐ\n");
            }

            sb.append("\n--------------------------------\n");

            sb.append("Tong tien: ")
              .append(bill.getTotalPrice())
              .append(" VNĐ\n");

            sb.append("================================\n");
            sb.append("Cam on quy khach!\n");

            FileWriter writer =
                    new FileWriter(file);

            writer.write(sb.toString());
            writer.close();

            System.out.println(
                    "Da luu hoa don: "
                    + file.getAbsolutePath());

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    public static void saveInvoice2(
            Bill bill,
            List<BillInfor> details,
            String tableInfo   // 👈 THÊM THÔNG TIN BÀN
    ) {

        try {
            File folder = new File(INVOICE_FOLDER);
            if (!folder.exists()) folder.mkdirs();

            String fileName = "Invoice_" + bill.getId() + ".txt";
            File file = new File(folder, fileName);

            StringBuilder sb = new StringBuilder();

            sb.append("================================\n");
            sb.append("          HOA DON\n");
            sb.append("================================\n\n");

            sb.append("Ma hoa don: ")
              .append(bill.getId())
              .append("\n");

            sb.append("ID Ban: ")
              .append(tableInfo)
              .append("\n\n");   // 👈 THÊM BÀN Ở ĐÂY

            for (BillInfor item : details) {

                BigDecimal lineTotal =
                        item.getPrice().multiply(
                                BigDecimal.valueOf(item.getQuantity()));

                sb.append(item.getFoodName())
                  .append(" x")
                  .append(item.getQuantity())
                  .append(" = ")
                  .append(lineTotal)
                  .append(" VNĐ\n");
            }

            sb.append("\n--------------------------------\n");

            sb.append("Tong tien: ")
              .append(bill.getTotalPrice())
              .append(" VNĐ\n");

            sb.append("================================\n");
            sb.append("Cam on quy khach!\n");

            FileWriter writer = new FileWriter(file);
            writer.write(sb.toString());
            writer.close();

            System.out.println("Da luu hoa don: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}