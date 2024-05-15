from sqlalchemy import *
from sqlalchemy.sql import func
from db.mysql.base import Base

class Ingredients(Base):
    __tablename__ = 'ingredients'
    ingredient_id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    login_id = Column(Integer, nullable=False)

    ingredient_name = Column(String(length=20), nullable=False)
    ingredient_price = Column(Integer, nullable=False)
    order_count = Column(Integer, nullable=False)

    created_at = Column(DateTime, server_default=func.now(), nullable=False)
    updated_at = Column(DateTime, server_default=func.now(), nullable=False, onupdate=func.now())