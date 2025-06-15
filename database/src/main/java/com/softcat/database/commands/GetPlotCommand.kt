package com.softcat.database.commands

import com.softcat.database.exceptions.NoDelayCommandException
import com.softcat.database.managers.local.PlotManager
import com.softcat.database.model.PlotDbModel

class GetPlotCommand(
    private val userId: String,
    private val plotManager: PlotManager
): Command {

    var result: Result<List<PlotDbModel>>? = null
        private set

    override suspend fun execute() {
        result = plotManager.getUserPlots(userId)
    }

    override suspend fun rollback() {
        throw NoDelayCommandException()
    }
}