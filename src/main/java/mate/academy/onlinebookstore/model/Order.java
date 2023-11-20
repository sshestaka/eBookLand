package mate.academy.onlinebookstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsExclude;
import org.apache.commons.lang3.builder.HashCodeExclude;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE orders SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.JOIN)
    @ToString.Exclude
    @EqualsExclude
    @HashCodeExclude
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false, name = "order_date")
    private LocalDateTime orderDate;

    @Column(nullable = false, name = "shipping_address")
    private String shippingAddress;

    @OneToMany(mappedBy = "order")
    @Fetch(value = FetchMode.JOIN)
    @JsonIgnore
    @ToString.Exclude
    @EqualsExclude
    @HashCodeExclude
    private Set<OrderItem> orderItems;

    @Column(nullable = false)
    private boolean isDeleted = false;

    public enum Status {
        PENDING,
        AWAITING_PAYMENT,
        CONFIRMED,
        SHIPPING,
        DELIVERED,
        COMPLETED,
        CANCELED,
        DECLINED,
        REFUNDED
    }
}
