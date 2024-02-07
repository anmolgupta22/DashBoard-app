package com.example.creatorlinkanalytics.di

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()

        val requestWithToken: Request = originalRequest.newBuilder()
            .header(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI"
            )
            .build()

        return chain.proceed(requestWithToken)
    }
}