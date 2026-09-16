package org.example.alittlebetter

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform