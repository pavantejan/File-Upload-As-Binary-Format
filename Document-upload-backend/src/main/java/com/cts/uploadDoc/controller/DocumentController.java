package com.cts.uploadDoc.controller;

import com.cts.uploadDoc.model.Document;
import com.cts.uploadDoc.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping(value = "/")
    public String welcome(){
        return "application working";
    }

    @PostMapping(value = "/save-pdf")
    public ResponseEntity<String> savePdfDocument(@RequestParam("file") MultipartFile file){
        byte[] content;
        try{
            content = file.getBytes();
            if(content.length > 16 * 1024 *1024 ) {
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File size exceeded, max allowed limit is 16MB");
            }
        }catch(IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed: " + e.getMessage());
        }

        try{
            documentService.saveDocument(file.getOriginalFilename(),content);
            return ResponseEntity.ok("File uploaded successfully!");
        }catch (DataIntegrityViolationException e) {
            // Condition for duplicatee entry of doc
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate entry for name: " + file.getOriginalFilename());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving document: " + e.getMessage());
        }
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<?> getAllDoc(){
        List<Document> allDocs = documentService.getAllDocuments();
        if(allDocs != null){
            List<String> names = new ArrayList<>();
            for(Document doc:allDocs){
                names.add(doc.getName());
            }
            System.out.println(names);
            return ResponseEntity.ok(names);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No documents found");
    }

    @GetMapping(value = "/download/{name}")
    public ResponseEntity<?> downloadByName(@PathVariable("name") String name){
        System.out.println("inside the download method");
        Document doc = documentService.downloadByName(name);
        if( doc != null){
            ByteArrayResource resource = new ByteArrayResource(doc.getContent());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + doc.getName()
                                + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(doc.getContent().length)
                    .body(resource);

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No documents found");
    }


    @DeleteMapping(value = "/remove/{name}")
    public ResponseEntity<String> deleteDocument(@PathVariable("name") String name){
        try{
            documentService.deleteDocument(name);
            return ResponseEntity.ok("Document deleted successfully");
        }catch(Exception e){
            return ResponseEntity.status(500).body("Error deleting document" + e.getMessage());
        }
    }



}
