package com.softcat.database.managers.local

import com.softcat.database.model.PlotDbModel

interface PlotManager {
    fun savePlot(model: PlotDbModel): Result<Unit>

    fun deletePlot(plotId: Int): Result<Unit>

    fun getUserPlots(userId: String): Result<List<PlotDbModel>>
}