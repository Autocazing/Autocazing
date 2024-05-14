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
influx_connection = InfluxDBConnection(
    host=settings.INFLUXDB_HOST,
    port=settings.INFLUXDB_PORT,
    username=settings.INFLUXDB_USER_NAME,
    password=settings.INFLUXDB_USER_PASSWORD,
    token=settings.INFLUXDB_USER_TOKEN
)

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

async def connect_to_influxdb():
    try:
        influx_connection.connect()
        print("Connected to InfluxDB")
    except Exception as e:
        print(f"Failed to connect to InfluxDB: {e}")

async def start_kafka_consumer():
    try:
        asyncio.create_task(consume_messages())
        print("Kafka consumer started")
    except Exception as e:
        print(f"Failed to start Kafka consumer: {e}")

@app.on_event("startup")
async def startup_event():
    await asyncio.gather(
        register_with_eureka(),
        connect_to_influxdb(),
        start_kafka_consumer()
    )

# Dependency
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@app.get("/api/fastapi-test")
async def root():
    producer.send('test', value={"id": "test on python", "data": "{\"id\":\"test on python\",\"message\":\"test message python\"}"})
    return {"message": "Hello World"}

@app.on_event("shutdown")
async def shutdown_event():
    consumer.close()  # Kafka 컨슈머 종료
    influx_connection.client.close()
    eureka_client.stop()
