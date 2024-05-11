from fastapi import FastAPI
from db.session import SessionLocal
from py_eureka_client import eureka_client
# from api import router as api_router

app = FastAPI()

# Eureka 서버 URL, FastAPI 애플리케이션의 호스트 & 포트 정보
eureka_server = "http://discovery-server:8761/eureka"
app_host = "solution-service"
app_port = 8088

# Eureka에 등록
eureka_client.init(eureka_server=eureka_server,
                                   app_name="solution-service",
                                   instance_port=app_port,
                                   instance_host=app_host)

eureka_client.start()

# Dependency
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@app.get("/api/fastapi-test")
async def root():
    return {"message": "Hello World"}

@app.on_event("shutdown")
def shutdown_event():
    eureka_client.stop()

# app.include_router(api_router)