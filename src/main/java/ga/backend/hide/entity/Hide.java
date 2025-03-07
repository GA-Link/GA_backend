package ga.backend.hide.entity;

import ga.backend.auditable.Auditable;
import ga.backend.company.entity.Company;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.employee.entity.Employee;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Cacheable
@Table(indexes = {@Index(name = "idx_hide_employee", columnList = "employee_pk")})
public class Hide extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="hide_pk")
    private Long pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_pk")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_type_pk")
    private CustomerType customerType;
}