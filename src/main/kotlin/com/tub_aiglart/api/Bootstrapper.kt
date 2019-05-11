/*
 * API - A basic REST API made for tub-aiglart.com
 *
 * Copyright (C) 2019  Oskar Lang
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package com.tub_aiglart.api

import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.config.ConfigurationSource
import org.apache.logging.log4j.core.config.Configurator

fun main(args: Array<String>) {
    val options = Options()
            .addOption(
                    Option.builder("L")
                            .longOpt("log-level")
                            .hasArg()
                            .build()
            )
            .addOption(
                    Option.builder("D")
                            .longOpt("debug")
                            .build()
            )
            .addOption(
                    Option.builder("WS")
                            .longOpt("websocket")
                            .build()
            )
    val parser = DefaultParser()
    val cli = parser.parse(options, args)

    Configurator.setRootLevel(Level.toLevel(cli.getOptionValue("L"), Level.INFO))
    Configurator.initialize(ClassLoader.getSystemClassLoader(), ConfigurationSource(ClassLoader.getSystemResourceAsStream("log4j2.xml")))

    API(cli)
}
