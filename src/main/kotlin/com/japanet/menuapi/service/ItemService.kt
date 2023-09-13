package com.japanet.menuapi.service

import com.japanet.menuapi.repository.ItemRepository
import mu.KLogger
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class ItemService(
    private val repository: ItemRepository,
    private val log: KLogger = KotlinLogging.logger {}
) {

}
