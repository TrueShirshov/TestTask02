package org.shirshov.testtask.network.utils


object Path {

    object Http {
        private const val API_VERSION = "/v1"
        const val LOGIN = "$API_VERSION/auth/login"
        const val SECURITY_GET = "$API_VERSION/securities"
        const val SIGNUP_RESET_PASSWORD_REQUEST = "$API_VERSION/auth/signup/reset-password-request"
        const val CANDLES_HISTORY = "$API_VERSION/candles/history"
        const val CANDLES_HISTORIES = "$API_VERSION/candles/histories"
        const val ACCOUNTS = "$API_VERSION/account"
    }

    object Server {
//        const val MARKETDATA_HTTP = BuildConfig.MARKETDATA_HTTP_URL
    }
}