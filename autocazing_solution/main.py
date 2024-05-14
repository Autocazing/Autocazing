from fastapi import FastAPI
from core.config import get_setting
from db.mysql.session import SessionLocal
from py_eureka_client import eureka_client
from messaging.kafka_instance import producer, consumer
from api.monthly_sales.monthly_sales_router import monthly_sales_router
from messaging.kafka_cosume_logic import consume_messages
import asyncio
import logging

app = FastAPI(docs_url='/api/solution-service/docs', openapi_url='/api/solution-service/openapi.json')
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# 하위 api 라우터들 main에 추가
app.include_router(monthly_sales_router, prefix="/api/solution")

async def register_with_eureka():
    try:
        await eureka_client.init_async(
            eureka_server="http://discovery-server:8761/eureka",
            app_name="solution-service",
            instance_port=8088,
            instance_host="solution-service"
        )
        print("Registered with Eureka")
    except Exception as e:
        print(f"Failed to register with Eureka: {e}")

@app.on_event("startup")
async def startup_event():
    register_with_eureka()
    asyncio.create_task(consume_messages())

@app.get("/api/fastapi-test")
async def root():
    producer.send('test', value={"id": "test on python", "data": "{\"id\":\"test on python\",\"message\":\"test message python\"}"})
    return {"message": "Hello World"}

@app.on_event("shutdown")
async def shutdown_event():
    consumer.close()  # Kafka 컨슈머 종료
    eureka_client.stop()
