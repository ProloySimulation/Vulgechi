package com.xossapp.vulegechi.utils

import java.util.concurrent.atomic.AtomicInteger

object RandomUtil {
    private val randomNumber = AtomicInteger()
    fun getRandomInt() =  randomNumber.getAndIncrement()+System.currentTimeMillis().toInt()
}