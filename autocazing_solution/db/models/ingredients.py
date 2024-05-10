from sqlalchemy import *
from sqlalchemy.orm import relationship, declarative_base
from sqlalchemy.sql import func
from ..base import Base

class Ingredients(Base):
    __tablename__ = 'menus'
    ingredient_id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    store_id = Column(Integer, nullable=False)

    ingredient_name = Column(String(length=20), nullable=False)
    ingredient_price = Column(Integer, nullable=False)
    order_count = Column(Integer, nullable=False)

    created_at = Column(DateTime, default=func.now(), nullable=False)
    updated_at = Column(DateTime, default=func.now(), onupdate=func.now())