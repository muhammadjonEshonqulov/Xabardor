package uz.xabardor.rest.services

import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.xabardor.BuildConfig
import uz.xabardor.app.App
import uz.xabardor.extensions.isNotOnline
import uz.xabardor.rest.Api
import java.math.BigInteger
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

open class BaseService {


    val api: Api

    init {
//        val logging = HttpLoggingInterceptor();
//        logging.level = HttpLoggingInterceptor.Level.BODY

        var builder = OkHttpClient().newBuilder()
            .cache(null)
//            .addInterceptor(logging)
            .addInterceptor(getTokenInterceptor())
            .connectionSpecs(listOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT))
            .connectTimeout(50000L, TimeUnit.MILLISECONDS)
            .readTimeout(50000L, TimeUnit.MILLISECONDS)
            .writeTimeout(50000L, TimeUnit.MILLISECONDS)
//        if (BuildConfig.isDebug) {
//            builder.addInterceptor(ChuckerInterceptor.Builder(App.context).collector(
//                ChuckerCollector(App.context)
//            ).build())
//        }
        var client = builder.build()

        var retrofit = Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        api = retrofit.create(Api::class.java);
    }

    fun getTokenInterceptor(): Interceptor {
        return Interceptor { chain ->
            val requestBuilder = chain
                .request()
                .newBuilder()
            addHeaders(requestBuilder)

            val request = requestBuilder.build()
            var response = chain.proceed(request)

            response
        }
    }


    companion object {
        val BASE_API_URL = "https://xabardor.uz/api/"
        private val KEY = "3c2319528068e709796bf8259c364137dc9dac34"

        private fun addHeaders(requestBuilder: Request.Builder) {
            val time = System.currentTimeMillis()


            requestBuilder.addHeader("time", "$time")
            requestBuilder.addHeader("Authorization", "Token $KEY")
        }

        public fun getHeaders(): Map<String, String> {

            val time = System.currentTimeMillis()

            val headers = HashMap<String, String>()

            headers.put("Authorization", "Token $KEY")

            return headers
        }

        private fun getMd5(input: String): String {
            try {

                // Static getInstance method is called with hashing MD5
                val md = MessageDigest.getInstance("MD5")

                // digest() method is called to calculate message digest
                //  of an input digest() return array of byte
                val messageDigest = md.digest(input.toByteArray())

                // Convert byte array into signum representation
                val no = BigInteger(1, messageDigest)

                // Convert message digest into hex value
                var hashtext = no.toString(16)
                while (hashtext.length < 32) {
                    hashtext = "0$hashtext"
                }
                return hashtext
            } catch (e: Exception) {
//            throw RuntimeException(e)
            }


            return ""
        }

        var noConnectionListener: NoConnectionListener? = null
    }

}

interface NoConnectionListener {
    fun onNoConnection()
}