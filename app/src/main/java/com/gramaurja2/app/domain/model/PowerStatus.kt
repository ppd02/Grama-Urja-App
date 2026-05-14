package com.gramaurja2.app.domain.model

enum class PowerStatus {
    ON,
    OFF,
    UNKNOWN;

    val isOnline: Boolean get() = this == ON
}
