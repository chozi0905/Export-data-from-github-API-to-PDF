package com.project.kerja.listuser.controllers;

import java.awt.Color;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Controller {

    @RequestMapping("/api")
    public String hello(){
        return "Proggram ini bertujuan untuk bisa export file berupa pdf dari data api github";

    }

    @GetMapping("/exportPDF")
    public void exportToPdf(HttpServletResponse respone) throws DocumentException, IOException{

        //untuk fetch ke github api
        String url = "https://api.github.com/users";
        RestTemplate restTemplate = new RestTemplate();

        //dijadikan list berupa object
        Object[] users = restTemplate.getForObject(url, Object[].class);
        List<Object> user = Arrays.asList(users);

        //convert to pdf
        setReponseHeade(respone, "application/pdf", ".pdf", "users_");
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, respone.getOutputStream());

        document.open();

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setSize(18);
        font.setColor(Color.BLACK);
        Paragraph para = new Paragraph("List Users", font);
        para.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(para);

        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);

        writeUserHeader(table);
        writeUserData(table, user);

        document.add(table);
        document.close();
    }

    //merancang header
    public void setReponseHeade(HttpServletResponse response, String contentType, String extension, String prefix) throws IOException{
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timeStamp = dateFormat.format(new Date());
        String fileName = prefix + timeStamp + extension;

        response.setContentType(contentType);

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
    }

    //pembuatan header
    private void writeUserHeader(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(Color.black);

        cell.setPhrase(new Phrase("Username", font));
        table.addCell(cell);
    }

    //memasukkan data kedalam kolom
    private void writeUserData(PdfPTable table, List<Object> user){
        for(int i = 0; i < user.size(); i++){
            Map<String, Object> mapUser = (Map<String, Object>) user.get(i);
            String nama = (String) mapUser.get("login");
            table.addCell(nama);
        }
    }

}
