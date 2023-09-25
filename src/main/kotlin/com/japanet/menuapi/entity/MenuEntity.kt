package com.japanet.menuapi.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.*
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

    @Column(name = "idt_customer",  columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    var customerId: UUID?,

    @OneToMany(mappedBy = "menu", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var items: MutableList<ItemEntity>?,

    @OneToMany(mappedBy = "menu", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var categories: MutableList<CategoryEntity>?,

    @OneToMany(mappedBy = "menu", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var additionalItems: MutableList<AdditionalItemEntity>? = null,

    @UpdateTimestamp
    @Column(name = "dat_update", nullable = true, updatable = true)
    var datUpdate: LocalDateTime? = null,

    @CreationTimestamp
    @Column(name = "dat_creation", nullable = false, updatable = false)
    var datCreation: LocalDateTime? = LocalDateTime.now()
) {
    @Override
    override fun toString(): String {
        return "MenuEntity(id=${this.id}, " +
                "establishmentId=${this.establishmentId}, " +
                "customerId=${this.customerId}, " +
                "datUpdate=${this.datUpdate}, " +
                "datCreation=${this.datCreation})"
    }
}
