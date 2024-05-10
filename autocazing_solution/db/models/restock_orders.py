from sqlalchemy import *
from sqlalchemy.orm import relationship, declarative_base
from sqlalchemy.sql import func
from enum import Enum as PyEnum
from db.base import Base

class RestockStatus(PyEnum):
    WRITING = "WRITING"
    ORDERED = "ORDERED"
    ON_DELIVERY = "ON_DELIVEREY"
    ARRIVED = "ARRIVED"
    COMPLETE = "COMPLETE"

class RestockOrderSpecific(Base):
    __tablename__ = 'restock_order_specifics'
    restock_order_id = Column(Integer, ForeignKey('restock_orders.restock_order_id'), nullable=False)
    ingredient_id = Column(Integer, nullable=False)
    ingredient_quantity = Column(Integer, nullable=False)
    ingredient_price = Column(Integer, nullable=False)

class RestockOrders(Base):
    __tablename__ = 'restock_orders'
    restock_order_id = Column(Integer, primary_key=True, autoincrement=True)
    store_id = Column(Integer, nullable=false)
    restock_order_specifics = relationship("RestockOrderSpecific", back_populates="restock_order", cascade="all, delete-orphan")
    status = Column(Enum(RestockStatus), default=RestockStatus.WRITING, nullable=False)

    created_at = Column(DateTime, default=func.now(), nullable=False)
    updated_at = Column(DateTime, default=func.now(), onupdate=func.now())