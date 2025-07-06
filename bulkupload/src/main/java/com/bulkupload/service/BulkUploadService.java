package com.bulkupload.service;

import com.bulkupload.model.Student;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class BulkUploadService {

    private final ProducerService service;

    public void bulkUpload(MultipartFile file) throws IOException {

       InputStream is = file.getInputStream();
       XSSFWorkbook workbook = new XSSFWorkbook(is);
        XSSFSheet sheet = workbook.getSheetAt(0);
        for(Row row: sheet)
        {
            if(row.getRowNum()==0)continue;
            Student student = new Student();
            student.setStd_id(((int) row.getCell(0).getNumericCellValue()));
            student.setName(row.getCell(1).getStringCellValue());
            student.setEmail(row.getCell(2).getStringCellValue());
            student.setLocation(row.getCell(3).getStringCellValue());
            service.sendStudent(student);
        }


    }
}
