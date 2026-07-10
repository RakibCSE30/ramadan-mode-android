package com.example.ramadan_mode

/**
 * একটা Iftar খাবারের আইটেম — নাম আর কেন এটা উপকারী তার সংক্ষিপ্ত বর্ণনা।
 */
data class IftarFoodItem(
    val name: String,
    val benefit: String
)

/**
 * ইফতারের জন্য সহজ, স্থির (fixed) খাবারের তালিকা।
 */
object NutritionData {

    val iftarSuggestions = listOf(
        IftarFoodItem(
            name = "Dates (খেজুর)",
            benefit = "দ্রুত এনার্জি ও প্রাকৃতিক সুগার দেয়, রোজার সুন্নত শুরু"
        ),
        IftarFoodItem(
            name = "Water (পানি)",
            benefit = "সারাদিনের পানিশূন্যতা পূরণ করে, ধীরে ধীরে পান করুন"
        ),
        IftarFoodItem(
            name = "Soup (সুপ)",
            benefit = "হালকা ও সহজপাচ্য, পেটকে ধীরে ধীরে খাবারের জন্য প্রস্তুত করে"
        ),
        IftarFoodItem(
            name = "Fruits (ফল)",
            benefit = "ভিটামিন, ফাইবার ও প্রাকৃতিক হাইড্রেশন সরবরাহ করে"
        ),
        IftarFoodItem(
            name = "Yogurt (দই)",
            benefit = "প্রোবায়োটিক ও প্রোটিন দেয়, হজমে সাহায্য করে"
        ),
        IftarFoodItem(
            name = "Avoid fried food (ভাজা খাবার এড়িয়ে চলুন)",
            benefit = "খালি পেটে বেশি তেল-মশলাযুক্ত খাবার হজমে সমস্যা করতে পারে"
        )
    )
}