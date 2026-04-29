package com.example.ecolush.data.remote

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseConfig {
    // Note: In a real project, use environment variables or a secure storage
    const val SUPABASE_URL = "https://iruaqvfiajwowdzjftpr.supabase.co"
    const val SUPABASE_KEY = "sb_publishable_aZPWugZJeFziWU5UK3dvSA__ujQsg_v"
}

val supabase = createSupabaseClient(
    supabaseUrl = SupabaseConfig.SUPABASE_URL,
    supabaseKey = SupabaseConfig.SUPABASE_KEY
) {
    install(Postgrest)
    install(Storage)
}
