from sqlalchemy import *
from sqlalchemy.orm import relationship
from sqlalchemy.sql import func
from enum import Enum as PyEnum
from db.mysql.base import Base

class RestockOrderSpecifics(Base):
    __tablename__ = 'restock_order_specifics'
    restock_order_id = Column(Integer, ForeignKey('restock_orders.restock_order_id'), nullable=False, primary_key=True) # SQLAlchemy는 PK가 없는 걸 허락하지 않음. 그래서 복합 PK를 만들어줌.
    ingredient_name = Column(String(length=20), nullable=False, primary_key=True)   # 복합 PK
    ingredient_quantity = Column(Integer, nullable=False)
    ingredient_price = Column(Integer, nullable=False)
    status = Column(String(length=10), nullable=False)

class RestockOrders(Base):
    __tablename__ = 'restock_orders'
    restock_order_id = Column(Integer, primary_key=True, autoincrement=True)
    login_id = Column(Integer, nullable=false)
    restock_order_specifics = relationship("RestockOrderSpecifics", cascade="all, delete-orphan")
    status = Column(String(length=20), server_default="WRITING", nullable=False)

    created_at = Column(DateTime, server_default=func.now(), nullable=False)
    updated_at = Column(DateTime, server_default=func.now(), nullable=False, onupdate=func.now())