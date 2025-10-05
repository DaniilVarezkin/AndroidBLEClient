package com.example.blescantest1.util.constants

import java.util.UUID

object BluetoothConstants {
    /**
     * UUID сервиса (Service UUID), под которым работает устройство.
     * У многих BLE-модулей (например, на чипах TI CC254x или HM-10).
     * Через этот сервис доступны кастомные характеристики для обмена данными.
     */
    val CTF_SERVICE_UUID: UUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb")

    /**
     * UUID характеристики (Characteristic UUID), относящейся к сервису выше.
     * Обычно `0xFFE1` используют как основную характеристику для обмена данными
     * (чтение/запись/уведомления). Именно через неё приложение отправляет
     * команды на устройство и получает ответы.
     */
    val CUSTOM_CHARACTERISTIC_UUID: UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb")

    /**
     * UUID стандартного дескриптора Client Characteristic Configuration Descriptor (CCCD).
     * Значение `0x2902` — это стандарт GATT.
     * Дескриптор нужен для включения/отключения уведомлений или индикаций
     * у характеристики (например, чтобы получать автоматические сообщения
     * от устройства при изменении данных).
     */
    val CCCD_UUID: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
}