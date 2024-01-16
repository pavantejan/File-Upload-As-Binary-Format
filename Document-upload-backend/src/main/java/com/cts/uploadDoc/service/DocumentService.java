package com.cts.uploadDoc.service;

import com.cts.uploadDoc.model.Document;
import com.cts.uploadDoc.repository.DocumentRepo;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepo documentRepo;

    @CacheEvict(value = "documents", allEntries = true)
    public void saveDocument(String name,byte[] content){
        Document doc = new Document();
        doc.setName(name);
        doc.setContent(content);
        System.out.println(doc);
        System.out.println(doc.getContent().length);
        documentRepo.save(doc);
//        documentRepo.flush();
    }

    public List<Document> getAllDocuments(){
        return documentRepo.findAll();
    }

    public Document downloadByName(String name){
        return documentRepo.findByName(name);
//        return null;
    }

    public void deleteDocument(String name){
        documentRepo.deleteDoc(name);
    }
}
