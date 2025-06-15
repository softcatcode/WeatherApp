package com.softcat.database.commands

import android.icu.number.NumberFormatter.UnitWidth
import com.softcat.database.managers.local.PlotManager
import com.softcat.database.model.PlotDbModel

class SavePlotCommand(
    private val plot: PlotDbModel,
    private val plotManager: PlotManager
): Command {

    var result: Result<Unit>? = null
        private set

    override suspend fun execute() {
        result = plotManager.savePlot(plot)
    }

    override suspend fun rollback() {
        result = plotManager.deletePlot(plot.id)
    }
}