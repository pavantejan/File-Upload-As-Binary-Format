package com.cts.uploadDoc.repository;

import com.cts.uploadDoc.model.Document;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface DocumentRepo extends JpaRepository<Document, Long> {
//
    @Query("SELECT u FROM Document u WHERE u.name = :name")
    Document findByName(String name);

    @Modifying
    @Query(value="DELETE FROM Document WHERE name = :name")
    void deleteDoc(String name);
}
