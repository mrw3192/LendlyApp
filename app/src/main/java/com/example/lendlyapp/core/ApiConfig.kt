package com.example.lendlyapp.core

<<<<<<< HEAD
object ApiConfig {
    const val BASE_URL       = "https://6d710e79-f4ca-4651-909f-7dd13bd29968.mock.pstmn.io/"
    const val API_KEY        = "123456789"
    const val API_KEY_HEADER = "x-api-key"
=======
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
>>>>>>> 8969aaf8bf2a7cdf0fc3eaa0b918595e1de561b0
}
