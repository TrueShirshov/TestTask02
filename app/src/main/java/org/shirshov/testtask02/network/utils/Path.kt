package org.shirshov.testtask02.network.utils


object Path {

    object Http {
        const val SEARCH = "/search"
        const val LOOKUP = "/lookup"
        const val FIXTURES = "/fixtures.json"
        const val RESULTS = "/results.json"
    }

    object Server {
        const val API_ADDRESS = "http://storage.googleapis.com/cdn-og-test-api/test-task"
    }
}