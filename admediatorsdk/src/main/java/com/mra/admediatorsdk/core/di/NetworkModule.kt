package com.mra.admediatorsdk.core.di

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mra.admediatorsdk.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okio.Buffer
import org.koin.java.KoinJavaComponent
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.reflect.Type
import java.nio.charset.Charset
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException


/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */
fun createBaseNetworkClient(gson: Gson, baseUrl: String) = retrofitClient(gson, baseUrl)

private fun retrofitClient(gson: Gson, baseUrl: String): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(getOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

fun getOkHttpClient(): OkHttpClient {

    val mContext: Context by KoinJavaComponent.inject(Context::class.java)

    val okHttpClient = OkHttpClient.Builder()

    setTimeOutToOkHttpClient(okHttpClient)


    /**
     * Log response when app is debug mode
     */
    if (BuildConfig.DEBUG) {
        okHttpClient.addInterceptor {
            val oldRequest = it.request()
            val newRequestBuilder = oldRequest.newBuilder()

            newRequestBuilder.addHeader("Accept", "application/json")

            newRequestBuilder.method(oldRequest.method(), oldRequest.body())

            val response = it.proceed(newRequestBuilder.build())
            val source = response.body()!!.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.

            val buffer: Buffer = source.buffer()
            val responseBodyString: String = buffer.clone().readString(Charset.forName("UTF-8"))
            Log.e("Retrofit", "Response -> $responseBodyString")

            return@addInterceptor response
        }
    }
    // if You need accept un safe ssl, uncommented below line
    acceptUnsafeSsl(okHttpClient)

    return okHttpClient.build()
}

private fun acceptUnsafeSsl(okHttpClient: OkHttpClient.Builder) {
    // https://stackoverflow.com/a/63399149/5945169
    // Create a trust manager that does not validate certificate chains
    try {
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate?>? {
                    return arrayOf()
                }
            }
        )

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())

        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory
        val trustManagerFactory: TrustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers: Array<TrustManager> = trustManagerFactory.trustManagers
        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
            "Unexpected default trust managers:" + trustManagers.contentToString()
        }

        val trustManager = trustManagers[0] as X509TrustManager
        okHttpClient.sslSocketFactory(sslSocketFactory, trustManager)
        okHttpClient.hostnameVerifier { _, _ -> true }
    } catch (e: Exception) {
        throw RuntimeException(e)
    }
}

private fun setTimeOutToOkHttpClient(okHttpClientBuilder: OkHttpClient.Builder) =
    okHttpClientBuilder.apply {
        readTimeout(90, TimeUnit.SECONDS)
        connectTimeout(90, TimeUnit.SECONDS)
        writeTimeout(90, TimeUnit.SECONDS)
    }


fun getGson() = GsonBuilder().create()
