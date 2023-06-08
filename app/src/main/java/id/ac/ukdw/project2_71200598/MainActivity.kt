package id.ac.ukdw.project2_71200598

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)

        btnTambah.setOnClickListener {
            tambahCatatan()
        }

        btnEdit.setOnClickListener {
            editCatatan()
        }

        btnHapus.setOnClickListener {
            hapusCatatan()
        }

        btnCari.setOnClickListener {
            cariCatatan()
        }

        tampilkanTotalPengeluaran()
        tampilkanDaftarPengeluaran()
    }

    private fun tambahCatatan() {
        val keterangan = edtKeterangan.text.toString()
        val nominal = edtNominal.text.toString().toDoubleOrNull()
        if (keterangan.isNotEmpty() && nominal != null) {
            val newRowId = databaseHelper.addExpense(keterangan, nominal)
            if (newRowId != -1L) {
                Toast.makeText(this, "Catatan pengeluaran ditambahkan", Toast.LENGTH_SHORT).show()
                tampilkanTotalPengeluaran()
                tampilkanDaftarPengeluaran()
                resetForm()
            } else {
                Toast.makeText(this, "Gagal menambahkan catatan pengeluaran", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Keterangan atau nominal tidak valid", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editCatatan() {
        val keterangan = edtKeterangan.text.toString()
        val nominal = edtNominal.text.toString().toDoubleOrNull()
        val id = edtId.text.toString().toIntOrNull()
        if (keterangan.isNotEmpty() && nominal != null && id != null) {
            val rowsAffected = databaseHelper.updateExpense(id, keterangan, nominal)
            if (rowsAffected > 0) {
                Toast.makeText(this, "Catatan pengeluaran diubah", Toast.LENGTH_SHORT).show()
                tampilkanTotalPengeluaran()
                tampilkanDaftarPengeluaran()
                resetForm()
            } else {
                Toast.makeText(this, "Gagal mengubah catatan pengeluaran", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Keterangan, nominal, atau ID tidak valid", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hapusCatatan() {
        val id = edtId.text.toString().toIntOrNull()
        if (id != null) {
            val rowsAffected = databaseHelper.deleteExpense(id)
            if (rowsAffected > 0) {
                Toast.makeText(this, "Catatan pengeluaran dihapus", Toast.LENGTH_SHORT).show()
                tampilkanTotalPengeluaran()
                tampilkanDaftarPengeluaran()
                resetForm()
            } else {
                Toast.makeText(this, "Gagal menghapus catatan pengeluaran", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "ID tidak valid", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cariCatatan() {
        val keterangan = edtCari.text.toString()
        if (keterangan.isNotEmpty()) {
            val expenses = databaseHelper.searchExpenses(keterangan)
            tampilkanDaftarPengeluaran(expenses)
        } else {
            Toast.makeText(this, "Masukkan keterangan untuk mencari catatan pengeluaran", Toast.LENGTH_SHORT).show()
        }
    }

    private fun tampilkanTotalPengeluaran() {
        val total = databaseHelper.getTotalExpenses()
        tvTotalPengeluaran.text = String.format("Total Pengeluaran: %.2f", total)
    }

    private fun tampilkanDaftarPengeluaran(expenses: List<Expense> = databaseHelper.getAllExpenses()) {
        val adapter = ExpenseAdapter(expenses)
        lvPengeluaran.adapter = adapter
    }

    private fun resetForm() {
        edtId.text = null
        edtKeterangan.text = null
        edtNominal.text = null
        edtCari.text = null
    }
}
