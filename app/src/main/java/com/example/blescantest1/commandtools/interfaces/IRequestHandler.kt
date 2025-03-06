package com.example.blescantest1.commandtools.interfaces

import android.media.tv.TsRequest

interface IRequestHandler<in TRequest, TResponse> where TRequest: IRequest<TResponse>{
    fun execute(command: TRequest): TResponse
}