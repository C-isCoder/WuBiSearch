package cn.congxiaodan.wubi

object WuBiContract {
    object WuBiEntry {
        const val TABLE_NAME = "wb"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_HZ = "hanzi"
        const val COLUMN_NAME_ZG = "zigen"
    }

    const val SQL_QUERY = "SELECT * FROM " +
        WuBiContract.WuBiEntry.TABLE_NAME +
        " WHERE " +
        WuBiContract.WuBiEntry.COLUMN_NAME_HZ +
        " == ?" +
        " ORDER BY " +
        WuBiContract.WuBiEntry.COLUMN_NAME_ID

    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${WuBiContract.WuBiEntry.TABLE_NAME}"

    const val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${WuBiContract.WuBiEntry.TABLE_NAME} (" +
            "${WuBiContract.WuBiEntry.COLUMN_NAME_ID} TEXT," +
            "${WuBiContract.WuBiEntry.COLUMN_NAME_HZ} TEXT," +
            "${WuBiContract.WuBiEntry.COLUMN_NAME_ZG} TEXT)"

}