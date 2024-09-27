package michal.malek.diagnosticsapp.auth.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class RetrievePasswordOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uid;
    private String userUid;
    @Column(columnDefinition = "DATETIME")
    private Date date;


    public RetrievePasswordOperation(String userUid) {
        this.userUid = userUid;
        uid = UUID.randomUUID().toString();
    }

    public RetrievePasswordOperation() {}
}
