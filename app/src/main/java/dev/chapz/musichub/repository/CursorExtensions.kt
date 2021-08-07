package dev.chapz.musichub.repository

import android.database.Cursor

fun Cursor.getInt(columnName: String) : Int {
    try {
        return this.getInt(this.getColumnIndex(columnName))
    } catch (ex: Throwable) {
        throw IllegalStateException("Unknown column $columnName", ex)
    }
}

fun Cursor.getLong(columnName: String): Long {
    try {
        return this.getLong(this.getColumnIndex(columnName))
    } catch (ex: Throwable) {
        throw IllegalStateException("Unknown column $columnName", ex)
    }
}

fun Cursor.getString(columnName: String): String {
    try {
        return this.getString(this.getColumnIndex(columnName))
    } catch (ex: Throwable) {
        throw IllegalStateException("Unknown column $columnName", ex)
    }
}

fun Cursor.getStringOrNull(columnName: String): String? {
    try {
        return this.getString(this.getColumnIndex(columnName))
    } catch (ex: Throwable) {
        throw IllegalStateException("Unknown column $columnName", ex)
    }
}