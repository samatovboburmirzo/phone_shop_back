package phone.shop.entity;

import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import phone.shop.types.OrderStatus;
import phone.shop.types.PaymentType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "order")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private ProfileEntity profile;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Column(name = "requirement")
    private String requirement;
    @Column
    private String address;
    @Column
    private String contact;
    @Column
    private Double deliveryCost;
    @Column(name = "payment_type")
    private PaymentType paymentType;
    @Enumerated(EnumType.STRING)
    @Column
    private OrderStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private SupplierEntity supplier;



}
