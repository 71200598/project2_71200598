package id.ac.ukdw.project2_71200598

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "expense.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "expenses"
        private const val COLUMN_ID = "id"
        private const val COLUMN_KETERANGAN = "keterangan"
        private const val COLUMN_NOMINAL = "nominal"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_KETERANGAN TEXT, $COLUMN_NOMINAL REAL)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addExpense(keterangan: String, nominal: Double): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_KETERANGAN, keterangan)
            put(COLUMN_NOMINAL, nominal)
        }
        val newRowId = db.insert(TABLE_NAME, null, values)
        db.close()
        return newRowId
    }

    fun getAllExpenses(): List<Expense> {
        val expenses = mutableListOf<Expense>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery(selectQuery, null)
        cursor?.let {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                    val keterangan = cursor.getString(cursor.getColumnIndex(COLUMN_KETERANGAN))
                    val nominal = cursor.getDouble(cursor.getColumnIndex(COLUMN_NOMINAL))
                    val expense = Expense(id, keterangan, nominal)
                    expenses.add(expense)
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        db.close()
        return expenses
    }

    fun updateExpense(id: Int, keterangan: String, nominal: Double): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_KETERANGAN, keterangan)
            put(COLUMN_NOMINAL, nominal)
        }
        val rowsAffected = db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return rowsAffected
    }

    fun deleteExpense(id: Int): Int {
        val db = writableDatabase
        val rowsAffected = db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return rowsAffected
    }

    fun searchExpenses(keterangan: String): List<Expense> {
        val expenses = mutableListOf<Expense>()
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_KETERANGAN LIKE '%$keterangan%'"
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery(selectQuery, null)
        cursor?.let {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                    val keterangan = cursor.getString(cursor.getColumnIndex(COLUMN_KETERANGAN))
                    val nominal = cursor.getDouble(cursor.getColumnIndex(COLUMN_NOMINAL))
                    val expense = Expense(id, keterangan, nominal)
                    expenses.add(expense)
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        db.close()
        return expenses
    }

    fun getTotalExpenses(): Double {
        val selectQuery = "SELECT SUM($COLUMN_NOMINAL) AS total FROM $TABLE_NAME"
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery(selectQuery, null)
        var total = 0.0
        cursor?.let {
            if (cursor.moveToFirst()) {
                total = cursor.getDouble(cursor.getColumnIndex("total"))
            }
            cursor.close()
        }
        db.close()
        return total
    }
}
