package com.softcat.database.managers.local

import com.softcat.database.internal.CursorMapperInterface
import com.softcat.database.internal.sqlExecutor.SQLiteInterface
import com.softcat.database.model.PlotDbModel
import javax.inject.Inject

class PlotManagerImpl @Inject constructor(
    private val executor: SQLiteInterface,
    private val mapper: CursorMapperInterface
): PlotManager {

    override fun savePlot(model: PlotDbModel): Result<Unit> {
        return try {
            executor.insertPlot(model)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun deletePlot(plotId: Int): Result<Unit> {
        return try {
            executor.deletePlot(plotId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getUserPlots(userId: String): Result<List<PlotDbModel>> {
        return try {
            val cursor = executor.getPlots(userId)
            val result = mapper.toPlots(cursor)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}