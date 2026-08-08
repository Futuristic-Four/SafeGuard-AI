package com.example.data.db

import androidx.room.TypeConverter
import com.example.data.model.DemographicGroup
import com.example.data.model.TechLiteracy

class Converters {
    @TypeConverter
    fun fromDemographicGroup(group: DemographicGroup): String = group.name

    @TypeConverter
    fun toDemographicGroup(name: String): DemographicGroup = try {
        DemographicGroup.valueOf(name)
    } catch (e: Exception) {
        DemographicGroup.PROFESSIONALS_23_40
    }

    @TypeConverter
    fun fromTechLiteracy(literacy: TechLiteracy): String = literacy.name

    @TypeConverter
    fun toTechLiteracy(name: String): TechLiteracy = try {
        TechLiteracy.valueOf(name)
    } catch (e: Exception) {
        TechLiteracy.AVERAGE
    }
}
