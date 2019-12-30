package yx.rbac.easy.configuration.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "annotion_id")
    private String annotionId;

    @Column(name = "annotion_note")
    private String annotionNote;

    public Permission(){}
    public Permission(String annotionId,String annotionNote){
        this.annotionId = annotionId;
        this.annotionNote = annotionNote;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnnotionId() {
        return annotionId;
    }

    public void setAnnotionId(String annotionId) {
        this.annotionId = annotionId;
    }

    public String getAnnotionNote() {
        return annotionNote;
    }

    public void setAnnotionNote(String annotionNote) {
        this.annotionNote = annotionNote;
    }
}
