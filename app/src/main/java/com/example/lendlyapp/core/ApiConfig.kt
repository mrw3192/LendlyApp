package com.example.lendlyapp.core

/**
 * Global API configurations for Retrofit.
 */
object ApiConfig {
    /** 
     * Postman Mock Server Base URL. 
     * IMPORTANT: Must always end with a trailing slash.
     */
    lateinit var BASE_URL: String
    
    /** Static API Key for the Mock Server */
    lateinit var API_KEY: String
}
