import asyncio
from messaging.kafka_instance import create_consumer
from sqlalchemy.orm import Session
from db.mysql.session import mysqlSession
from db.mysql.models.ingredients import Ingredients
from db.mysql.models.menus import Menus
from db.mysql.models.menu_ingredients import MenuIngredients
from db.mysql.models.orders import Orders, OrderSpecifics
from db.mysql.models.restock_orders import RestockOrders, RestockOrderSpecifics


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
            ingredient_id = value["ingredientId"],
            ingredient_name = value["ingredientName"],
            ingredient_price = value["ingredientPrice"],
            order_count = value["orderCount"]
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
            await process_menu_message(message.key, message.value)
    finally:
        await menu_consumer.stop()

async def process_menu_message(key: str, value: dict):
    db: Session = get_db_session()
    try:
        new_menu = Menus(
            login_id = key,
            menu_id = value["menuId"],
            menu_name = value["menuName"],
            menu_price = value["menuPrice"],
            on_event = value["onEvent"],
            discount_rate = value["discountRate"]
        )
        new_menu_ingredients = [MenuIngredients(
            login_id = key,
            menu_id = value["menuId"],
            ingredient_id = ing["ingredientId"],
            capacity = ing["capacity"]
        ) for ing in value["menuIngredients"]]
        db.add(new_menu)
        for ing in new_menu_ingredients:
            db.add(ing)
        db.commit()
    except Exception as e:
        db.rollback()
        print(f"Error processing menu message: {e}")
    finally:
        db.close()

async def consume_order_messages():
    await order_consumer.start()
    try:
        async for message in order_consumer:
            print("Received from order:", message.value)
            await process_order_message(message.key, message.value)
    finally:
        await order_consumer.stop()

async def process_order_message(key: str, value: dict):
    db: Session = get_db_session()
    try:
        new_order = Orders(
            login_id = key,
            order_id = value["orderId"],
            order_specifics = [OrderSpecifics(
                menu_name = spec["menuName"],
                menu_quantity = spec["menuQuantity"],
                menu_price = spec["menuPrice"]
            ) for spec in value["orderSpecifics"]]
        )
        db.add(new_order)
        db.commit()
    except Exception as e:
        db.rollback()
        print(f"Error processing order message: {e}")
    finally:
        db.close()

async def consume_restock_order_messages():
    await restock_order_consumer.start()
    try:
        async for message in restock_order_consumer:
            print("Received from restock_order:", message.value)
            await process_restock_order_message(message.key, message.value)
    finally:
        await restock_order_consumer.stop()

async def process_restock_order_message(key: str, value: dict):
    db: Session = get_db_session()
    try:
        new_restock_order = RestockOrders(
            login_id = key,
            restock_order_id = value["restockOrderId"],
            restock_order_specifics = [RestockOrderSpecifics(
                ingredient_name = spec["ingredientName"],
                ingredient_quantity = spec["ingredientQuantity"],
                ingredient_price = spec["ingredientPrice"],
                status = spec["restockSpecificStatus"]
            ) for spec in value["restockOrderSpecifics"]],
            status = value["status"]
        )
        db.add(new_restock_order)
        db.commit()
    except Exception as e:
        db.rollback()
        print(f"Error processing restock order message: {e}")
    finally:
        db.close()

async def consume_messages():
    await asyncio.gather(
        consume_ingredient_messages(),
        consume_menu_messages(),
        consume_order_messages(),
        consume_restock_order_messages(),
    )