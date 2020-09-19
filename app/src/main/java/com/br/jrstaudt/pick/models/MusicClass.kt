package com.br.jrstaudt.pick.models

class MusicClass {

    var id: Int = 0
    var description: String? = null
    var date: String? = null

    constructor()

    constructor(id: Int, description: String, date: String) {
        this.id = id
        this.description = description
        this.date = date
    }

    companion object{
        val CLASSES_TABLE_NAME = "musicClasses_db"

        val CLASSES_ID = "id"
        val CLASSES_DESC = "description"
        val CLASSES_DATE = "date"

        val CREATE_TABLE = (
                "CREATE TABLE "
                    + CLASSES_TABLE_NAME + "("
                    + CLASSES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + CLASSES_DESC + " TEXT, "
                    + CLASSES_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")"
                )
    }
}