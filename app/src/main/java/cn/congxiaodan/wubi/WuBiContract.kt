package cn.congxiaodan.wubi

import android.provider.BaseColumns

object WuBiContract {
    object WuBiEntry {
        const val TABLE_NAME = "wb"
        const val COLUMN_NAME_CHINESE = "chinese"
        const val COLUMN_NAME_CODE = "code"
    }

    const val SQL_QUERY = "SELECT * FROM " +
        WuBiContract.WuBiEntry.TABLE_NAME +
        " WHERE " +
        WuBiContract.WuBiEntry.COLUMN_NAME_CHINESE +
        " == ?" +
        " ORDER BY " +
        BaseColumns._ID

    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${WuBiContract.WuBiEntry.TABLE_NAME}"

    const val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${WuBiContract.WuBiEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${WuBiContract.WuBiEntry.COLUMN_NAME_CHINESE} TEXT," +
            "${WuBiContract.WuBiEntry.COLUMN_NAME_CODE} TEXT)"

}