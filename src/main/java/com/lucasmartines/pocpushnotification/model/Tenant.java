package com.lucasmartines.pocpushnotification.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tenant {
    private String name;
    private FirebaseCredential firebaseCredential;
}
