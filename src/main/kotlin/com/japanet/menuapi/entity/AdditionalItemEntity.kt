package com.japanet.menuapi.entity

import com.fasterxml.jackson.annotation.JsonIgnore
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
@Table(name = "additional_item")
class AdditionalItemEntity(

    @Id
    @Column(name = "idt_additional_item")
    @GeneratedValue(strategy = IDENTITY)
    var id: Long? = 0,

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "idt_menu")
    var menu: MenuEntity,

    @JsonIgnore
    @ManyToMany(mappedBy = "additionalItems", fetch = LAZY, cascade = [PERSIST, MERGE, REFRESH])
    var items: MutableList<ItemEntity>? = null,

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
