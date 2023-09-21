package com.japanet.menuapi.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Digits

@Entity
@Table(name = "item")
class ItemEntity(

    @Id
    @Column(name = "idt_item")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idt_menu")
    var menu: MenuEntity,

    @OneToOne(cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY)
    @JoinColumn(name = "idt_category", nullable = false, updatable = true)
    var category: CategoryEntity?,

    @Column(name = "des_name", nullable = false, updatable = true)
    var name: String?,

    @Column(name = "des_description", nullable = true, updatable = true)
    var description: String?,

    @Digits(integer = 9, fraction = 2)
    @Column(name = "des_price", precision = 9, scale = 2, nullable = false, updatable = true)
    var price: BigDecimal?,

    @UpdateTimestamp
    @Column(name = "dat_update", nullable = true, updatable = true)
    var datUpdate: LocalDateTime? = null,

    @CreationTimestamp
    @Column(name = "dat_creation", nullable = false, updatable = false)
    var datCreation: LocalDateTime? = LocalDateTime.now()

)
