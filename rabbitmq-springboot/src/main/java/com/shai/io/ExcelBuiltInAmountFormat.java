package com.shai.io;

import com.sun.rowset.internal.Row;

import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelBuiltInAmountFormat {
        public static void main(String[] args) {
            // 1. 创建工作簿和工作表
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("内置纯金额格式示例");

            // 2. 创建纯金额格式CellStyle（使用Excel内置编码11：#,##0.00）
            CellStyle amountCellStyle = workbook.createCellStyle();
            // 关键：设置内置金额格式编码，避免文本化
            amountCellStyle.setDataFormat((short) 11); // 带千分位、保留2位小数的纯金额格式

            // 3. 批量创建单元格并应用样式（复用CellStyle，优化资源）
            for (int i = 0; i < 5; i++) {
                Row row = sheet.createRow(i);
                Cell cell = row.createCell(0);
                // 核心前提：赋值为纯数字类型（double/BigDecimal，避免字符串）
                double amount = 12345.678 + i * 1000;
                cell.setCellValue(amount);
                cell.setCellStyle(amountCellStyle); // 复用同一个样式
            }

            // 4. 调整列宽，优化显示
            sheet.autoSizeColumn(0);

            // 5. 写入文件并关闭资源
            try (FileOutputStream fos = new FileOutputStream("builtin_amount_example.xlsx")) {
                workbook.write(fos);
                System.out.println("内置纯金额格式Excel创建成功，打开即生效，无需手动编辑");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }
}
