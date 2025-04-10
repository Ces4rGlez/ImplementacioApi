package com.utng_gds0651

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform