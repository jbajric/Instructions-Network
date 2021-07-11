package com.instructionnetwork.systemevents.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "systemevents")
public class SystemEventsModel implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column (name = "time_stamp")
    private String timestamp;

    @Column (name = "microservice")
    private String microservice;

    @Column (name = "action_type")
    private String actionType;

    @Column (name = "resource_name")
    private String resourceName;

    @Column (name = "response_type")
    private String responseType;
}
