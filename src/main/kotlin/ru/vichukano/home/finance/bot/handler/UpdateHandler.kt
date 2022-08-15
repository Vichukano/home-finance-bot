package ru.vichukano.home.finance.bot.handler

interface UpdateHandler<T, V> {

    fun handle(update: T): V

}