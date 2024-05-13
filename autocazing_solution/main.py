from fastapi import FastAPI
from core.config import get_setting
from db.mysql.session import SessionLocal
from py_eureka_client import eureka_client
import asyncio
from messaging.kafka_instance import producer, consumer
from api.monthly_sales.monthly_sales_router import monthly_sales_router
from messaging.kafka_cosume_logic import consume_messages
from db.influxdb.connection import InfluxDBConnection

app = FastAPI(docs_url='/api/solution-service/docs', openapi_url='/api/solution-service/openapi.json')
settings = get_setting()
# 하위 api 라우터들 main에 추가
app.include_router(monthly_sales_router, prefix="/api/solution")
# InfluxDB 연결 객체 생성
influx_connection = InfluxDBConnection(token=settings.INFLUX_DB_TOKEN)

@app.on_event("startup")
async def startup_event():
    await eureka_client.init_async(
        eureka_server="http://discovery-server:8761/eureka",
        app_name="solution-service",
        instance_port=8088,
        instance_host="solution-service"
    )
    asyncio.create_task(consume_messages())  # Kafka 메시지 수신을 위한 비동기 태스크 생성

# Dependency
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@app.get("/api/fastapi-test")
async def root():
    producer.send('test', value={"id":"test on python", "data":"{\"id\":\"test on python\",\"message\":\"test message python\"}"})
    return {"message": "Hello World"}

@app.on_event("shutdown")
async def shutdown_event():
    eureka_client.stop()
    consumer.close()  # Kafka 컨슈머 종료
