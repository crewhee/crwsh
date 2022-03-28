package ru.crwsh.mse.parser

import java.util.*

class Parser {
    fun Parse(inputStream: Scanner, variables: Map<String, String>): ParseResult? {
        var str = inputStream.nextLine().split("|", ">")
        while (str.isEmpty())
            str = inputStream.nextLine().split("|", ">")
//        return ParseResult(str[0].split(" ").first(), str[0].split(" ").drop(1), str.getOrElse(2) { "" })
        return null
    }
}