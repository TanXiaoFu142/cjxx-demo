package com.xf.config;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import java.util.List;

/**
 * @ClassName MergeStrategy
 * @Description TODO
 * @Author tanjunjie
 * @Date 2023/10/16 17:53
 * @Version 1.0
 */
public class MergeStrategy extends AbstractMergeStrategy {
    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {
        // 设置样式
        CellStyle cellStyle = cell.getCellStyle();
        //水平居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //自动换行
        cellStyle.setWrapText(true);

        if (relativeRowIndex == null || relativeRowIndex ==0) {
            return;
        }
        int rowIndex = cell.getRowIndex();
        int collIndex = cell.getColumnIndex();
        sheet = cell.getSheet();

        Row preRow = sheet.getRow(rowIndex - 1);
        //获取上一行单元格
        Cell preCell = preRow.getCell(collIndex);

        List<CellRangeAddress> list = sheet.getMergedRegions();

        CellStyle cs = cell.getCellStyle();
        cell.setCellStyle(cs);


        for (int i = 0; i < list.size(); i++) {
            CellRangeAddress cellRangeAddress = list.get(i);
            if (cellRangeAddress.containsRow(preCell.getRowIndex()) && cellRangeAddress.containsColumn(preCell.getColumnIndex())) {
                int lastColumn = cellRangeAddress.getLastColumn();
                int firstColumn = cellRangeAddress.getFirstColumn();
                CellRangeAddress cra = new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex(), firstColumn, lastColumn);
                sheet.addMergedRegion(cra);
                RegionUtil.setBorderBottom(BorderStyle.THIN,cra,sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN,cra,sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN,cra,sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN,cra,sheet);
            }

        }
    }
}
