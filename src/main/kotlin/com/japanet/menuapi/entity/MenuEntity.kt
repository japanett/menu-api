package com.japanet.menuapi.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "menu")
class MenuEntity(

    @Id
    @Column(name = "idt_menu")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0,

    @Column(name = "idt_establishment", nullable = false, updatable = false)
    var establishmentId: Long?,

    @Column(name = "idt_customer", nullable = false, updatable = false)
    var customerId: Long?,

    @OneToMany(mappedBy = "menu", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var items: MutableList<ItemEntity>?,

    @OneToMany(mappedBy = "menu", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var categories: MutableList<CategoryEntity>?,

    /*

      LISTA DE PROMOTIONS

     */

    @UpdateTimestamp
    @Column(name = "dat_update", nullable = true, updatable = true)
    var datUpdate: LocalDateTime? = null,

    @CreationTimestamp
    @Column(name = "dat_creation", nullable = false, updatable = false)
    var datCreation: LocalDateTime? = LocalDateTime.now()
)
