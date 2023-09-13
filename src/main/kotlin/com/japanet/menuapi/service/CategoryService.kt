package com.japanet.menuapi.service

import com.japanet.menuapi.repository.CategoryRepository
import mu.KLogger
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val repository: CategoryRepository,
    private val log: KLogger = KotlinLogging.logger {}
) {
}
