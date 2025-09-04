
package com.example.fikenastet.base.network

class NetworkError(val errorCode: Int, override val message: String?) : Throwable(message)
