package ru.vichukano.home.finance.bot.handler

import org.telegram.telegrambots.meta.api.objects.Update

fun userId(update: Update) =
    if (update.hasMessage()) update.message.from.id.toString() else throw IllegalStateException("User id not found")

fun chatId(update: Update) = update.message.chatId.toString()

fun message(update: Update) = update.message.text
