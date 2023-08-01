package com.example.fmsafety

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ContactModel::class], version = 1, exportSchema = false)
public abstract  class FMDatabase: RoomDatabase() {

   abstract fun contactDao():ContactDao


   companion object{
       @Volatile
        var Instance:FMDatabase?=null
       fun getDatabase(context: Context):FMDatabase{


           Instance?.let {
               return  it
           }


          return  synchronized(FMDatabase::class.java){
               val instance= Room.databaseBuilder(context.applicationContext,FMDatabase::class.java,"FMData").build()

               Instance=instance

               instance
           }


       }
   }

}