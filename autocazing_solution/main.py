from fastapi import FastAPI
from db.session import SessionLocal
from py_eureka_client import eureka_client
# from api import router as api_router

app = FastAPI()

# Eureka 서버 URL, FastAPI 애플리케이션의 호스트 & 포트 정보
eureka_server = "http://your-eureka-server-host:port/eureka/"
app_host = "app_host_address"
app_port = 8000

# Eureka에 등록
eureka_client.init_registry_client(eureka_server=eureka_server,
                                   app_name="fastapi-service",
                                   instance_port=app_port,
                                   instance_host=app_host)

# Dependency
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@app.get("/fastapi-test")
async def root():
    return {"message": "Hello World"}

# app.include_router(api_router)