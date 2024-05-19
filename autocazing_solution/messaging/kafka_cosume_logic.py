import asyncio
from datetime import datetime
from messaging.kafka_instance import create_consumer
from sqlalchemy import extract, func
from sqlalchemy.orm import Session
from db.mysql.session import mysqlSession
from db.mysql.models.ingredients import Ingredients
from db.mysql.models.menus import Menus
from db.mysql.models.menu_ingredients import MenuIngredients
from db.mysql.models.orders import Orders, OrderSpecifics
from db.mysql.models.restock_orders import RestockOrders, RestockOrderSpecifics
from db.mysql.models.reports import Reports, ExpirationSpecifics, IngredientSolution
from db.mysql.models.predicted_sales import PredictedSales
from pulp import LpMaximize, LpProblem, LpVariable, lpSum, LpStatus, value


# 각 토픽별 Kafka 컨슈머 생성
ingredient_consumer = create_consumer('ingredient')
menu_consumer = create_consumer('menu')
order_consumer = create_consumer('order')
restock_order_consumer = create_consumer('restock_order')
expiration_ingredients_consumer = create_consumer('expiration_ingredients')

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

async def consume_expiration_messages():
    await expiration_ingredients_consumer.start()
    try:
        async for message in expiration_ingredients_consumer:
            print("Received from restock_order:", message.value)
            await process_expiration_message(message.key, message.value)
    finally:
        await expiration_ingredients_consumer.stop()

async def process_expiration_message(key: str, value: dict):
    db: Session = get_db_session()
    try:
        today = datetime.utcnow().date()
        # Retrieve the report for today's date
        year_month = f"{today.year:04d}{today.month:02d}"
        report = db.query(Reports).filter(
            extract('year', Reports.created_at) == today.year,
            extract('month', Reports.created_at) == today.month,
            extract('day', Reports.created_at) == today.day,
            Reports.login_id == key
        ).first()
        # Create a new report if none exists for today
        if report is None:
            report = Reports(
                login_id=key,
                expected_monthly_sales = db.query(PredictedSales).filter(
                    PredictedSales.year_month == year_month,
                    PredictedSales.login_id == key
                    ).first(),
                current_monthly_sales = db.query(
                    func.sum(OrderSpecifics.menu_price * OrderSpecifics.menu_quantity)
                    ).join(Orders).filter(
                        Orders.login_id == key,
                        func.date_format(Orders.created_at, '%Y%m') == year_month
                    ).scalar() or 0
            )
            db.add(report)
            db.commit()
            db.refresh(report)  # Refresh to get the new report_id
        # Add expiration ingredients to the report
        for ingredient in value["expirationIngredients"]:
            new_expiration_specific = ExpirationSpecifics(
                report_id=report.report_id,
                ingredient_id=ingredient["ingredientId"],
                ingredient_name=ingredient["ingredientName"],
                remain=ingredient["remain"]
            )
            db.add(new_expiration_specific)
        db.commit()

        # Call function to solve ingredient solution and save results
        solve_and_save_ingredient_solution(report.report_id, key, db)
    except Exception as e:
        db.rollback()
        print(f"Error processing restock order message: {e}")
    finally:
        db.close()

def solve_and_save_ingredient_solution(report_id: int, login_id: int, db: Session):
    # Get all menus
    menus = db.query(MenuIngredients).all()
    menu_ids = list(set([menu.menu_id for menu in menus]))
    
    for ingredient_id in db.query(ExpirationSpecifics.ingredient_id).filter(ExpirationSpecifics.report_id == report_id).distinct():
        ingredient_id = ingredient_id[0]  # Extract actual ingredient_id from the tuple
        # Get remaining amount for the ingredient
        expiration_specific = db.query(ExpirationSpecifics).filter(
            ExpirationSpecifics.report_id == report_id,
            ExpirationSpecifics.ingredient_id == ingredient_id
        ).first()

        if not expiration_specific:
            continue
        
        remain_amount = expiration_specific.remain
        
        # 메뉴별 가격 및 이름 가져오기
        menus = db.query(Menus).filter(Menus.menu_id.in_(menu_ids)).all()
        menu_prices = {menu.menu_id: menu.menu_price for menu in menus}
        menu_names = {menu.menu_id: menu.menu_name for menu in menus}
        
        # 재료 사용량 가져오기
        ingredient_usage = {
            menu.menu_id: menu.capacity for menu in db.query(MenuIngredients).filter(MenuIngredients.menu_id.in_(menu_ids), MenuIngredients.ingredient_id == ingredient_id).all()
        }
        
        # 문제 정의
        model = LpProblem(name="Cafe_Product_Mix", sense=LpMaximize)
        # 변수 정의
        variables = {menu_id: LpVariable(name=f"menu_{menu_id}", lowBound=0, cat="Integer") for menu_id in menu_ids}
        # 목적 함수 (가격 기반)
        model += lpSum([menu_prices[menu_id] * variables[menu_id] for menu_id in menu_ids])
        # 제약 조건 (해당 재료의 총 사용량이 잔여량을 넘지 않도록)
        model += lpSum([ingredient_usage[menu_id] * variables[menu_id] for menu_id in menu_ids]) <= remain_amount, f"{ingredient_id}_Usage"
        # 문제 풀기
        model.solve()
        # 결과 출력
        optimal_sales = {menu_names[menu_id]: value(variables[menu_id]) for menu_id in menu_ids if value(variables[menu_id]) is not None and value(variables[menu_id]) > 0}

        # Save results to IngredientSolution table
        for menu_name, sale_quantity in optimal_sales.items():
            ingredient_solution = IngredientSolution(
                report_id=report_id,
                ingredient_id=ingredient_id,
                menu_name=menu_name,
                sale_quantity=sale_quantity
            )
            db.add(ingredient_solution)
        db.commit()

async def consume_messages():
    await asyncio.gather(
        consume_ingredient_messages(),
        consume_menu_messages(),
        consume_order_messages(),
        consume_restock_order_messages(),
        consume_expiration_messages(),
    )