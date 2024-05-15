import asyncio
from messaging.kafka_instance import ingredient_consumer, menu_consumer, order_consumer, restock_order_consumer

# 비동기 kafka 메시지 소비

async def consume_ingredient_messages():
    await ingredient_consumer.start()
    try:
        async for message in ingredient_consumer:
            print("Received from ingredient:", message.value)
            # ingredient 메시지 처리 로직 추가
    finally:
        await ingredient_consumer.stop()

async def consume_menu_messages():
    await menu_consumer.start()
    try:
        async for message in menu_consumer:
            print("Received from menu:", message.value)
            # menu 메시지 처리 로직 추가
    finally:
        await menu_consumer.stop()

async def consume_order_messages():
    await order_consumer.start()
    try:
        async for message in order_consumer:
            print("Received from order:", message.value)
            # order 메시지 처리 로직 추가
    finally:
        await order_consumer.stop()

async def consume_restock_order_messages():
    await restock_order_consumer.start()
    try:
        async for message in restock_order_consumer:
            print("Received from restock_order:", message.value)
            # restock_order 메시지 처리 로직 추가
    finally:
        await restock_order_consumer.stop()

async def consume_messages():
    await asyncio.gather(
        consume_ingredient_messages(),
        consume_menu_messages(),
        consume_order_messages(),
        consume_restock_order_messages(),
    )