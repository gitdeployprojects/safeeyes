package com.eyezen.data.model

import java.time.LocalDate

/**
 * Subscription plan.
 */
data class SubscriptionPlan(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val currency: String = "USD",
    val billingPeriod: String, // "monthly", "yearly"
    val features: List<String>,
    val trialDays: Int = 0
)

/**
 * User subscription status.
 */
data class UserSubscription(
    val userId: String,
    val planId: String,
    val planName: String,
    val status: String, // "active", "canceled", "expired", "trial"
    val startDate: LocalDate,
    val expiryDate: LocalDate,
    val autoRenew: Boolean,
    val price: Double,
    val currency: String
)

/**
 * Premium features
 */
object PremiumFeatures {
    const val UNLIMITED_REPORTS = "unlimited_reports"
    const val CLOUD_SYNC = "cloud_sync"
    const val CUSTOM_GOALS = "custom_goals"
    const val ADVANCED_ANALYTICS = "advanced_analytics"
    const val AD_FREE = "ad_free"
    const val EXPORT_DATA = "export_data"
    const val API_ACCESS = "api_access"
    const val PRIORITY_SUPPORT = "priority_support"

    val basicPlan = SubscriptionPlan(
        id = "plan_basic",
        name = "Basic",
        description = "Essential eye care tracking",
        price = 0.0,
        billingPeriod = "free",
        features = listOf(
            "Daily break reminders",
            "Water intake tracking",
            "Basic exercises",
            "Eye health score"
        )
    )

    val proPlan = SubscriptionPlan(
        id = "plan_pro",
        name = "Pro",
        description = "Advanced analytics and cloud sync",
        price = 4.99,
        billingPeriod = "monthly",
        features = listOf(
            "Everything in Basic",
            "Cloud sync across devices",
            "Unlimited reports",
            "Advanced analytics",
            "Custom goals",
            "Ad-free experience",
            "Export reports (PDF/CSV)"
        ),
        trialDays = 7
    )

    val premiumPlan = SubscriptionPlan(
        id = "plan_premium",
        name = "Premium",
        description = "Complete wellness platform",
        price = 9.99,
        billingPeriod = "monthly",
        features = listOf(
            "Everything in Pro",
            "API access",
            "Priority support",
            "Custom notifications",
            "Team collaboration",
            "Offline mode"
        ),
        trialDays = 14
    )

    val allPlans = listOf(basicPlan, proPlan, premiumPlan)
}
