package com.kuliah.adopet.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.kuliah.adopet.model.PetModelClass

class DatabaseHandler(context: Context): SQLiteOpenHelper(context,
    DATABASE_NAME,null,
    DATABASE_VERSION
) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "AdopetDatabase"

        private val TABLE_ACCOUNT = "AccountTable"
        private val KEY_ACCOUNT_ID = "account_id"
        private val KEY_EMAIL = "email"
        private val KEY_UNAME = "username"
        private val KEY_PASSWORD = "password"
        private val KEY_PHONE = "phone"
        private val KEY_LOGGED_IN = "logged_in"

        private val KEY_ADDRESS = "address"
        private val KEY_X = "x"
        private val KEY_Y = "y"

        private val TABLE_PET = "PetTable"
        private val KEY_PET_ID = "pet_id"
        private val KEY_PET_ACCOUNT_ID = "account_id"
        private val KEY_CATEGORY = "category"
        private val KEY_NAME = "name"
        private val KEY_SEX = "sex"
        private val KEY_SIZE = "size"
        private val KEY_AGE = "age"
        private val KEY_WEIGHT = "weight"
        private val KEY_TYPE = "type"
        private val KEY_VACCINE = "vaccine"
        private val KEY_DESCRIPTION = "description"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        // creating table with fields
        val CREATE_ACCOUNT_TABLE =
            ("CREATE TABLE $TABLE_ACCOUNT($KEY_ACCOUNT_ID INTEGER PRIMARY KEY, $KEY_UNAME TEXT, $KEY_PASSWORD TEXT, " +
                    "$KEY_EMAIL TEXT, $KEY_ADDRESS TEXT, $KEY_X TEXT, $KEY_Y TEXT, $KEY_LOGGED_IN INTEGER)")
        db?.execSQL(CREATE_ACCOUNT_TABLE)

        val CREATE_PET_TABLE =
            ("CREATE TABLE $TABLE_PET($KEY_PET_ID INTEGER PRIMARY KEY, $KEY_PET_ACCOUNT_ID INTEGER, $KEY_CATEGORY TEXT, $KEY_NAME TEXT, " +
                    "$KEY_ADDRESS TEXT, $KEY_X TEXT, $KEY_Y TEXT, $KEY_SEX TEXT, $KEY_SIZE TEXT, $KEY_AGE TEXT, $KEY_WEIGHT TEXT, " +
                    "$KEY_TYPE TEXT, $KEY_VACCINE TEXT, $KEY_DESCRIPTION TEXT)")
        db?.execSQL(CREATE_PET_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_ACCOUNT")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_PET")
        onCreate(db)
    }

    // Create akun
    fun createAccount(email: String, pass: String, uname: String): Boolean {
        val db = this.writableDatabase
        val getQuery = "SELECT * FROM $TABLE_ACCOUNT WHERE $KEY_EMAIL = '$email';"
        val getQuery2 = "SELECT * FROM $TABLE_ACCOUNT WHERE $KEY_UNAME = '$uname';"
        val cursorCheck: Cursor = db.rawQuery(getQuery, null)
        val cursorCheck2: Cursor = db.rawQuery(getQuery2, null)
//        val res: Int
        if (cursorCheck.count > 0 || cursorCheck2.count > 0) {
            Log.d("CREATION", "Failed create account, uname:$email $uname used")
            cursorCheck.close()
            db.close()
            return false
        } else {
            val addQuery =
                "INSERT INTO $TABLE_ACCOUNT($KEY_EMAIL, $KEY_PASSWORD, $KEY_UNAME, $KEY_LOGGED_IN) values('$email', '$pass', '$uname', -1);"
            val cursor: Cursor = db.rawQuery(addQuery, null)

            Log.d("CREATION", "Create account, uname:$uname, email:$email, pass:$pass")
            Log.d("CREATION", addQuery)
            Log.d("CREATION", getQuery)
            cursorCheck.close()
            try {
                if (cursor.moveToFirst()) {
                }
            } finally {
                cursor.close()
            }
            db.close()
            return true
        }
    }

    // Update akun
    fun updateAccount(id:Int, pass: String, uname: String, phone:String, address:String, x:String, y:String): Boolean {
        val db = this.writableDatabase
        val getQuery = "SELECT * FROM $TABLE_ACCOUNT WHERE $KEY_EMAIL = '$uname' WHERE NOT $KEY_ACCOUNT_ID = $id;"
        val cursorCheck: Cursor = db.rawQuery(getQuery, null)
//        val res: Int
        if (cursorCheck.count > 0) {
            Log.d("CREATION", "Failed create account, $uname used")
            cursorCheck.close()
            db.close()
            return false
        } else {
            val addQuery =
                "UPDATE $TABLE_ACCOUNT SET $KEY_UNAME = $uname, $KEY_PASSWORD = $pass, $KEY_PHONE = $phone, $KEY_ADDRESS = $address, $KEY_X = $x, $KEY_Y = $y WHERE $KEY_ACCOUNT_ID = $id;"
            val cursor: Cursor = db.rawQuery(addQuery, null)

            Log.d("CREATION", "Update account, uname:$uname, pass:$pass")
            Log.d("CREATION", addQuery)
            Log.d("CREATION", getQuery)
            cursorCheck.close()
            try {
                if (cursor.moveToFirst()) {
                }
            } finally {
                cursor.close()
            }
            db.close()
            return true
        }
    }

    // Login akun
    fun loginAccount(email: String, pass: String): Boolean {
        val db = this.writableDatabase

        val getQuery = "SELECT * FROM $TABLE_ACCOUNT WHERE $KEY_EMAIL = '$email';"
        val cursor: Cursor = db.rawQuery(getQuery, null)
        var res = true
        if (cursor.count > 0) {
            val updateQuery =
                "UPDATE $TABLE_ACCOUNT SET $KEY_LOGGED_IN = 1 WHERE $KEY_EMAIL = '$email' ;"
            val cursor2 = db.rawQuery(updateQuery, null)
            try {
                if (cursor2.moveToFirst()) {
                }
            } finally {
                cursor2.close()
            }
        } else {
            res = false
        }

        Log.d("CREATION", "Login account, email:$email, pass:$pass")
        cursor.close()
        db.close()
        return res
    }

    // Logout akun
    fun logoutAccount(id: Int): Boolean {
        val db = this.writableDatabase

        val getQuery = "SELECT * FROM $TABLE_ACCOUNT WHERE $KEY_ACCOUNT_ID = $id;"
        val cursor: Cursor = db.rawQuery(getQuery, null)
        var res = true
        if (cursor.count > 0) {
            val updateQuery =
                "UPDATE $TABLE_ACCOUNT SET $KEY_LOGGED_IN = -1 WHERE $KEY_ACCOUNT_ID = $id ;"
            val cursor2 = db.rawQuery(updateQuery, null)
            try {
                if (cursor2.moveToFirst()) {
                }
            } finally {
                cursor2.close()
            }
        } else {
            res = false
        }

        Log.d("CREATION", "Logout account, id:$id")
        try {
            if (cursor.moveToFirst()) {
            }
        } finally {
            cursor.close()
        }
        db.close()
        return res
    }

    // Check is logged in
    fun checkAccount(): Int {
        val db = this.writableDatabase

        val checkQuery = "SELECT * FROM $TABLE_ACCOUNT WHERE $KEY_LOGGED_IN = 1 LIMIT 1;"
        val cursor: Cursor = db.rawQuery(checkQuery, null)
        var loggedAccount = -1
        if (cursor.count > 0) {
            if (cursor.moveToFirst()) {
                do {
                    loggedAccount = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ACCOUNT_ID))
                } while (cursor.moveToNext())
            }
        }
        Log.d("CREATION", "Account logged id:$loggedAccount")
        cursor.close()
        db.close()
        return loggedAccount
    }

    fun getUname(id: Int): String {
        val db = this.readableDatabase

        val getQuery = "SELECT * FROM $TABLE_ACCOUNT WHERE $KEY_ACCOUNT_ID = $id;"
        val cursor: Cursor = db.rawQuery(getQuery, null)
        var unameAccount = ""
        if (cursor.count > 0) {
            if (cursor.moveToFirst()) {
                do {
                    unameAccount = cursor.getString(cursor.getColumnIndexOrThrow(KEY_UNAME))
                } while (cursor.moveToNext())
            }
        }
        Log.d("CREATION", "Account logged uname:$unameAccount")
        cursor.close()
        db.close()
        return unameAccount
    }

    // Create new Pet
    fun createAdopt(accID:Int, name: String, category: String, address: String, x:String, y:String, sex: String, size: String, age: String, weight: String, type: String, vaccine: String, desc: String): Boolean {
        val db = this.writableDatabase
        val addQuery = "INSERT INTO $TABLE_PET($KEY_PET_ACCOUNT_ID, $KEY_NAME, $KEY_CATEGORY, $KEY_ADDRESS, $KEY_X, $KEY_Y, $KEY_SEX, $KEY_SIZE, $KEY_AGE, $KEY_WEIGHT, $KEY_TYPE, $KEY_VACCINE, $KEY_DESCRIPTION) " +
                "values($accID, '$name', '$category', '$address', '$x', '$y', '$sex', '$size', '$age', '$weight', '$type', '$vaccine', '$desc');"
        val cursor: Cursor = db.rawQuery(addQuery, null)

        Log.d("CREATION", "Create pet, from acc ID:$accID")
        Log.d("CREATION", addQuery)
        try {
            if (cursor.moveToFirst()) {
            }
        } finally {
            cursor.close()
        }
        db.close()
        return true
    }

    // GET list pet
    fun getPet(Filter:String="",AccID:String=""): List<PetModelClass> {
        val empList: ArrayList<PetModelClass> = ArrayList<PetModelClass>()
        var selectQuery = "SELECT * FROM $TABLE_PET "
        if (Filter == "Dog") {
            selectQuery = "SELECT * FROM $TABLE_PET WHERE $KEY_CATEGORY = 'Dog';"
        } else if (Filter == "Cat") {
            selectQuery = "SELECT * FROM $TABLE_PET WHERE $KEY_CATEGORY = 'Cat';"
        } else if (Filter == "Account") {
            selectQuery = "SELECT * FROM $TABLE_PET WHERE $KEY_PET_ACCOUNT_ID = $AccID;"
        }
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        if (cursor.moveToFirst()) {
            do {
                val emp = PetModelClass(
                    accID = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PET_ACCOUNT_ID)),
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PET_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)) ,
                    category = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY)),
                    address = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ADDRESS)),
                    x = cursor.getString(cursor.getColumnIndexOrThrow(KEY_X)),
                    y = cursor.getString(cursor.getColumnIndexOrThrow(KEY_Y)),
                    sex = cursor.getString(cursor.getColumnIndexOrThrow(KEY_SEX)),
                    size = cursor.getString(cursor.getColumnIndexOrThrow(KEY_SIZE)),
                    age = cursor.getString(cursor.getColumnIndexOrThrow(KEY_AGE)),
                    weight = cursor.getString(cursor.getColumnIndexOrThrow(KEY_WEIGHT)),
                    type = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE)),
                    desc = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)),
                )
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return empList
    }
}