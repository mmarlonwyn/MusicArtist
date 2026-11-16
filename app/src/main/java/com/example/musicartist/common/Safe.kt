package com.example.musicartist.common

import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException

class AppException(message: String) : Exception(message)

suspend fun <T> safeApi(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: AppException) {
    Result.failure(e)
} catch (e: HttpException) {
    Result.failure(AppException("HTTP ${e.code()}"))
} catch (e: IOException) {
    Result.failure(AppException(e.message ?: "Koneksi gagal"))
} catch (e: Exception) {
    Result.failure(AppException(e.message ?: "Terjadi kesalahan"))
}
