from fastapi import FastAPI
from db.session import SessionLocal
from py_eureka_client import eureka_client
# from api import router as api_router

app = FastAPI()

@app.on_event("startup")
async def startup_event():
    await eureka_client.init_async(
        eureka_server="http://discovery-server:8761/eureka",
        app_name="solution-service",
        instance_port=8088,
        instance_host="solution-service"
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
    return {"message": "Hello World"}

@app.on_event("shutdown")
async def shutdown_event():
    eureka_client.stop()

# app.include_router(api_router)