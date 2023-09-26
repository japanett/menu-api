package com.japanet.menuapi.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.*
import javax.persistence.CascadeType.*
import javax.persistence.FetchType.LAZY
import javax.persistence.GenerationType.IDENTITY
import javax.validation.constraints.Digits

@Entity
@Table(name = "item")
class ItemEntity(

    @Id
    @Column(name = "idt_item")
    @GeneratedValue(strategy = IDENTITY)
    var id: Long? = 0,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "idt_menu")
    var menu: MenuEntity,

    @ManyToMany(fetch = LAZY, cascade = [PERSIST, MERGE, REFRESH])
    @JoinTable(name = "item_additional_item", joinColumns = [
        JoinColumn(name = "idt_item", referencedColumnName = "idt_item", nullable = false, updatable = false)
    ], inverseJoinColumns = [
       JoinColumn(name = "idt_additional_item", referencedColumnName = "idt_additional_item", nullable = false, updatable = false)
    ])
    var additionalItems: MutableList<AdditionalItemEntity>? = null,

    @OneToOne(cascade = [PERSIST], fetch = LAZY)
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
