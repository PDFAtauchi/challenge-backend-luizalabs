package com.atauchi.transformerFileService

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<TransformerFileServiceApplication>().with(TestcontainersConfiguration::class).run(*args)
}
