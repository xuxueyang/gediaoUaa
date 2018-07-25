package uaa.web.rest.util.excel;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uaa.domain.UaaError;
import uaa.web.rest.util.excel.basel.*;
import util.UUIDGenerator;
import util.Validators;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);
    private static final int BUFFER_INITIALLY = 4000;



    private static UaaError excelHelper(HttpServletResponse response, JFile jFile){
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new BufferedInputStream(new ByteArrayInputStream(jFile.getFileContent()));
            byte[] buffer = new byte[BUFFER_INITIALLY];//一次读取4000个字节
            response.reset();//清除首部的空白行
            response.addHeader("Content-Length", Integer.toString(jFile.getFileContent().length));
            os = new BufferedOutputStream(response.getOutputStream());
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(jFile.getExpectedFullFileName(), "UTF-8"));
            response.setContentType("application/vnd.ms-excel");
            while(is.read(buffer)>0){
                os.write(buffer);
                os.flush();
            }
        } catch (Exception e) {
            return UaaError.failure(e.getMessage());
        }finally{
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
        return UaaError.success();
    }



    /**
     *
     * @param response 返回給前端的輸出流
     * @param list 數據列表
     * @param fileNameNoExtension 文件名
     * @param extension 文件後綴，默認xls
     */
    public static UaaError writeExcel(HttpServletResponse response,List<? extends ColumnWalker> list,
                                  String fileNameNoExtension){
        if(Validators.fieldBlank(fileNameNoExtension)){
            fileNameNoExtension = UUIDGenerator.getUUID();
        }
        ExcelModel excelModel = readRecord(list);
        byte[] content = excel2003(excelModel);
        JFile jFile = new JFile();
        jFile.setFileContent(content);
        jFile.setExpectedFullFileName(fileNameNoExtension+".xls");
        jFile.setFileNameNoExtension(fileNameNoExtension);
        jFile.setFileExtension(".xls");
        return excelHelper(response,jFile);
    }

    private static ExcelModel readRecord(List<? extends ColumnWalker> list) {
        ExcelModel excelModel = new ExcelModel();
        if (list != null && !list.isEmpty()) {
            Class<?> clazz = ((ColumnWalker)list.get(0)).getClass();
            ExcelHead excelHead = (ExcelHead)clazz.getAnnotation(ExcelHead.class);
            Field[] fields = clazz.getDeclaredFields();
            ExcelColumn excelColumn = null;
            List<String> columns = new ArrayList();
            Field[] var7 = fields;
            int var8 = fields.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                Field field = var7[var9];
                excelColumn = (ExcelColumn)field.getAnnotation(ExcelColumn.class);
                if (excelColumn != null) {
                    columns.add(excelColumn.value());
                }
            }

            if (excelHead != null) {
                excelModel.setTitle(excelHead.value());
            }

            excelModel.setColumns(columns);
            excelModel.setRecordColumns(list);
            return excelModel;
        } else {
            throw new IllegalArgumentException("arguments list can not be null or empty");
        }
    }

    private static byte[] excel2003(ExcelModel excelModel) {
        byte[] bytes = null;
        Workbook wb = null;
        ByteArrayOutputStream fileOut = null;
        int currentRowNumber = 0;

        try {
            wb = new HSSFWorkbook();
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setAlignment((short)2);
            Font font = wb.createFont();
            font.setBoldweight((short)700);
            font.setFontHeightInPoints((short)16);
            CreationHelper createHelper = wb.getCreationHelper();
            Sheet sheet = wb.createSheet(excelModel.getSheetName());
            String title = excelModel.getTitle();
            String detail = excelModel.getDetail();
            List<String> columns = excelModel.getColumns();
            int columnSize = columns.size();
            Row rowColumns;
            Cell cell;
            if (StringUtils.isNotBlank(title)) {
                rowColumns = sheet.createRow(currentRowNumber);
                cell = rowColumns.createCell(0);
                cell.setCellValue(createHelper.createRichTextString(excelModel.getTitle()));
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnSize - 1));
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                ++currentRowNumber;
            }

            if (StringUtils.isNoneBlank(new CharSequence[]{detail})) {
                rowColumns = sheet.createRow(currentRowNumber);
                cell = rowColumns.createCell(0);
                cell.setCellValue(createHelper.createRichTextString(excelModel.getDetail()));
                sheet.addMergedRegion(new CellRangeAddress(currentRowNumber, currentRowNumber, 0, columnSize - 1));
                cell.setCellStyle(cellStyle);
                ++currentRowNumber;
            }

            if (!columns.isEmpty()) {
                rowColumns = sheet.createRow(currentRowNumber);
                cell = null;

                for(int i = 0; i < columnSize; ++i) {
                    cell = rowColumns.createCell(i);
                    cell.setCellValue(createHelper.createRichTextString((String)columns.get(i)));
                }

                ++currentRowNumber;
            }

            List<ColumnWalker> records = new ArrayList<ColumnWalker>();
            List<? extends ColumnWalker> recordColumns = excelModel.getRecordColumns();
            if(recordColumns!=null&&recordColumns.size()>0){
                for(ColumnWalker tmp:recordColumns){
                    records.add(tmp);
                }
            }

            if (!columns.isEmpty()) {
                for(Iterator var25 = records.iterator(); var25.hasNext(); ++currentRowNumber) {
                    ColumnWalker columnWalker = (ColumnWalker)var25.next();
                    Row rowRecords = sheet.createRow(currentRowNumber);
//                    Cell cell = null;
                    for(int i = 0; i < columnSize; ++i) {
                        cell = rowRecords.createCell(i);
                        cell.setCellValue(columnWalker.next());
                    }
                }
            }

            fileOut = new ByteArrayOutputStream(4000);
            wb.write(fileOut);
            bytes = fileOut.toByteArray();
            fileOut.flush();
        } catch (IOException var22) {
            LOGGER.error(var22.getMessage(), var22);
        } finally {
            IOUtils.closeQuietly(fileOut);
            IOUtils.closeQuietly(wb);
        }

        return bytes;
    }
}
