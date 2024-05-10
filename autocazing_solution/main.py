from fastapi import FastAPI
from db.session import SessionLocal
# from api import router as api_router

app = FastAPI()

# Dependency
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

# app.include_router(api_router)