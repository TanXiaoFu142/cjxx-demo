package com.xf.controller;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.ImageData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Excel导出单元格中有图片，图片会进行压缩，缩略，插入单元格
 * 注意：
 *      - 该策略是复制原表格中的图片进行缩放，原图片并没有删除掉，而是将尺寸设置为 0 看不到而已，但是依旧占用空间
 *      - 且在多 sheet 的环境下，除第一个 sheet，其余的 sheet 图片会被置 0
 * 目前上述问题并没有得到解决，如果在导出数据较多或者存在多个 sheet 的情况下不建议使用
 */
public class CustomImageModifyStrategy implements CellWriteHandler {

    /**
     * 已经处理的Cell
     */
    private final CopyOnWriteArrayList<String> REPEATS = new CopyOnWriteArrayList<>();

    /**
     * 单元格的图片最大张数（每列的单元格图片张数不确定，单元格宽度需按照张数最多的长度来设置）
     */
    private final AtomicReference<Integer> MAX_IMAGE_SIZE = new AtomicReference<>(0);

    /**
     * 标记手动添加的图片，用于排除EasyExcel自动添加的图片
     */
    private final CopyOnWriteArrayList<Integer> CREATE_PIC_INDEX = new CopyOnWriteArrayList<>();

    /**
     * 默认图片宽度（单位像素）：60
     */
    private final static int DEFAULT_IMAGE_WIDTH = 60;

    /**
     * 默认像素转换因子：32
     */
    private final static int DEFAULT_PIXEL_CONVERSION_FACTOR = 32;

    /**
     * 图片宽度，单位像素
     */
    private final int imageWidth;

    /**
     * 像素转换因子
     */
    private final int pixelConversionFactor;

    public CustomImageModifyStrategy() {
        this.imageWidth = DEFAULT_IMAGE_WIDTH;
        this.pixelConversionFactor = DEFAULT_PIXEL_CONVERSION_FACTOR;
    }

    public CustomImageModifyStrategy(int imageWidth, int pixelConversionFactor) {
        this.imageWidth = imageWidth;
        this.pixelConversionFactor = pixelConversionFactor;
    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, WriteCellData<?> cellData, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        //  在数据转换成功后 不是头就把类型设置成空
        if (isHead) {
            return;
        }
        //将要插入图片的单元格的type设置为空,下面再填充图片
        if (!CollectionUtils.isEmpty(cellData.getImageDataList())) {
            cellData.setType(CellDataTypeEnum.EMPTY);
        }
    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        //  在 单元格写入完毕后 ，自己填充图片
        if (isHead || CollectionUtils.isEmpty(cellDataList)) {
            return;
        }
        boolean listFlag = false;
        Sheet sheet = cell.getSheet();
        List<ImageData> imageDataList = cellDataList.get(0).getImageDataList();
        if (!CollectionUtils.isEmpty(imageDataList)) {
            listFlag = true;
        }
        if (!listFlag && imageDataList == null) {
            return;
        }
        String key = cell.getRowIndex() + "_" + cell.getColumnIndex();
        if (REPEATS.contains(key)) {
            return;
        }
        REPEATS.add(key);
        if (imageDataList.size() > MAX_IMAGE_SIZE.get()) {
            MAX_IMAGE_SIZE.set(imageDataList.size());
        }

        int widthValue =  imageWidth * pixelConversionFactor;
        sheet.setColumnWidth(cell.getColumnIndex(), listFlag ? widthValue * MAX_IMAGE_SIZE.get() + pixelConversionFactor : widthValue);

        if (listFlag) {
            for (int i = 0; i < imageDataList.size(); i++) {
                ImageData imageData = imageDataList.get(i);
                if (imageData == null) {
                    continue;
                }
                byte[] image = imageData.getImage();
                int index = this.insertImage(sheet, cell, image, i);
                CREATE_PIC_INDEX.add(index);
            }
        } else {
            this.insertImage(sheet, cell, imageDataList.get(0).getImage(), 0);
        }

        // 清除EasyExcel自动添加的没有格式的图片
        XSSFDrawing drawingPatriarch = (XSSFDrawing) sheet.getDrawingPatriarch();
        List<XSSFShape> shapes = drawingPatriarch.getShapes();
        for (int i = 0; i < shapes.size(); i++) {
            XSSFShape shape = shapes.get(i);
            if (shape instanceof XSSFPicture && !CREATE_PIC_INDEX.contains(i)) {
                CREATE_PIC_INDEX.add(i);
                XSSFPicture picture = (XSSFPicture) shape;
                // 这里只是将图片的大小设置为 0，所以表格依旧会存放该图片
                picture.resize(0);
            }
        }
    }

    /**
     * 重新插入一个图片
     *
     * @param sheet       Excel页面
     * @param cell        表格元素
     * @param pictureData 图片数据
     * @param i           图片顺序
     */
    public int insertImage(Sheet sheet, Cell cell, byte[] pictureData, int i) {
        int picWidth = Units.pixelToEMU(imageWidth);
        int index = sheet.getWorkbook().addPicture(pictureData, HSSFWorkbook.PICTURE_TYPE_PNG);
        Drawing<?> drawing = sheet.getDrawingPatriarch();
        if (drawing == null) {
            drawing = sheet.createDrawingPatriarch();
        }
        CreationHelper helper = sheet.getWorkbook().getCreationHelper();
        ClientAnchor anchor = helper.createClientAnchor();
        /*
         * 设置图片坐标
         * 为了不让图片遮挡单元格的上边框和右边框，故 x1、x2、y1 这几个坐标点均向后移动了一个像素点
         */
        anchor.setDx1(Units.pixelToEMU(1) + picWidth * i);
        anchor.setDx2(Units.pixelToEMU(1) + picWidth + picWidth * i);
        anchor.setDy1(Units.pixelToEMU(1));
        anchor.setDy2(0);
        //设置图片位置
        int columnIndex = cell.getColumnIndex();
        anchor.setCol1(columnIndex);
        anchor.setCol2(columnIndex);
        int rowIndex = cell.getRowIndex();
        anchor.setRow1(rowIndex);
        anchor.setRow2(rowIndex + 1);
        anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
        drawing.createPicture(anchor, index);
        return index;
    }

}
