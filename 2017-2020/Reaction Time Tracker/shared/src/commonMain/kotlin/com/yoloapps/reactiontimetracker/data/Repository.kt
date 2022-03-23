package com.yoloapps.reactiontimetracker.data

import com.yoloapps.reactiontimetracker.DateUtils
import com.yoloapps.reactiontimetracker.data.db.Log
import com.yoloapps.reactiontimetracker.data.db.Prompt
import com.yoloapps.reactiontimetracker.data.db.Test
import com.yoloapps.reactiontimetracker.data.db.TestsDatabase

//object Repository/*(val databaseDriverFactory: DatabaseDriverFactory)*/ {
class Repository(databaseDriverFactory: DatabaseDriverFactory) {


    private val database = TestsDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.testsDatabaseQueries

    fun getLastTest(): TestData? {
        val test = dbQuery.selectLatestTest().executeAsOne()
        return test?.let { TestData(it, dbQuery.selectPromptsByDate(it.date).executeAsList()) }
    }

//    private fun mapTestSelecting(
//        date: Long,
//        score: Long,
//    ): TestEntity {
//        return TestEntity(date, score)
//    }
//
//    private fun mapPromptsSelecting(
//
//    ): List<TestData> {
//
//    }

    fun storeTest(data: TestData) {
        dbQuery.insertTest(data.test.date, data.test.score)
        data.prompts.forEach { prompt ->
            dbQuery.insertPrompt(prompt.date, prompt.number, prompt.time, prompt.response_time)
        }
    }

    fun storeLog(log: Log) {
        //TODO("fix date stuff (make it local and etc.)")
        DateUtils.getDayRange(log.date).also {
            deleteLogsByRange(it[0], it[1])
        }
        dbQuery.insertLog(log.date, log.quality, log.hours, log.note)
    }

    private fun deleteLogsByRange(start: Long, end: Long) {
        dbQuery.removeLogsByRange(start, end)
    }

//    fun storePrompts(data: List<Prompt>) {
//
//    }
//
//    fun getTestsByWeek(date: Long): Array<TestData> {
//
//    }
//
//    fun getTestsByMonth(date: Long): Array<TestData> {
//
//    }
//
//    fun getTestsByYear(date: Long): Array<TestData> {
//
//    }
//

    fun getTestsByRange(start: Long, end: Long): List<Test> {
        return dbQuery.selectTestsByRange(start, end).executeAsList()
    }
    fun getTestsByRange(range: List<Long>): List<Test> {
        return dbQuery.selectTestsByRange(range[0], range[1]).executeAsList()
    }

    fun getPromptsByRange(start: Long, end: Long): List<Prompt> {
        return dbQuery.selectPromptsByRange(start, end).executeAsList()
    }
    fun getPromptsByRange(range: List<Long>): List<Prompt> {
        return dbQuery.selectPromptsByRange(range[0], range[1]).executeAsList()
    }

    fun getLogsByRange(start: Long, end: Long): List<Log> {
        return dbQuery.selectLogsByRange(start, end).executeAsList()
    }
    fun getLogsByRange(range: List<Long>): List<Log> {
        return dbQuery.selectLogsByRange(range[0], range[1]).executeAsList()
    }

    fun getAllTests(): List<Test> {
        return getTestsByRange(0, Long.MAX_VALUE)
    }

    fun getAllPrompts(): List<Prompt> {
        return getPromptsByRange(0, Long.MAX_VALUE)
    }

    fun getAllLogs(): List<Log> {
        return dbQuery.selectAllLogs().executeAsList()
    }

    fun deleteByRange(start: Long, end: Long) {
        dbQuery.removeTestsByRange(start, end)
    }

    fun deleteAll() {
        deleteByRange(Long.MIN_VALUE, Long.MAX_VALUE)
    }
}