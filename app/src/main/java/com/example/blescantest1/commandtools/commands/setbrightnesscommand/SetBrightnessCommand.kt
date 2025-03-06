package com.example.blescantest1.commandtools.commands.setbrightnesscommand

import com.example.blescantest1.commandtools.interfaces.IRequest

data class SetBrightnessCommand(val brightness: Int) : IRequest<Unit>