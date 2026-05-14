package com.melikalihocagil.saglikcell.presentetion.navigation

import kotlinx.serialization.Serializable

/**
 * Uygulama içerisindeki ekran rotalarını temsil eden sealed class.
 * Type-safe navigation için Kotlin Serialization kullanılır.
 */
@Serializable
sealed class Screen {
    
    @Serializable
    data object Dashboard : Screen()

    @Serializable
    data object Login : Screen()

    @Serializable
    data object Profile : Screen()

    @Serializable
    data object MetricEntry : Screen()

    @Serializable
    data object Analytics : Screen()

    @Serializable
    data object Premium : Screen()
}
