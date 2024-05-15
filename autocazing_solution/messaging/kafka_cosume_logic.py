from fastapi import Depends
import asyncio
from messaging.kafka_instance import create_consumer
from sqlalchemy.orm import Session
from json import loads
from db.mysql.session import mysqlSession
from db.mysql.models.ingredients import Ingredients


# 각 토픽별 Kafka 컨슈머 생성
ingredient_consumer = create_consumer('ingredient')
menu_consumer = create_consumer('menu')
order_consumer = create_consumer('order')
restock_order_consumer = create_consumer('restock_order')

def get_db_session() -> Session:
    return next(mysqlSession.get_db())

# 비동기 kafka 메시지 소비
async def consume_ingredient_messages():
    await ingredient_consumer.start()
    try:
        async for message in ingredient_consumer:
            print("Received from ingredient:", message.value)
            await process_ingredient_message(message.key, message.value)
    finally:
        await ingredient_consumer.stop()

async def process_ingredient_message(key: str, value: str):
    db: Session = get_db_session()
    try:
        new_ingredient = Ingredients(
            login_id=key,
            ingredient_name = value["ingredient_name"],
            ingredient_price = value["ingredient_price"],
            order_count = value["order_count"]
        )
        db.add(new_ingredient)
        db.commit()
    except Exception as e:
        db.rollback()
        print(f"Error processing ingredient message: {e}")

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