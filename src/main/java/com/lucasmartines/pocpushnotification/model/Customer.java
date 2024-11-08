package com.lucasmartines.pocpushnotification.model;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    private String id;
    private String name;
    private List<Tenant> tenants;
}
